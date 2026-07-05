package com.viral.ai.model

data class AnalyticsData(
    val followers: Int,
    val reach: Int,
    val engagementRate: Double,
    val impressions: Int,
    val topPosts: List<SocialPost>
)

data class SocialPost(
    val id: String,
    val platform: String,
    val imageUrl: String,
    val likes: Int,
    val comments: Int,
    val shareUrl: String
)
