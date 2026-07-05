package com.viral.ai.engine

class ResponseCleaner {
    fun clean(text: String): String {
        val lines = text.lines()
        val uniqueLines = mutableSetOf<String>()
        val result = mutableListOf<String>()
        
        lines.forEach { line ->
            val trimmed = line.trim()
            if (trimmed.isNotEmpty() && !uniqueLines.contains(trimmed)) {
                uniqueLines.add(trimmed)
                result.add(line)
            }
        }
        
        return result.joinToString("\n")
    }
}
