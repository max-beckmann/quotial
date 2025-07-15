package com.max.quotial.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.max.quotial.network.QuoteOfTheDay
import com.max.quotial.network.QuoteService
import com.max.quotial.network.RetrofitHelper
import kotlinx.coroutines.launch

class QuoteViewModel : ViewModel() {
    val quoteService = RetrofitHelper.retrofit.create(QuoteService::class.java)

    var quoteOfTheDay by mutableStateOf<QuoteOfTheDay?>(null)
        private set

    fun loadQuoteOfTheDay() {
        viewModelScope.launch {
            quoteOfTheDay = quoteService.getQuoteOfTheDay().first()
        }
    }
}