package com.viral.ai.model

import androidx.compose.ui.graphics.vector.ImageVector

data class AiTool(
    val id: String,
    val name: String,
    val description: String,
    val icon: ImageVector,
    val route: String,
    val category: ToolCategory,
    val promptPrefix: String,
    val placeholder: String
)

enum class ToolCategory {
    CONTENT, MARKETING, STRATEGY, IMAGE, VIDEO, BUSINESS, PRODUCTIVITY, EDUCATION, CAREER
}

enum class SocialPlatform {
    INSTAGRAM, FACEBOOK, YOUTUBE, WHATSAPP, GOOGLE, LINKEDIN, TIKTOK, TWITTER
}
