package com.max.quotial.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavigationItem(
    val route: String,
    val label: String,
    val icon: ImageVector,
)

val bottomNavigationItems = listOf(
    BottomNavigationItem("posts_screen", "Home", Icons.Default.Home),
    BottomNavigationItem("groups_overview_screen", "Groups", Icons.Default.Menu),
    BottomNavigationItem("profile_screen", "Profile", Icons.Default.Person),
)