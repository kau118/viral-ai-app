package com.viral.ai.viewmodel

import android.app.Application
import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.viral.ai.model.Chat
import com.viral.ai.model.ChatMessage
import com.viral.ai.repository.AiRepository
import com.viral.ai.repository.ChatRepository
import com.viral.ai.engine.AIResponsePipeline
import com.viral.ai.engine.FollowUpGenerator
import com.viral.ai.engine.IntentDetector
import com.viral.ai.util.ConnectivityObserver
import com.viral.ai.util.NetworkConnectivityObserver
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.Dispatchers

class ChatViewModel(application: Application) : AndroidViewModel(application) {

    private val TAG = "PaavaniAI_ChatVM"
    private val aiRepository = AiRepository()
    private val chatRepository = ChatRepository()
    private val pipeline = AIResponsePipeline()
    private val followUpGenerator = FollowUpGenerator()
    private val intentDetector = IntentDetector()
    private val connectivityObserver = NetworkConnectivityObserver(application)
    
    private val _chats = MutableStateFlow<List<Chat>>(emptyList())
    val chats = _chats.asStateFlow()

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages = _messages.asStateFlow()

    private val _networkStatus = MutableStateFlow(ConnectivityObserver.Status.Available)
    val networkStatus = _networkStatus.asStateFlow()

    var currentChatId by mutableStateOf<String?>(null)
    var isAiLoading by mutableStateOf(false)
    var streamingText by mutableStateOf("")
    var chatSearchQuery by mutableStateOf("")
    
    private val _followUps = MutableStateFlow<List<String>>(emptyList())
    val followUps = _followUps.asStateFlow()

    private val _selectedBitmaps = mutableStateListOf<Bitmap>()
    val selectedBitmaps: List<Bitmap> get() = _selectedBitmaps

    private var chatJob: Job? = null
    private var streamingJob: Job? = null

    init {
        Log.d(TAG, "ChatViewModel initialized")
        viewModelScope.launch {
            chatRepository.getChats().catch { e ->
                Log.e(TAG, "Error fetching chats: ${e.message}")
            }.collectLatest {
                _chats.value = it
            }
        }
        
        viewModelScope.launch {
            connectivityObserver.observe().collect {
                _networkStatus.value = it
            }
        }
    }

    fun addSelectedBitmap(bitmap: Bitmap) {
        if (_selectedBitmaps.size < 4) {
            _selectedBitmaps.add(bitmap)
        }
    }

    fun removeSelectedBitmap(bitmap: Bitmap) {
        _selectedBitmaps.remove(bitmap)
    }

    fun selectChat(chatId: String) {
        Log.d(TAG, "selectChat() called for: $chatId")
        currentChatId = chatId
        chatJob?.cancel()
        chatJob = viewModelScope.launch {
            chatRepository.getMessages(chatId).catch { e ->
                Log.e(TAG, "Error fetching messages: ${e.message}")
            }.collectLatest {
                Log.d(TAG, "Updating messages state with ${it.size} items")
                _messages.value = it
            }
        }
    }

