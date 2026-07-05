package com.viral.ai.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.viral.ai.model.AiTool
import com.viral.ai.model.ToolCategory
import com.viral.ai.ui.components.PremiumCard
import com.viral.ai.ui.components.ShimmerEffect
import com.viral.ai.ui.theme.*
import com.viral.ai.viewmodel.StudioViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudioScreen(navController: NavController, viewModel: StudioViewModel = viewModel()) {
    var selectedCategory by remember { mutableStateOf("All") }
    var isLoading by remember { mutableStateOf(true) }
    
    val categories = remember {
        listOf("All") + ToolCategory.values().map { 
            it.name.lowercase(Locale.ROOT).replaceFirstChar { char -> 
                if (char.isLowerCase()) char.titlecase(Locale.ROOT) else char.toString() 
            } 
        }
    }
    val tools by viewModel.tools.collectAsState()

    val filteredTools = remember(selectedCategory, tools) {
        if (selectedCategory == "All") {
            tools
        } else {
            tools.filter { it.category.name.equals(selectedCategory, ignoreCase = true) }
        }
    }

    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(600)
        isLoading = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AI Studio", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BackgroundDark,
                    titleContentColor = Color.White
                )
            )
        },
        containerColor = BackgroundDark
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            ScrollableTabRow(
                selectedTabIndex = categories.indexOf(selectedCategory),
                containerColor = Color.Transparent,
                contentColor = PrimaryPurple,
                edgePadding = 16.dp,
                divider = {},
                indicator = { tabPositions ->
                    val index = categories.indexOf(selectedCategory)
                    if (index != -1 && index < tabPositions.size) {
                        TabRowDefaults.SecondaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[index]),
                            color = PrimaryPurple
                        )
                    }
                }
            ) {
                categories.forEach { category ->
                    Tab(
                        selected = selectedCategory == category,
                        onClick = { selectedCategory = category },
                        text = { 
                            Text(
                                text = category, 
                                style = MaterialTheme.typography.bodyMedium,
                                color = if (selectedCategory == category) PrimaryPurple else TextSecondary
                            ) 
                        }
                    )
                }
            }

            Crossfade(targetState = isLoading, label = "studio_loading") { loading ->
                if (loading) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(8) {
                            ShimmerEffect(Modifier.fillMaxWidth().height(160.dp), shape = RoundedCornerShape(24.dp))
                        }
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(filteredTools) { tool ->
                            ToolCard(tool) {
                                navController.navigate(tool.route)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ToolCard(tool: AiTool, onClick: () -> Unit) {
    PremiumCard(
        modifier = Modifier.height(160.dp),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(PrimaryPurple.copy(alpha = 0.1f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(tool.icon, null, tint = PrimaryPurple, modifier = Modifier.size(24.dp))
            }
            Column {
                Text(
                    text = tool.name, 
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = Color.White
                )
                Text(
                    text = tool.description, 
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary,
                    maxLines = 2
                )
            }
        }
    }
}
