package com.max.quotial

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.google.firebase.auth.FirebaseAuth
import com.max.quotial.ui.screen.PostsScreen
import com.max.quotial.ui.theme.QuotialTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
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