package com.viral.ai.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.viral.ai.ui.components.PremiumCard
import com.viral.ai.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlatformStudioScreen(
    navController: NavController,
    title: String,
    icon: ImageVector,
    color: Color,
    tools: List<StudioToolItem>
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title, style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BackgroundDark,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        containerColor = BackgroundDark
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .background(color.copy(alpha = 0.1f), RoundedCornerShape(24.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(icon, null, tint = color, modifier = Modifier.size(40.dp))
                        Spacer(Modifier.height(8.dp))
                        Text("AI Tools for $title", color = color, style = MaterialTheme.typography.titleMedium)
                    }
                }
            }
            items(tools) { tool ->
                PremiumCard(onClick = { navController.navigate(tool.route) }) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(color.copy(alpha = 0.1f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(tool.icon, null, tint = color, modifier = Modifier.size(20.dp))
                        }
                        Spacer(Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(tool.name, color = Color.White, style = MaterialTheme.typography.titleMedium)
                            Text(tool.desc, color = TextSecondary, style = MaterialTheme.typography.bodySmall)
                        }
                        Icon(Icons.Default.ArrowForward, null, tint = TextMuted, modifier = Modifier.size(16.dp))
                    }
                }
            }
        }
    }
}

data class StudioToolItem(val name: String, val desc: String, val icon: ImageVector, val route: String)

@Composable
fun InstagramStudioScreen(navController: NavController) {
    val tools = listOf(
        StudioToolItem("Reel Script", "Viral storyboard scripts", Icons.Default.Movie, "reel_script_generator"),
        StudioToolItem("Caption Generator", "Engagement driven captions", Icons.Default.ChatBubbleOutline, "caption_generator"),
        StudioToolItem("Bio Optimizer", "Convert visitors to followers", Icons.Default.Person, "bio_generator"),
        StudioToolItem("Hashtag Tool", "Reach the right audience", Icons.Default.Tag, "hashtag_generator")
    )
    PlatformStudioScreen(navController, "Instagram", Icons.Default.CameraAlt, PrimaryPurple, tools)
}

@Composable
fun FacebookStudioScreen(navController: NavController) {
    val tools = listOf(
        StudioToolItem("FB Post", "Long-form storytelling", Icons.Default.Facebook, "fb_post_generator"),
        StudioToolItem("Community Update", "Engage your groups", Icons.Default.Groups, "fb_post_generator"),
        StudioToolItem("Ad Copy", "High ROI Meta ads", Icons.Default.AdsClick, "ad_copy_generator")
    )
    PlatformStudioScreen(navController, "Facebook", Icons.Default.Facebook, SecondaryBlue, tools)
}

@Composable
fun YouTubeStudioScreen(navController: NavController) {
    val tools = listOf(
        StudioToolItem("Video Title", "SEO & CTR focused", Icons.Default.PlayCircle, "yt_title_generator"),
        StudioToolItem("Video Description", "SEO optimized tags & info", Icons.Default.Description, "yt_desc_generator"),
        StudioToolItem("Script Generator", "Educational or entertaining", Icons.Default.Assignment, "reel_script_generator")
    )
    PlatformStudioScreen(navController, "YouTube", Icons.Default.PlayCircle, Color.Red, tools)
}

@Composable
fun WhatsAppStudioScreen(navController: NavController) {
    val tools = listOf(
        StudioToolItem("Marketing Message", "Broadcast that sells", Icons.Default.Message, "wa_marketing_generator"),
        StudioToolItem("Customer Support", "Professional responses", Icons.Default.SupportAgent, "chat"),
        StudioToolItem("Catalog Description", "Sell your products", Icons.Default.ShoppingBag, "product_desc_generator")
    )
    PlatformStudioScreen(navController, "WhatsApp", Icons.Default.Message, SuccessGreen, tools)
}
