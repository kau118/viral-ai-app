package com.viral.ai.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.viral.ai.model.AnalyticsData
import com.viral.ai.model.SocialPost
import com.viral.ai.network.ApiService
import kotlinx.coroutines.tasks.await
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

class SocialRepository {
    private val apiService: ApiService by lazy {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        val gson = GsonBuilder()
            .setLenient()
            .create()

        Retrofit.Builder()
            .baseUrl("https://paavani-ai-backend.onrender.com/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService::class.java)
    }

    suspend fun getAnalytics(): AnalyticsData {
        return try {
            val user = FirebaseAuth.getInstance().currentUser
            val token = user?.getIdToken(false)?.await()?.token 
                ?: return fallbackData()
            
            apiService.getAnalytics("Bearer $token")
        } catch (e: Exception) {
            Log.e("PaavaniAI_Social", "Failed to fetch analytics", e)
            fallbackData()
        }
    }

    private fun fallbackData(): AnalyticsData {
        return AnalyticsData(
            followers = 48200,
            reach = 124000,
            engagementRate = 5.2,
            impressions = 890000,
            topPosts = listOf(
                SocialPost("1", "Instagram", "", 1200, 45, ""),
                SocialPost("2", "Facebook", "", 800, 32, ""),
                SocialPost("3", "YouTube", "", 5400, 112, "")
            )
        )
    }
}
