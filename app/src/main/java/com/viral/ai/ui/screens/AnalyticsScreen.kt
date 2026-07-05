package com.viral.ai.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.viral.ai.ui.components.BarChart
import com.viral.ai.ui.components.LineChart
import com.viral.ai.ui.components.PremiumCard
import com.viral.ai.ui.theme.*
import com.viral.ai.viewmodel.AnalyticsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsScreen(navController: NavController, viewModel: AnalyticsViewModel = viewModel()) {
    val data by viewModel.analyticsData.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Performance Hub", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BackgroundDark,
                    titleContentColor = Color.White
                )
            )
        },
        containerColor = BackgroundDark
    ) { padding ->
        Crossfade(targetState = isLoading, label = "analytics_loading") { loading ->
            if (loading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = PrimaryPurple)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(padding),
                    contentPadding = PaddingValues(20.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    data?.let { analytics ->
                        item {
                            SectionTitle("AUDIENCE GROWTH")
                            Spacer(Modifier.height(12.dp))
                            PremiumCard {
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.TrendingUp, null, tint = SuccessGreen, modifier = Modifier.size(16.dp))
                                        Spacer(Modifier.width(8.dp))
                                        Text("Monthly Growth", color = TextSecondary, style = MaterialTheme.typography.labelMedium)
                                    }
                                    Text("${analytics.followers}", color = Color.White, style = MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.Black))
                                    Spacer(Modifier.height(16.dp))
                                    LineChart(
                                        data = listOf(10f, 15f, 12f, 25f, 30f, 28f, 45f),
                                        color = SecondaryBlue,
                                        modifier = Modifier.fillMaxWidth().height(120.dp)
                                    )
                                }
                            }
                        }
                        
                        item {
                            SectionTitle("ENGAGEMENT METRICS")
                            Spacer(Modifier.height(12.dp))
                            PremiumCard {
                                Column {
                                    Text("Interaction Rate", color = TextSecondary, style = MaterialTheme.typography.labelMedium)
                                    Text("${analytics.engagementRate}%", color = Color.White, style = MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.Black))
                                    Spacer(Modifier.height(16.dp))
                                    BarChart(
                                        data = listOf(5f, 8f, 4f, 7f, 9f, 6f, 10f),
                                        color = PrimaryPurple,
                                        modifier = Modifier.fillMaxWidth().height(120.dp)
                                    )
                                }
                            }
                        }
                        
                        item {
                            SectionTitle("TOP PERFORMING CONTENT")
                            Spacer(Modifier.height(12.dp))
                        }
                        
                        items(analytics.topPosts) { post ->
                            PremiumCard {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(52.dp)
                                            .background(Color.White.copy(alpha = 0.05f), RoundedCornerShape(12.dp)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = if (post.platform == "Instagram") "📸" else if (post.platform == "YouTube") "📹" else "📝",
                                            fontSize = 24.sp
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(16.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(post.platform, color = Color.White, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                                        Text("${post.likes} Likes • ${post.comments} Comments", color = TextSecondary, style = MaterialTheme.typography.bodySmall)
                                    }
                                    Surface(
                                        color = SuccessGreen.copy(alpha = 0.1f),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text(
                                            "Viral", 
                                            color = SuccessGreen, 
                                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Black, letterSpacing = 1.sp),
        color = PrimaryPurple
    )
}
