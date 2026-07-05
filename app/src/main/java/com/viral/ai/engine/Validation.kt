package com.viral.ai.engine

class ResponseValidator {
    fun isValid(response: String): Boolean {
        return response.isNotBlank() && response.length > 2
    }
}

class SafetyChecker {
    private val blockedWords = listOf("harmful", "illegal", "toxic") // Simplified for demo
    
    fun isSafe(text: String): Boolean {
        val lower = text.lowercase()
        return blockedWords.none { lower.contains(it) }
    }
}

class MarkdownFormatter {
    fun format(text: String): String {
        // Ensures proper spacing for markdown elements
        return text.replace(Regex("(?m)^- "), "• ")
            .replace("\n\n\n", "\n\n")
            .trim()
    }
}
