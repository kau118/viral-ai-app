package com.viral.ai.engine

class ConversationMemory {
    private val memory = mutableListOf<Pair<String, Boolean>>()

    fun store(text: String, isUser: Boolean) {
        memory.add(text to isUser)
        if (memory.size > 20) {
            memory.removeAt(0)
        }
    }

    fun getRecentHistory(): List<Pair<String, Boolean>> = memory.toList()

    fun clear() {
        memory.clear()
    }
}
