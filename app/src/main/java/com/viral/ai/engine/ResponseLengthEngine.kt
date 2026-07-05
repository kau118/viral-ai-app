package com.viral.ai.engine

class ResponseLengthEngine {
    fun calculateRequiredLength(input: String): ResponseLength {
        val wordCount = input.trim().split(Regex("\\s+")).size
        return when {
            wordCount <= 2 && (input.contains("hi", true) || input.contains("hello", true)) -> ResponseLength.TINY
            input.contains("teach", true) || input.contains("explain", true) || input.contains("how to", true) || input.contains("steps", true) -> ResponseLength.DETAILED
            wordCount > 15 -> ResponseLength.MEDIUM
            else -> ResponseLength.SHORT
        }
    }
}
