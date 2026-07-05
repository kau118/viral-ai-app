package com.viral.ai.engine

class FollowUpGenerator {
    fun generate(intent: AiIntent): List<String> {
        return when (intent) {
            AiIntent.BUSINESS -> listOf("How can I scale this?", "What is the ROI?", "Next steps?")
            AiIntent.MARKETING -> listOf("More reel ideas?", "Best hashtags?", "How to track reach?")
            AiIntent.CODING -> listOf("Explain this part?", "How to optimize?", "Add error handling?")
            AiIntent.HEALTH -> listOf("Meal plan for this?", "Morning routine?", "Weekly schedule?")
            else -> listOf("Tell me more.", "Give examples.", "What's next?")
        }
    }
}
