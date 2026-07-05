package com.viral.ai.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.viral.ai.repository.AiRepository
import com.viral.ai.repository.SocialRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val aiRepository: AiRepository = AiRepository(),
    private val socialRepository: SocialRepository = SocialRepository()
) : ViewModel() {
    
    private val _aiScore = MutableStateFlow(88)
    val aiScore = _aiScore.asStateFlow()

    private val _reach = MutableStateFlow("48.2k")
    val reach = _reach.asStateFlow()

    private val _engagement = MutableStateFlow("12.4k")
    val engagement = _engagement.asStateFlow()

    init {
        refreshData()
    }

    fun refreshData() {
        viewModelScope.launch {
            val analytics = socialRepository.getAnalytics()
            _reach.value = "${analytics.reach / 1000.0}k"
            _engagement.value = "${analytics.followers / 1000.0}k"
        }
    }
}
