package com.viral.ai.network

import com.viral.ai.model.AnalyticsData
import retrofit2.http.GET
import retrofit2.http.Header

interface ApiService {
    @GET("api/analytics")
    suspend fun getAnalytics(
        @Header("Authorization") token: String
    ): AnalyticsData

    @GET("api/profile")
    suspend fun getUserProfile(
        @Header("Authorization") token: String
    ): com.viral.ai.model.User
}
