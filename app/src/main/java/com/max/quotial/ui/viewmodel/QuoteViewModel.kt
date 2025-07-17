package com.max.quotial.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.max.quotial.network.QuoteOfTheDay
import com.max.quotial.network.QuoteService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuoteViewModel @Inject constructor(
    private val quoteService: QuoteService
) : ViewModel() {

    var quoteOfTheDay by mutableStateOf<QuoteOfTheDay?>(null)
        private set

    fun loadQuoteOfTheDay() {
        viewModelScope.launch {
            quoteOfTheDay = quoteService.getQuoteOfTheDay().first()
        }
    }
}