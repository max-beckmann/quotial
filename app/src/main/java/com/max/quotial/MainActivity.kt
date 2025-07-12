package com.max.quotial

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.navigation.compose.rememberNavController
import com.max.quotial.data.repository.AuthRepository
import com.max.quotial.ui.navigation.AppNavigation
import com.max.quotial.ui.theme.QuotialTheme

class MainActivity : ComponentActivity() {
    private val authRepository = AuthRepository()

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

            QuotialTheme {
                AppNavigation(navController, authRepository)
            }
        }
    }
}