package com.viral.ai.ui.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Dashboard : Screen("dashboard", "Dashboard", Icons.Default.Dashboard)
    object Chat : Screen("chat", "AI Chat", Icons.Default.Chat)
    object Studio : Screen("studio", "Studio", Icons.Default.Edit)
    object Analytics : Screen("analytics", "Stats", Icons.Default.Analytics)
    object Profile : Screen("profile", "Profile", Icons.Default.Person)
}
