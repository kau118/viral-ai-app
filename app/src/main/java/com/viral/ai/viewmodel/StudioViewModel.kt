package com.viral.ai.viewmodel

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.viral.ai.model.AiTool
import com.viral.ai.model.ToolCategory
import com.viral.ai.repository.AiRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class StudioViewModel(private val repository: AiRepository = AiRepository()) : ViewModel() {
    private val _uiState = MutableStateFlow<StudioUiState>(StudioUiState.Idle)
    val uiState = _uiState.asStateFlow()

    private val _tools = MutableStateFlow<List<AiTool>>(emptyList())
    val tools = _tools.asStateFlow()

    init {
        loadTools()
    }

    private fun loadTools() {
        _tools.value = listOf(
            // --- CONTENT & SOCIAL ---
            AiTool("cap_gen", "Caption Generator", "Create viral captions for any post", Icons.Default.ChatBubbleOutline, "caption_generator", ToolCategory.CONTENT, "Act as a viral copywriter. Generate 5 high-engagement captions for: ", "e.g. New luxury watch launch"),
            AiTool("reel_scr", "Reel Script", "Plan high-retention reel scripts", Icons.Default.Movie, "reel_script_generator", ToolCategory.VIDEO, "Act as a viral Reel producer. Write a detailed 30s script (hook, body, CTA) for: ", "e.g. A day in life of a developer"),
            AiTool("li_post", "LinkedIn Post", "Professional authority building posts", Icons.Default.Work, "linkedin_post_generator", ToolCategory.CONTENT, "Act as a thought leader on LinkedIn. Write an insightful, professional post about: ", "e.g. The future of remote work"),
            AiTool("yt_title", "YouTube Title", "High CTR titles for videos", Icons.Default.PlayCircle, "yt_title_generator", ToolCategory.VIDEO, "Act as a YouTube growth consultant. Generate 10 high CTR titles for: ", "e.g. Minecraft hardcore survival"),
            AiTool("blog_gen", "Blog Writer", "SEO optimized blog outlines & content", Icons.Default.Article, "blog_generator", ToolCategory.CONTENT, "Act as an SEO expert and content writer. Create a detailed blog outline and intro for: ", "e.g. Benefits of a plant-based diet"),
            
            // --- MARKETING & SALES ---
            AiTool("hash_tool", "Hashtag Tool", "Find trending & niche tags", Icons.Default.Tag, "hashtag_generator", ToolCategory.MARKETING, "Act as an SEO expert. Generate 30 trending hashtags categorized by volume for: ", "e.g. Vegan bodybuilding"),
            AiTool("hook_gen", "Viral Hooks", "10 scroll-stopping hooks", Icons.Default.AutoAwesome, "hook_generator", ToolCategory.CONTENT, "Act as a psychology-driven marketer. Write 10 scroll-stopping viral hooks for: ", "e.g. How to save money"),
            AiTool("ad_copy", "Ad Copy", "High conversion paid ads", Icons.Default.AdsClick, "ad_copy_generator", ToolCategory.MARKETING, "Act as a paid ads specialist. Write high-ROI ad copy for: ", "e.g. Online coding bootcamp"),
            AiTool("sales_exp", "Sales Closer", "Persuasive sales scripts & replies", Icons.Default.Sell, "sales_expert", ToolCategory.MARKETING, "Act as a high-ticket sales closer. Provide a persuasive response or script for: ", "e.g. Handling price objections for coaching"),
            
            // --- BUSINESS & CAREER ---
            AiTool("prod_desc", "Product Description", "Sell effectively with AI", Icons.Default.ShoppingBag, "product_desc_generator", ToolCategory.BUSINESS, "Act as an e-commerce sales expert. Write a high-converting product description for: ", "e.g. Ergonomic desk lamp"),
            AiTool("biz_adv", "Business Advisor", "Strategic advice for startups", Icons.Default.Psychology, "business_advisor", ToolCategory.BUSINESS, "Act as a visionary CEO and consultant. Provide a strategic roadmap and advice for: ", "e.g. Scaling a SaaS to 10k users"),
            AiTool("resume_b", "Resume Builder", "Optimize your professional profile", Icons.Default.Badge, "resume_builder", ToolCategory.CAREER, "Act as an executive recruiter. Improve this resume section or write a high-impact summary for: ", "e.g. Senior Product Manager at a fintech"),
            AiTool("email_w", "Email Writer", "Professional & cold outreach", Icons.Default.Email, "email_writer", ToolCategory.BUSINESS, "Act as a professional communications expert. Write a clear, effective email for: ", "e.g. Requesting a partnership with a brand"),
            
            // --- PRODUCTIVITY & TOOLS ---
            AiTool("day_p", "Daily Planner", "Organize your life & tasks", Icons.Default.CalendarToday, "daily_planner", ToolCategory.PRODUCTIVITY, "Act as a high-performance productivity coach. Create an optimized daily schedule for: ", "e.g. Working professional with a side hustle"),
            AiTool("gram_fix", "Grammar Fix", "Perfect your English writing", Icons.Default.Spellcheck, "grammar_fixer", ToolCategory.CONTENT, "Act as a meticulous editor. Fix the grammar and improve the flow of this text: ", "e.g. Paste your draft here..."),
            AiTool("sum_pdf", "Summarizer", "Distill info from long texts", Icons.Default.Summarize, "summarizer", ToolCategory.PRODUCTIVITY, "Act as an information architect. Provide a concise executive summary of this text: ", "e.g. Paste a long article or document text..."),
            
            // --- EDUCATION & GROWTH ---
            AiTool("study_a", "Study Assistant", "Learn anything faster", Icons.Default.School, "study_assistant", ToolCategory.EDUCATION, "Act as an elite tutor. Break down and explain this topic simply with key concepts: ", "e.g. Quantum entanglement for beginners"),
            AiTool("code_a", "Coding Assistant", "Debug and optimize code", Icons.Default.Code, "coding_assistant", ToolCategory.EDUCATION, "Act as a Senior Software Engineer. Debug, optimize, or explain this code: ", "e.g. Kotlin coroutine leak analysis"),
            
            // --- IMAGE & VISUALS ---
            AiTool("prod_shoot", "Product Shoot", "AI Photo shoot concepts", Icons.Default.Camera, "product_shoot_generator", ToolCategory.IMAGE, "Act as a commercial photographer. Generate 5 creative product shoot concepts for: ", "e.g. Organic skincare bottle"),
            AiTool("post_gen", "Poster Generator", "Visual layout ideas for posts", Icons.Default.Style, "poster_generator", ToolCategory.IMAGE, "Act as a creative director. Generate a visual layout and text overlay plan for a poster about: ", "e.g. Summer sale event")
        )
    }

    fun executeTool(prompt: String) {
        viewModelScope.launch {
            _uiState.value = StudioUiState.Loading
            val result = withContext(Dispatchers.IO) { repository.generateContent(prompt) }
            result.onSuccess {
                _uiState.value = StudioUiState.Success(it)
            }.onFailure {
                _uiState.value = StudioUiState.Error(it.message ?: "An error occurred")
            }
        }
    }

    fun resetState() {
        _uiState.value = StudioUiState.Idle
    }
}

sealed class StudioUiState {
    object Idle : StudioUiState()
    object Loading : StudioUiState()
    data class Success(val result: String) : StudioUiState()
    data class Error(val message: String) : StudioUiState()
}
