package com.viral.ai.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.viral.ai.model.AnalyticsData
import com.viral.ai.repository.SocialRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AnalyticsViewModel(private val repository: SocialRepository = SocialRepository()) : ViewModel() {
    private val _analyticsData = MutableStateFlow<AnalyticsData?>(null)
    val analyticsData = _analyticsData.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    init {
        fetchAnalytics()
    }

    fun fetchAnalytics() {
        viewModelScope.launch {
            _isLoading.value = true
            _analyticsData.value = repository.getAnalytics()
            _isLoading.value = false
        }
    }
}
