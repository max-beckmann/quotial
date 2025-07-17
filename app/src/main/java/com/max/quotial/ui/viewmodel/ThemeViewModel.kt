package com.max.quotial.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.max.quotial.data.datastore.ThemeDatastore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(
    private val themeDatastore: ThemeDatastore
) : ViewModel() {
    val darkModeFlow: Flow<Boolean> = themeDatastore.loadTheme()

    fun setDarkMode(isDarkMode: Boolean) {
        viewModelScope.launch {
            themeDatastore.saveTheme(isDarkMode)
        }
    }
}