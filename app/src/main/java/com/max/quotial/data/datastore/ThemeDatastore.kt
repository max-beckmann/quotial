package com.max.quotial.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.max.quotial.dataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ThemeDatastore(private val context: Context) {
    private val dataStore = context.dataStore
    private val darkModeKey = booleanPreferencesKey("dark_mode")

    fun loadTheme(): Flow<Boolean> = dataStore.data.map { it[darkModeKey] ?: false }

    suspend fun saveTheme(isDarkMode: Boolean) {
        dataStore.edit { settings ->
            settings[darkModeKey] = isDarkMode
        }
    }
}