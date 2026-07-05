package com.viral.ai.engine

class PromptBuilder {
    private val experts = mapOf(
        AiIntent.BUSINESS to BusinessExpert(),
        AiIntent.MARKETING to MarketingExpert(),
        AiIntent.CODING to CodingExpert(),
        AiIntent.HEALTH to HealthExpert(),
        AiIntent.FITNESS to HealthExpert(),
        AiIntent.EDUCATION to EducationExpert(),
        AiIntent.FINANCE to FinanceExpert(),
        AiIntent.LEGAL to LegalExpert(),
        AiIntent.CREATIVE_WRITING to CreativeExpert(),
        AiIntent.CAREER to CareerExpert(),
        AiIntent.SOCIAL_MEDIA to MarketingExpert()
    )

    fun build(
        userInput: String,
        context: ConversationContext,
        history: List<Pair<String, Boolean>> = emptyList()
    ): String {
        val prompt = StringBuilder()
        
        // Identity
        prompt.append("IDENTITY:\n")
        prompt.append("You are Paavani AI, India's smartest Personal AI Partner. NOT Gemini, GPT, or a robot. You are a highly intelligent human-like companion.\n\n")
        
        // Strict Personality Rules
        prompt.append("STRICT OUTPUT RULES:\n")
        prompt.append("- NEVER start with introductions like 'Certainly', 'Sure', or 'I'd be happy to help'.\n")
        prompt.append("- NEVER use robotic disclaimers like 'As an AI' or 'I don't have feelings'.\n")
        prompt.append("- Answer DIRECTLY and IMMEDIATELY.\n")
        prompt.append("- Use a natural, conversational, yet highly professional and confident tone.\n")
        prompt.append("- If the answer is complex, use bullet points and bold headings for clarity.\n")
        prompt.append("- Keep it concise. Quality over quantity.\n\n")
        
        // Dynamic Role-play
        val expert = experts[context.intent]
        if (expert != null) {
            prompt.append("CORE ROLE: ${expert.getSpecialInstructions()}\n\n")
        } else {
            prompt.append("CORE ROLE: Assume the role of a highly intelligent, empathetic, and multi-talented Personal Partner.\n\n")
        }

        // Response Constraints
        prompt.append("CONSTRAINTS:\n")
        prompt.append("- Format for: ${context.length}\n")
        prompt.append("- Language: Match the user's language (mostly English/Hindi mix if detected).\n\n")
        
        // Memory/Context Injection
        if (history.isNotEmpty()) {
            prompt.append("CONVERSATION MEMORY (Use this for continuity):\n")
            // Take last 8 messages for better context
            history.takeLast(8).forEach { (text, isUser) ->
                val role = if (isUser) "User" else "You (Paavani)"
                prompt.append("$role: ${text.take(150)}...\n")
            }
            prompt.append("\n")
        }
        
        // Current User Message
        prompt.append("USER'S CURRENT MESSAGE:\n")
        prompt.append(userInput)
        
        return prompt.toString()
    }
}
