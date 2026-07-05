package com.viral.ai.engine

class Humanizer {
    private val roboticPhrases = listOf(
        "As an AI", "Certainly", "I'd be happy to help", "Here's a detailed explanation",
        "Let's dive in", "This depends", "Overall", "In conclusion", "Hope this helps",
        "I understand", "Absolutely"
    )

    fun humanize(text: String): String {
        var humanized = text
        roboticPhrases.forEach { phrase ->
            humanized = humanized.replace(phrase, "", ignoreCase = true)
        }
        
        // Remove empty lines created by removals
        humanized = humanized.lines()
            .filter { it.trim().isNotEmpty() }
            .joinToString("\n")
            
        return humanized.trim()
    }
}
