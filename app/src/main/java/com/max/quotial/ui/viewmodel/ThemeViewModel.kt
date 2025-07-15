package com.max.quotial.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.max.quotial.data.datastore.ThemeDatastore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ThemeViewModel(application: Application) : AndroidViewModel(application) {
    private val themeDatastore = ThemeDatastore(application)
    val darkModeFlow: Flow<Boolean> = themeDatastore.loadTheme()

    fun setDarkMode(isDarkMode: Boolean) {
        viewModelScope.launch {
            themeDatastore.saveTheme(isDarkMode)
        }
    }
}