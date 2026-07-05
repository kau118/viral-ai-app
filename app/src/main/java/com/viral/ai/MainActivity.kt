package com.viral.ai

import android.os.Bundle
import android.app.Application
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.viral.ai.ui.theme.*
import com.viral.ai.ui.screens.*
import com.viral.ai.viewmodel.ChatViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PaavaniAITheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val auth = Firebase.auth
    val currentUser = auth.currentUser
    
    val startDestination = if (currentUser != null) "dashboard" else "login"

    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination
            val showBottomBar = listOf("dashboard", "chat", "studio", "analytics", "profile").any { it == currentDestination?.route }
            
            if (showBottomBar) {
                NavigationBar(
                    containerColor = SurfaceDark,
                    tonalElevation = 0.dp
                ) {
                    val items = listOf(Screen.Dashboard, Screen.Chat, Screen.Studio, Screen.Analytics, Screen.Profile)
                    items.forEach { screen ->
                        val isSelected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
                        NavigationBarItem(
                            icon = { 
                                Icon(
                                    screen.icon, 
                                    contentDescription = null,
                                    tint = if (isSelected) PrimaryPurple else Color.Gray
                                ) 
                            },
                            label = { 
                                Text(
                                    screen.title,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (isSelected) PrimaryPurple else Color.Gray
                                ) 
                            },
                            selected = isSelected,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = PrimaryPurple.copy(alpha = 0.1f)
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            color = BackgroundDark
        ) {
            NavHost(navController = navController, startDestination = startDestination) {
                composable("login") { LoginScreen(navController) }
                composable("signup") { SignupScreen(navController) }
                composable("forgot_password") { ForgotPasswordScreen(navController) }
                composable("dashboard") { DashboardScreen(navController) }
                composable("chat") { 
                    ChatScreen(navController) 
                }
                composable("studio") { SocialStudioHub(navController) }
                composable("analytics") { AnalyticsScreen(navController) }
                composable("profile") { ProfileScreen(navController) }
                
                // Studio Hubs
                composable("studio_instagram") { InstagramStudioScreen(navController) }
                composable("studio_facebook") { FacebookStudioScreen(navController) }
                composable("studio_youtube") { YouTubeStudioScreen(navController) }
                composable("studio_whatsapp") { WhatsAppStudioScreen(navController) }

                composable(
                    "tool_gen?title={title}&prefix={prefix}&placeholder={placeholder}",
                    arguments = listOf(
                        navArgument("title") { defaultValue = "AI Tool" },
                        navArgument("prefix") { defaultValue = "" },
                        navArgument("placeholder") { defaultValue = "Details..." }
                    )
                ) { backStackEntry ->
                    ToolGeneratorScreen(
                        navController = navController,
                        title = backStackEntry.arguments?.getString("title") ?: "AI Tool",
                        promptPrefix = backStackEntry.arguments?.getString("prefix") ?: "",
                        placeholder = backStackEntry.arguments?.getString("placeholder") ?: "Details..."
                    )
                }

                // Dedicated routes for Studio tools
                composable("caption_generator") { ToolGeneratorScreen(navController, "Caption Generator", "Act as a viral copywriter. Generate 5 high-engagement captions for: ", "e.g. New sneaker launch") }
                composable("reel_script_generator") { ToolGeneratorScreen(navController, "Reel Script", "Act as a viral Reel producer. Write a detailed 30s script (hook, body, CTA) for: ", "e.g. Morning routine for founders") }
                composable("hashtag_generator") { ToolGeneratorScreen(navController, "Hashtag Tool", "Act as an SEO expert. Generate 30 trending hashtags categorized by volume for: ", "e.g. Vegan recipes") }
                composable("hook_generator") { ToolGeneratorScreen(navController, "Viral Hooks", "Act as a psychology-driven marketer. Write 10 scroll-stopping viral hooks for: ", "e.g. Coding tutorials") }
                composable("product_desc_generator") { ToolGeneratorScreen(navController, "Product Description", "Act as an e-commerce sales expert. Write a high-converting product description for: ", "e.g. Ergonomic chair") }
                composable("bio_generator") { ToolGeneratorScreen(navController, "Bio Optimizer", "Act as a branding expert. Write 5 catchy, conversion-focused social media bios for: ", "e.g. Digital nomad artist") }
                composable("yt_title_generator") { ToolGeneratorScreen(navController, "YouTube Title", "Act as a YouTube growth consultant. Generate 10 high CTR titles for: ", "e.g. Minecraft speedrun") }
                composable("yt_desc_generator") { ToolGeneratorScreen(navController, "YT Description", "Act as an SEO expert. Write an optimized YouTube description with keywords for: ", "e.g. iPhone 15 Pro Review") }
                composable("fb_post_generator") { ToolGeneratorScreen(navController, "Facebook Post", "Act as a community manager. Write an engaging Facebook post for: ", "e.g. Community meetup") }
                composable("wa_marketing_generator") { ToolGeneratorScreen(navController, "WhatsApp Marketing", "Act as a direct sales expert. Write a professional WhatsApp marketing message for: ", "e.g. Flash sale on clothing") }
                composable("google_post_generator") { ToolGeneratorScreen(navController, "Google Business Post", "Act as a local business consultant. Write a Google Business update for: ", "e.g. Weekend special menu") }
                composable("ad_copy_generator") { ToolGeneratorScreen(navController, "Ad Copy", "Act as a paid ads specialist. Write high-ROI ad copy for: ", "e.g. Online course for AI") }
                composable("carousel_generator") { ToolGeneratorScreen(navController, "Carousel Generator", "Act as a content strategist. Create a 7-slide viral carousel plan for: ", "e.g. 5 steps to financial freedom") }
                composable("poster_generator") { ToolGeneratorScreen(navController, "Poster Generator", "Act as a creative director. Generate a visual layout and text overlay plan for a poster about: ", "e.g. Grand opening event") }
                composable("product_shoot_generator") { ToolGeneratorScreen(navController, "Product Shoot", "Act as a commercial photographer. Generate 5 creative product shoot concepts for: ", "e.g. Minimalist watch") }
                
                // New Studio routes
                composable("linkedin_post_generator") { ToolGeneratorScreen(navController, "LinkedIn Post", "Act as a thought leader on LinkedIn. Write an insightful, professional post about: ", "e.g. Future of work") }
                composable("blog_generator") { ToolGeneratorScreen(navController, "Blog Writer", "Act as an SEO expert and content writer. Create a detailed blog outline and intro for: ", "e.g. Plant-based benefits") }
                composable("sales_expert") { ToolGeneratorScreen(navController, "Sales Closer", "Act as a high-ticket sales closer. Provide a persuasive response or script for: ", "e.g. Price objection") }
                composable("business_advisor") { ToolGeneratorScreen(navController, "Business Advisor", "Act as a visionary CEO and consultant. Provide a strategic roadmap and advice for: ", "e.g. Scaling startup") }
                composable("resume_builder") { ToolGeneratorScreen(navController, "Resume Builder", "Act as an executive recruiter. Improve this resume section or write a high-impact summary for: ", "e.g. Product Manager summary") }
                composable("email_writer") { ToolGeneratorScreen(navController, "Email Writer", "Act as a professional communications expert. Write a clear, effective email for: ", "e.g. Partner request") }
                composable("daily_planner") { ToolGeneratorScreen(navController, "Daily Planner", "Act as a high-performance productivity coach. Create an optimized daily schedule for: ", "e.g. Founder daily routine") }
                composable("grammar_fixer") { ToolGeneratorScreen(navController, "Grammar Fix", "Act as a meticulous editor. Fix the grammar and improve the flow of this text: ", "e.g. Paste draft...") }
                composable("summarizer") { ToolGeneratorScreen(navController, "Summarizer", "Act as an information architect. Provide a concise executive summary of this text: ", "e.g. Paste long text...") }
                composable("study_assistant") { ToolGeneratorScreen(navController, "Study Assistant", "Act as an elite tutor. Break down and explain this topic simply with key concepts: ", "e.g. Quantum physics") }
                composable("coding_assistant") { ToolGeneratorScreen(navController, "Coding Assistant", "Act as a Senior Software Engineer. Debug, optimize, or explain this code: ", "e.g. Coroutine crash fix") }

                composable("calendar") { CalendarScreen(navController) }
                composable("calendar_hub") { CalendarHubScreen(navController) }
                composable("trending") { TrendingScreen(navController) }
                composable("settings") { SettingsScreen(navController) }
                composable("subscription") { SubscriptionScreen(navController) }
                composable("image_ai") { ImagePromptScreen(navController) }
            }
        }
    }
}
