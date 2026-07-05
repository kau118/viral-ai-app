package com.viral.ai.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.viral.ai.ui.components.GradientButton
import com.viral.ai.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscriptionScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Go Pro", style = MaterialTheme.typography.titleLarge) },
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
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Text(
                    text = "Unlock Viral Growth", 
                    color = Color.White, 
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Black
                )
                Text(
                    text = "Get the ultimate AI toolset for digital success", 
                    color = TextSecondary, 
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            item {
                ProPlanCard()
            }

            item {
                Text(
                    text = "Why Upgrade?", 
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                FeatureRow("Unlimited AI Generations", "Never run out of ideas again")
            }
            item {
                FeatureRow("Gemini 1.5 Pro Access", "Most powerful models available")
            }
            item {
                FeatureRow("Advanced Analytics", "Deep insights into your growth")
            }
            item {
                FeatureRow("Content Calendar Pro", "Plan & schedule months in advance")
            }
        }
    }
}

@Composable
fun ProPlanCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(2.dp, Brush.linearGradient(PremiumGradient))
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Creator Pro", color = PrimaryPurple, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Surface(
                    color = PrimaryPurple, 
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        "MOST POPULAR", 
                        color = Color.White, 
                        fontSize = 10.sp, 
                        fontWeight = FontWeight.Bold, 
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.Bottom) {
                Text("₹999", color = Color.White, fontSize = 40.sp, fontWeight = FontWeight.Black)
                Text("/month", color = TextSecondary, fontSize = 16.sp, modifier = Modifier.padding(bottom = 8.dp))
            }
            Spacer(modifier = Modifier.height(24.dp))
            GradientButton(text = "Start 7-Day Free Trial", onClick = { })
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                "Cancel anytime. No questions asked.", 
                color = TextMuted, 
                fontSize = 12.sp, 
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
fun FeatureRow(title: String, desc: String) {
    Row(verticalAlignment = Alignment.Top, modifier = Modifier.fillMaxWidth()) {
        Icon(Icons.Default.Check, null, tint = SuccessGreen, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(12.dp))
        Column {
            Text(title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(desc, color = TextSecondary, fontSize = 14.sp)
        }
    }
}
