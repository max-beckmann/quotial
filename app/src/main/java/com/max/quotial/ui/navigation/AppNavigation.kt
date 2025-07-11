package com.max.quotial.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.max.quotial.ui.screen.GroupCreationScreen
import com.max.quotial.ui.screen.GroupsOverviewScreen
import com.max.quotial.ui.screen.PostsScreen
import com.max.quotial.ui.screen.ProfileScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation(navController: NavHostController) {
    var selectedIndex by rememberSaveable { mutableIntStateOf(0) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                bottomNavigationItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedIndex == index,
                        onClick = {
                            navController.navigate(item.route)
                            selectedIndex = index
                        },
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label) }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController,
            startDestination = "posts_screen",
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            composable("posts_screen") { PostsScreen() }
            composable("groups_overview_screen") {
                GroupsOverviewScreen(onCreateGroupClick = {
                    navController.navigate(
                        "group_creation_screen"
                    )
                })
            }
            composable("group_creation_screen") {
                GroupCreationScreen(onGroupCreated = {
                    navController.navigate("groups_overview_screen")
                })
            }
            composable("profile_screen") { ProfileScreen() }
        }
    }
}