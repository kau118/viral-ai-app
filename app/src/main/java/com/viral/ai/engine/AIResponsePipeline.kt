package com.viral.ai.engine

import android.util.Log

class AIResponsePipeline(
    private val contextManager: ContextManager = ContextManager(),
    private val promptBuilder: PromptBuilder = PromptBuilder(),
    private val humanizer: Humanizer = Humanizer(),
    private val responseCleaner: ResponseCleaner = ResponseCleaner(),
    private val markdownFormatter: MarkdownFormatter = MarkdownFormatter(),
    private val validator: ResponseValidator = ResponseValidator(),
    private val safetyChecker: SafetyChecker = SafetyChecker()
) {
    private val TAG = "AIResponsePipeline"

    fun preparePrompt(userInput: String, history: List<Pair<String, Boolean>>): String {
        Log.d(TAG, "Preparing prompt for: $userInput")
        val context = contextManager.buildContext(userInput)
        return promptBuilder.build(userInput, context, history)
    }

    fun processRawResponse(rawResponse: String): String {
        Log.d(TAG, "Processing raw AI response")
        
        // Phase 1: Cleaning
        var processed = responseCleaner.clean(rawResponse)
        
        // Phase 2: Humanizing
        processed = humanizer.humanize(processed)
        
        // Phase 3: Formatting
        processed = markdownFormatter.format(processed)
        
        // Phase 4: Validation
        if (!validator.isValid(processed) || !safetyChecker.isSafe(processed)) {
            Log.w(TAG, "Response failed validation or safety check")
            return "I'm sorry, I couldn't generate a safe and valid response. Please try asking differently."
        }
        
        return processed
    }
}
