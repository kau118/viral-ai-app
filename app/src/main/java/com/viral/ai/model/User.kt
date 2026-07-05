package com.viral.ai.model

data class User(
    val uid: String = "",
    val email: String = "",
    val displayName: String = "",
    val profilePictureUrl: String = "",
    val bio: String = "",
    val instagramConnected: Boolean = false,
    val facebookConnected: Boolean = false,
    val twitterConnected: Boolean = false,
    val linkedinConnected: Boolean = false,
    val youtubeConnected: Boolean = false
)
