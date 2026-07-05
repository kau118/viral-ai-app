package com.viral.ai.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Chat(
    val id: String = "",
    val userId: String = "",
    val title: String = "New Chat",
    val isPinned: Boolean = false,
    @ServerTimestamp val createdAt: Date? = null,
    @ServerTimestamp val updatedAt: Date? = null
)

data class ChatMessage(
    val id: String = "",
    val chatId: String = "",
    val text: String = "",
    val isUser: Boolean = true,
    @ServerTimestamp val timestamp: Date? = null
)
