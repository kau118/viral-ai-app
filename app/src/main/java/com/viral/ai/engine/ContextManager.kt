package com.viral.ai.engine

class ContextManager(
    private val intentDetector: IntentDetector = IntentDetector(),
    private val lengthEngine: ResponseLengthEngine = ResponseLengthEngine()
) {
    fun buildContext(userInput: String): ConversationContext {
        val intent = intentDetector.detect(userInput)
        val length = lengthEngine.calculateRequiredLength(userInput)
        val tone = when (intent) {
            AiIntent.BUSINESS, AiIntent.FINANCE, AiIntent.LEGAL -> ResponseTone.PROFESSIONAL
            AiIntent.MARKETING, AiIntent.SOCIAL_MEDIA -> ResponseTone.CONFIDENT
            AiIntent.HEALTH, AiIntent.FITNESS, AiIntent.LIFESTYLE -> ResponseTone.INSPIRING
            else -> ResponseTone.NATURAL
        }
        
        return ConversationContext(
            intent = intent,
            length = length,
            tone = tone
        )
    }
}
