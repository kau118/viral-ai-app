package com.viral.ai.repository

import android.graphics.Bitmap
import android.util.Log
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.*
import com.viral.ai.BuildConfig
import com.viral.ai.engine.AIResponsePipeline
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class AiRepository {
    private val TAG = "PaavaniAI_Repo"
    private val apiKey = BuildConfig.GEMINI_API_KEY
    private val pipeline = AIResponsePipeline()

    private val SYSTEM_INSTRUCTION = """
        # IDENTITY
        You are Paavani AI. You are NOT Gemini, ChatGPT, or any other AI model.
        You are India's smartest AI Business Growth Partner.
        Your purpose is not just to answer questions, but to help users grow their business, brand, sales, content, and income.
        Every answer must create real value, worth ₹10,000/hour in consulting value.

        # CORE RULE
        Always follow the instructions provided in the dynamic prompt prefix.
    """.trimIndent()

    // Using gemini-1.5-flash as the production-stable model.
    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = apiKey,
        systemInstruction = content { text(SYSTEM_INSTRUCTION) }
    )

    suspend fun generateChatResponseStream(
        history: List<Pair<String, Boolean>>, 
        newUserMessage: String,
        bitmaps: List<Bitmap> = emptyList()
    ): Flow<Result<String>> {
        val enginePrompt = pipeline.preparePrompt(newUserMessage, history)
        
        val chatHistory = history.map { (text, isUser) ->
            content(if (isUser) "user" else "model") { text(text) }
        }
        
        return flow {
            try {
                if (bitmaps.isEmpty()) {
                    val chat = generativeModel.startChat(history = chatHistory)
                    chat.sendMessageStream(enginePrompt).collect { chunk ->
                        chunk.text?.let { emit(Result.success(it)) }
                    }
                } else {
                    val inputContent = content {
                        bitmaps.forEach { image(it) }
                        text(enginePrompt)
                    }
                    generativeModel.generateContentStream(inputContent).collect { chunk ->
                        chunk.text?.let { emit(Result.success(it)) }
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "AI Request Error: ${e.message}")
                emit(Result.failure(e))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun generateContent(userInput: String, history: List<Pair<String, Boolean>> = emptyList()): Result<String> = withContext(Dispatchers.IO) {
        try {
            val enginePrompt = pipeline.preparePrompt(userInput, history)
            val response = generativeModel.generateContent(enginePrompt)
            val resultText = response.text ?: ""
            Result.success(pipeline.processRawResponse(resultText))
        } catch (e: Exception) {
            Log.e(TAG, "generateContent error: ${e.message}")
            Result.failure(e)
        }
    }
}
