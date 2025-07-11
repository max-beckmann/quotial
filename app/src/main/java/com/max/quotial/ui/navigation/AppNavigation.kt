package com.max.quotial.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.max.quotial.ui.screen.PostsScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation(navController: NavHostController) {
    Scaffold { innerPadding ->
        NavHost(
            navController,
            startDestination = "posts_screen",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("posts_screen") {
                PostsScreen()
            }
        }
    }
}