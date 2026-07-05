package com.viral.ai.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
fun CalendarHubScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Content Planner", style = MaterialTheme.typography.titleLarge) },
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
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                SectionHeader("Manage Schedule")
                Spacer(Modifier.height(12.dp))
                CalendarActionCard("Weekly Planner", "Organize your next 7 days", Icons.Default.DateRange, PrimaryPurple) {
                    navController.navigate("calendar")
                }
            }
            item {
                CalendarActionCard("Monthly Roadmap", "Big picture content strategy", Icons.Default.CalendarMonth, SecondaryBlue) {
                    navController.navigate("calendar")
                }
            }
            item {
                SectionHeader("AI Strategy")
                Spacer(Modifier.height(12.dp))
                CalendarActionCard("Festival Planner", "Never miss a viral holiday", Icons.Default.Celebration, AccentPink) {
                    navController.navigate("tool_gen?title=Festival Planner&prefix=Generate a content strategy for upcoming Indian festivals for: &placeholder=Your niche...")
                }
            }
            item {
                CalendarActionCard("Auto Content Ideas", "AI-powered monthly plan", Icons.Default.AutoAwesome, SuccessGreen) {
                    navController.navigate("tool_gen?title=Auto Ideas&prefix=Generate a 30-day content calendar with daily post ideas for: &placeholder=Your business/niche...")
                }
            }
        }
    }
}

@Composable
fun CalendarActionCard(title: String, desc: String, icon: ImageVector, color: Color, onClick: () -> Unit) {
    PremiumCard(onClick = onClick) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(color.copy(alpha = 0.1f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, null, tint = color, modifier = Modifier.size(28.dp))
            }
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, color = Color.White, style = MaterialTheme.typography.titleMedium)
                Text(desc, color = TextSecondary, style = MaterialTheme.typography.bodySmall)
            }
            Icon(Icons.Default.ChevronRight, null, tint = TextMuted)
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelLarge,
        color = PrimaryPurple,
        fontWeight = FontWeight.Bold
    )
}
