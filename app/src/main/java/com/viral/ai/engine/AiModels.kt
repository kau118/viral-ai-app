package com.viral.ai.engine

enum class AiIntent {
    BUSINESS, MARKETING, HEALTH, FITNESS, EDUCATION, CODING, 
    TRAVEL, SHOPPING, FINANCE, LEGAL, ENTERTAINMENT, 
    IMAGE_PROMPT, VIDEO_PROMPT, GENERAL_CHAT, CREATIVE_WRITING, 
    CAREER, LIFESTYLE, FASHION, SOCIAL_MEDIA
}

enum class ResponseLength {
    TINY, SHORT, MEDIUM, DETAILED
}

enum class ResponseTone {
    PROFESSIONAL, FRIENDLY, CONFIDENT, NATURAL, INSPIRING
}

data class ConversationContext(
    val intent: AiIntent = AiIntent.GENERAL_CHAT,
    val length: ResponseLength = ResponseLength.MEDIUM,
    val tone: ResponseTone = ResponseTone.NATURAL,
    val topic: String? = null,
    val userPreferences: Map<String, String> = emptyMap()
)
