package com.max.quotial

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.max.quotial.data.repository.AuthRepository
import com.max.quotial.ui.screen.PostsScreen
import com.max.quotial.ui.theme.QuotialTheme

class MainActivity : ComponentActivity() {
    private val authRepository = AuthRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!authRepository.isUserLoggedIn()) {
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
        }

        enableEdgeToEdge()
        setContent {
            QuotialTheme {
                PostsScreen()
            }
        }
    }
}