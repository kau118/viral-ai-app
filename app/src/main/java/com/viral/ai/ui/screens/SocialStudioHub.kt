package com.viral.ai.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.viral.ai.ui.components.PremiumCard
import com.viral.ai.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SocialStudioHub(navController: NavController) {
    val platforms = listOf(
        StudioPlatform("Instagram", "Reels, Captions & Bio", Icons.Default.CameraAlt, PrimaryPurple, "studio_instagram"),
        StudioPlatform("Facebook", "Posts & Community", Icons.Default.Facebook, SecondaryBlue, "studio_facebook"),
        StudioPlatform("YouTube", "Titles, Tags & Scripts", Icons.Default.PlayCircle, Color.Red, "studio_youtube"),
        StudioPlatform("WhatsApp Business", "Marketing Messages", Icons.Default.Message, SuccessGreen, "studio_whatsapp")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Social Studio", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BackgroundDark,
                    titleContentColor = Color.White
                )
            )
        },
        containerColor = BackgroundDark
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                Text(
                    "Select a platform specialized tools.", 
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
            }
            items(platforms) { platform ->
                PlatformCard(platform) {
                    navController.navigate(platform.route)
                }
            }
            
            item {
                Spacer(Modifier.height(16.dp))
                Text(
                    "GENERAL AI TOOLS", 
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Black, letterSpacing = 1.sp),
                    color = PrimaryPurple
                )
                Spacer(Modifier.height(12.dp))
                PremiumCard(onClick = { navController.navigate("hashtag_generator") }) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier.size(40.dp).background(SecondaryBlue.copy(alpha = 0.1f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Tag, null, tint = SecondaryBlue)
                        }
                        Spacer(Modifier.width(16.dp))
                        Text("Trending Hashtag Finder", color = Color.White, style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.weight(1f))
                        Icon(Icons.Default.ArrowForward, null, tint = TextMuted, modifier = Modifier.size(16.dp))
                    }
                }
            }
        }
    }
}

data class StudioPlatform(val name: String, val desc: String, val icon: ImageVector, val color: Color, val route: String)

@Composable
fun PlatformCard(platform: StudioPlatform, onClick: () -> Unit) {
    PremiumCard(onClick = onClick) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(platform.color.copy(alpha = 0.15f), RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(platform.icon, null, tint = platform.color, modifier = Modifier.size(32.dp))
            }
            Spacer(Modifier.width(20.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(platform.name, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = Color.White)
                Text(platform.desc, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
            }
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(Color.White.copy(alpha = 0.05f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.ChevronRight, null, tint = TextMuted, modifier = Modifier.size(18.dp))
            }
        }
    }
}
