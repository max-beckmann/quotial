package com.max.quotial

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.compose.rememberNavController
import com.max.quotial.data.repository.AuthRepository
import com.max.quotial.data.repository.PostRepository
import com.max.quotial.ui.navigation.AppNavigation
import com.max.quotial.ui.theme.QuotialTheme
import com.max.quotial.ui.viewmodel.ThemeViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var authRepository: AuthRepository

    @Inject
    lateinit var postRepository: PostRepository

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!authRepository.isUserLoggedIn()) {
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
        }

        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val themeViewModel: ThemeViewModel by viewModels()
            val darkTheme by themeViewModel.darkModeFlow.collectAsState(initial = false)

            QuotialTheme(darkTheme) {
                AppNavigation(navController, authRepository.getUser(), postRepository)
            }
        }
    }
}