    fun createNewChat(title: String = "New Chat") {
        viewModelScope.launch {
            try {
                Log.d(TAG, "createNewChat() called")
                withTimeout(10000) {
                    val id = chatRepository.createChat(title)
                    selectChat(id)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Failed to create chat: ${e.message}")
            }
        }
    }

    fun sendMessage(text: String) {
        Log.d(TAG, "1. User clicked Send")
        if ((text.isBlank() && _selectedBitmaps.isEmpty()) || isAiLoading) {
            Log.d(TAG, "   Aborting: input empty or already loading")
            return
        }
        
        Log.d(TAG, "2. sendMessage() started")
        val bitmapsToSend = _selectedBitmaps.toList()
        _selectedBitmaps.clear()
        
        // Optimistic UI update
        val tempMessage = ChatMessage(text = text, isUser = true, timestamp = java.util.Date())
        _messages.value = _messages.value + tempMessage
        isAiLoading = true
        _followUps.value = emptyList()
        
        viewModelScope.launch(Dispatchers.Main) {
            try {
                Log.d(TAG, "3. ViewModel processing request in coroutine")
                var chatId = currentChatId
                if (chatId == null) {
                    Log.d(TAG, "   Creating new chat session...")
                    chatId = withTimeout(12000) {
                        chatRepository.createChat(text.take(30).ifBlank { "Visual Analysis" } + "...")
                    }
                    currentChatId = chatId
                    selectChat(chatId!!)
                }
                
                Log.d(TAG, "   Saving user message to DB...")
                withTimeout(10000) {
                    chatRepository.saveMessage(chatId!!, text, true)
                }
                
                generateResponse(chatId!!, text, bitmapsToSend)
                
            } catch (e: Exception) {
                Log.e(TAG, "   Critical failure in sendMessage: ${e.message}", e)
                isAiLoading = false
                streamingText = "Delivery failed: ${e.message}"
                delay(3000)
                if (streamingText.startsWith("Delivery failed")) {
                    streamingText = ""
                }
            }
        }
    }

    private fun generateResponse(chatId: String, text: String, bitmaps: List<Bitmap> = emptyList()) {
        Log.d(TAG, "   Starting generateResponse()")
        isAiLoading = true
        streamingText = ""
        
        streamingJob?.cancel()
        streamingJob = viewModelScope.launch(Dispatchers.Main) {
            try {
                Log.d(TAG, "   Collecting history...")
                val history = _messages.value.filter { it.id.isNotEmpty() }.map { it.text to it.isUser }
                
                Log.d(TAG, "4. Repository request start")
                withTimeout(100000) { // 100s timeout
                    aiRepository.generateChatResponseStream(history, text, bitmaps).collect { result ->
                        result.onSuccess { chunk ->
                            if (streamingText.isEmpty()) Log.d(TAG, "6. HTTP response started receiving")
                            streamingText += chunk
                        }.onFailure { error ->
                            Log.e(TAG, "5. HTTP request failed or error emitted", error)
                            throw error
                        }
                    }
                }
                
                Log.d(TAG, "7. Response received completely")
                if (streamingText.isNotEmpty()) {
                    Log.d(TAG, "   Finalizing and saving response...")
                    val processedText = pipeline.processRawResponse(streamingText)
                    try {
                        withTimeout(15000) {
                            chatRepository.saveMessage(chatId, processedText, false)
                        }
                        Log.d(TAG, "8. UI and DB updated successfully")
                        val intent = intentDetector.detect(text)
                        _followUps.value = followUpGenerator.generate(intent)
                        streamingText = ""
                    } catch (e: Exception) {
                        Log.e(TAG, "   Failed to save AI response: ${e.message}")
                    }
                }
            } catch (e: TimeoutCancellationException) {
                Log.e(TAG, "   AI request timed out")
                streamingText = "Connection timed out. Gemini took too long to respond."
            } catch (e: Exception) {
                Log.e(TAG, "   AI Connection issue: ${e.message}", e)
                streamingText = "AI Connection issue: ${e.message}"
            } finally {
                Log.d(TAG, "9. Loading finished (Resetting state)")
                isAiLoading = false
            }
        }
    }

    fun stopGeneration() {
        Log.d(TAG, "stopGeneration() called")
        streamingJob?.cancel()
        isAiLoading = false
        val chatId = currentChatId
        if (chatId != null && streamingText.isNotEmpty()) {
            viewModelScope.launch {
                val processed = pipeline.processRawResponse(streamingText)
                try {
                    chatRepository.saveMessage(chatId, processed, false)
                    streamingText = ""
                } catch (e: Exception) { }
            }
        }
    }

    fun deleteChat(chatId: String) {
        viewModelScope.launch {
            try {
                chatRepository.deleteChat(chatId)
                if (currentChatId == chatId) {
                    currentChatId = null
                    _messages.value = emptyList()
                }
            } catch (e: Exception) { }
        }
    }

    fun togglePin(chatId: String, isPinned: Boolean) {
        viewModelScope.launch {
            try {
                chatRepository.togglePinChat(chatId, isPinned)
            } catch (e: Exception) { }
        }
    }
}
