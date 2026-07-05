package com.viral.ai.engine

class IntentDetector {
    fun detect(text: String): AiIntent {
        val lowerText = text.lowercase()
        return when {
            lowerText.contains("business") || lowerText.contains("startup") || lowerText.contains("revenue") -> AiIntent.BUSINESS
            lowerText.contains("marketing") || lowerText.contains("ads") || lowerText.contains("growth") -> AiIntent.MARKETING
            lowerText.contains("health") || lowerText.contains("doctor") || lowerText.contains("medicine") -> AiIntent.HEALTH
            lowerText.contains("fit") || lowerText.contains("gym") || lowerText.contains("workout") -> AiIntent.FITNESS
            lowerText.contains("teach") || lowerText.contains("learn") || lowerText.contains("education") -> AiIntent.EDUCATION
            lowerText.contains("code") || lowerText.contains("kotlin") || lowerText.contains("android") || lowerText.contains("bug") -> AiIntent.CODING
            lowerText.contains("travel") || lowerText.contains("flight") || lowerText.contains("hotel") -> AiIntent.TRAVEL
            lowerText.contains("buy") || lowerText.contains("price") || lowerText.contains("shopping") -> AiIntent.SHOPPING
            lowerText.contains("money") || lowerText.contains("finance") || lowerText.contains("investment") -> AiIntent.FINANCE
            lowerText.contains("legal") || lowerText.contains("law") || lowerText.contains("court") -> AiIntent.LEGAL
            lowerText.contains("movie") || lowerText.contains("music") || lowerText.contains("fun") -> AiIntent.ENTERTAINMENT
            lowerText.contains("image") || lowerText.contains("draw") || lowerText.contains("generate picture") -> AiIntent.IMAGE_PROMPT
            lowerText.contains("video") || lowerText.contains("reel") || lowerText.contains("short") -> AiIntent.VIDEO_PROMPT
            lowerText.contains("write") || lowerText.contains("story") || lowerText.contains("poem") -> AiIntent.CREATIVE_WRITING
            lowerText.contains("job") || lowerText.contains("career") || lowerText.contains("resume") -> AiIntent.CAREER
            lowerText.contains("lifestyle") || lowerText.contains("habit") -> AiIntent.LIFESTYLE
            lowerText.contains("fashion") || lowerText.contains("cloth") || lowerText.contains("boutique") -> AiIntent.FASHION
            lowerText.contains("instagram") || lowerText.contains("facebook") || lowerText.contains("social") -> AiIntent.SOCIAL_MEDIA
            else -> AiIntent.GENERAL_CHAT
        }
    }
}
