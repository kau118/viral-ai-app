package com.viral.ai.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
fun ImagePromptScreen(navController: NavController) {
    val options = listOf(
        PromptOption("Instagram Post", "Aesthetic lifestyle shots", Icons.Default.CameraAlt, "Generate a highly detailed, professional Instagram post prompt for Midjourney about: ", PrimaryPurple),
        PromptOption("Product Shoot", "Studio product photography", Icons.Default.ShoppingBag, "Generate a commercial product photography prompt for: ", SecondaryBlue),
        PromptOption("Lifestyle", "Natural human moments", Icons.Default.Face, "Generate a realistic lifestyle photography prompt for: ", SuccessGreen),
        PromptOption("Background", "Cool textures & vibes", Icons.Default.Wallpaper, "Generate an abstract background or texture prompt for: ", AccentPink),
        PromptOption("Banner/Header", "Web & social headers", Icons.Default.ViewStream, "Generate a high-resolution social media banner prompt for: ", Color.Yellow),
        PromptOption("Logo Concept", "Minimalist branding", Icons.Default.Token, "Generate a modern logo design prompt for: ", Color.Cyan)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Visual AI Studio", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BackgroundDark,
                    titleContentColor = Color.White
                )
            )
        },
        containerColor = BackgroundDark
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            Text(
                "Create world-class visual prompts for AI image generators.", 
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )
            
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(options) { option ->
                    PromptCard(option) {
                        navController.navigate("tool_gen?title=${option.name}&prefix=${option.prefix}&placeholder=Describe your visual idea...")
                    }
                }
            }
        }
    }
}

data class PromptOption(val name: String, val desc: String, val icon: ImageVector, val prefix: String, val color: Color)

@Composable
fun PromptCard(option: PromptOption, onClick: () -> Unit) {
    PremiumCard(
        modifier = Modifier.height(160.dp),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier.size(40.dp).background(option.color.copy(alpha = 0.15f), RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(option.icon, null, tint = option.color, modifier = Modifier.size(22.dp))
            }
            Column {
                Text(
                    text = option.name, 
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = Color.White
                )
                Text(
                    text = option.desc, 
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary,
                    maxLines = 2
                )
            }
        }
    }
}
