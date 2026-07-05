package com.viral.ai.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.viral.ai.ui.components.*
import com.viral.ai.ui.theme.*
import com.viral.ai.viewmodel.DashboardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(navController: NavController, viewModel: DashboardViewModel = viewModel()) {
    val aiScore by viewModel.aiScore.collectAsState()
    
    var isLoading by remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(800)
        isLoading = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Paavani AI", 
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp
                        )
                    ) 
                },
                actions = {
                    IconButton(onClick = { navController.navigate("profile") }) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Brush.linearGradient(PremiumGradient)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Person, null, tint = Color.White, modifier = Modifier.size(20.dp))
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BackgroundDark,
                    titleContentColor = Color.White
                )
            )
        },
        containerColor = BackgroundDark
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(28.dp)
        ) {
            if (isLoading) {
                item { DashboardSkeleton() }
            } else {
                item {
                    WelcomeHeader()
                }
                
                item {
                    StatusCard(aiScore)
                }

                item {
                    SectionTitle("AI SUITE", "Explore your capabilities")
                    Spacer(Modifier.height(16.dp))
                    SuiteGrid(navController)
                }

                item {
                    SectionTitle("QUICK INTELLIGENCE", "One-tap solutions")
                    Spacer(Modifier.height(16.dp))
                    QuickActionsRow(navController)
                }

                item {
                    DashboardPremiumActionCard(
                        title = "Universal Consult",
                        subtitle = "Chat with your advanced AI partner",
                        onClick = { navController.navigate("chat") }
                    )
                }
                
                item { Spacer(Modifier.height(40.dp)) }
            }
        }
    }
}

@Composable
fun DashboardPremiumActionCard(title: String, subtitle: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(Brush.linearGradient(PremiumGradient))
            .clickable { onClick() }
            .padding(24.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Color.White)
                Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = Color.White.copy(alpha = 0.8f))
            }
            Box(
                modifier = Modifier.size(48.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.ArrowForward, null, tint = Color.White)
            }
        }
    }
}

@Composable
fun WelcomeHeader() {
    Column {
        Text(
            text = "Good morning,",
            style = MaterialTheme.typography.bodyLarge,
            color = TextSecondary
        )
        Text(
            text = "Strategic Partner",
            style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Black),
            color = Color.White
        )
    }
}

@Composable
fun StatusCard(score: Int) {
    GlassmorphicCard(modifier = Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(60.dp).clip(CircleShape).background(PrimaryPurple.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    progress = { score / 100f },
                    modifier = Modifier.size(50.dp),
                    color = PrimaryPurple,
                    trackColor = Color.White.copy(alpha = 0.1f),
                    strokeWidth = 4.dp
                )
                Text("$score", fontWeight = FontWeight.Bold, color = PrimaryPurple, fontSize = 14.sp)
            }
            Spacer(Modifier.width(20.dp))
            Column {
                Text("Intelligence Level", style = MaterialTheme.typography.titleMedium, color = Color.White)
                Text("Your AI adaptability is peaking", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
            }
        }
    }
}

@Composable
fun SuiteGrid(navController: NavController) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        SuiteCard(
            title = "Studio",
            icon = Icons.Default.AutoAwesome,
            gradient = PremiumGradient,
            modifier = Modifier.weight(1f)
        ) { navController.navigate("studio") }
        
        SuiteCard(
            title = "Visuals",
            icon = Icons.Default.Image,
            gradient = BlueGradient,
            modifier = Modifier.weight(1f)
        ) { navController.navigate("image_ai") }
    }
    Spacer(Modifier.height(16.dp))
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        SuiteCard(
            title = "Calendar",
            icon = Icons.Default.CalendarMonth,
            gradient = GreenGradient,
            modifier = Modifier.weight(1f)
        ) { navController.navigate("calendar_hub") }
        
        SuiteCard(
            title = "Analysis",
            icon = Icons.Default.Analytics,
            gradient = GoldGradient,
            modifier = Modifier.weight(1f)
        ) { navController.navigate("analytics") }
    }
}

@Composable
fun SuiteCard(title: String, icon: ImageVector, gradient: List<Color>, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .height(120.dp)
            .clip(RoundedCornerShape(28.dp))
            .background(CardBackground)
            .clickable { onClick() }
            .border(1.dp, GlassBorder, RoundedCornerShape(28.dp))
    ) {
        Column(modifier = Modifier.padding(16.dp).fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
            Box(
                modifier = Modifier.size(40.dp).clip(RoundedCornerShape(12.dp)).background(Brush.linearGradient(gradient)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, null, tint = Color.White, modifier = Modifier.size(20.dp))
            }
            Text(title, color = Color.White, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
        }
    }
}

@Composable
fun QuickActionsRow(navController: NavController) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        item { QuickActionItem("Reel Script", Icons.Default.Movie, PrimaryPurple) { navController.navigate("reel_script_generator") } }
        item { QuickActionItem("Daily Plan", Icons.Default.Today, SuccessGreen) { navController.navigate("daily_planner") } }
        item { QuickActionItem("Resume", Icons.Default.Badge, SecondaryBlue) { navController.navigate("resume_builder") } }
    }
}

@Composable
fun QuickActionItem(title: String, icon: ImageVector, color: Color, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable { onClick() }) {
        Box(
            modifier = Modifier.size(64.dp).clip(RoundedCornerShape(20.dp)).background(color.copy(alpha = 0.1f)).border(1.dp, color.copy(alpha = 0.2f), RoundedCornerShape(20.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, null, tint = color, modifier = Modifier.size(28.dp))
        }
        Spacer(Modifier.height(8.dp))
        Text(title, color = TextSecondary, style = MaterialTheme.typography.labelMedium)
    }
}

@Composable
fun SectionTitle(title: String, subtitle: String) {
    Column {
        Text(title, style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Black, letterSpacing = 2.sp), color = PrimaryPurple)
        Text(subtitle, style = MaterialTheme.typography.bodySmall, color = TextMuted)
    }
}

@Composable
fun DashboardSkeleton() {
    Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
        ShimmerEffect(Modifier.width(150.dp).height(24.dp))
        ShimmerEffect(Modifier.fillMaxWidth().height(100.dp), shape = RoundedCornerShape(28.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            ShimmerEffect(Modifier.weight(1f).height(120.dp), shape = RoundedCornerShape(28.dp))
            ShimmerEffect(Modifier.weight(1f).height(120.dp), shape = RoundedCornerShape(28.dp))
        }
        ShimmerEffect(Modifier.fillMaxWidth().height(80.dp), shape = RoundedCornerShape(28.dp))
    }
}
