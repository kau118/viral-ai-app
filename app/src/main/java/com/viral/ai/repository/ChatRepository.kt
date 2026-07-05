package com.viral.ai.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.viral.ai.model.Chat
import com.viral.ai.model.ChatMessage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout

class ChatRepository {
    private val TAG = "PaavaniAI_ChatRepo"
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val userId: String get() = auth.currentUser?.uid ?: ""

    fun getChats(): Flow<List<Chat>> = callbackFlow {
        if (userId.isEmpty()) {
            trySend(emptyList())
            return@callbackFlow
        }
        val subscription = db.collection("users").document(userId).collection("chats")
            .orderBy("isPinned", Query.Direction.DESCENDING)
            .orderBy("updatedAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e(TAG, "Snapshot error (chats): ${error.message}")
                    return@addSnapshotListener
                }
                val chats = snapshot?.documents?.mapNotNull { it.toObject(Chat::class.java)?.copy(id = it.id) } ?: emptyList()
                trySend(chats)
            }
        awaitClose { subscription.remove() }
    }

    suspend fun createChat(title: String): String {
        Log.d(TAG, "Creating chat: $title")
        val chat = Chat(userId = userId, title = title, createdAt = java.util.Date(), updatedAt = java.util.Date())
        return try {
            val doc = withTimeout(8000) {
                db.collection("users").document(userId).collection("chats").add(chat).await()
            }
            Log.d(TAG, "Chat created with ID: ${doc.id}")
            doc.id
        } catch (e: Exception) {
            Log.e(TAG, "Failed to create chat: ${e.message}")
            throw e
        }
    }

    suspend fun updateChatTitle(chatId: String, title: String) {
        db.collection("users").document(userId).collection("chats").document(chatId)
            .update("title", title, "updatedAt", java.util.Date()).await()
    }

    suspend fun togglePinChat(chatId: String, isPinned: Boolean) {
        db.collection("users").document(userId).collection("chats").document(chatId)
            .update("isPinned", isPinned).await()
    }

    suspend fun deleteChat(chatId: String) {
        val messages = db.collection("users").document(userId).collection("chats").document(chatId).collection("messages").get().await()
        db.runBatch { batch ->
            messages.forEach { batch.delete(it.reference) }
            batch.delete(db.collection("users").document(userId).collection("chats").document(chatId))
        }.await()
    }

    fun getMessages(chatId: String): Flow<List<ChatMessage>> = callbackFlow {
        Log.d(TAG, "Subscribing to messages for: $chatId")
        val subscription = db.collection("users").document(userId).collection("chats").document(chatId)
            .collection("messages")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e(TAG, "Snapshot error (messages): ${error.message}")
                    return@addSnapshotListener
                }
                val messages = snapshot?.documents?.mapNotNull { it.toObject(ChatMessage::class.java)?.copy(id = it.id) } ?: emptyList()
                Log.d(TAG, "Received ${messages.size} messages from DB")
                trySend(messages)
            }
        awaitClose { subscription.remove() }
    }

    suspend fun saveMessage(chatId: String, text: String, isUser: Boolean) {
        Log.d(TAG, "Saving message to DB (isUser: $isUser)")
        val message = ChatMessage(chatId = chatId, text = text, isUser = isUser, timestamp = java.util.Date())
        try {
            withTimeout(8000) {
                db.collection("users").document(userId).collection("chats").document(chatId)
                    .collection("messages").add(message).await()
                db.collection("users").document(userId).collection("chats").document(chatId)
                    .update("updatedAt", java.util.Date()).await()
            }
            Log.d(TAG, "Message saved successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to save message: ${e.message}")
            throw e
        }
    }
}
