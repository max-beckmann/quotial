package com.max.quotial.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.google.firebase.auth.FirebaseUser
import com.max.quotial.data.repository.PostRepository
import com.max.quotial.ui.screen.GroupCreationScreen
import com.max.quotial.ui.screen.GroupScreen
import com.max.quotial.ui.screen.GroupsOverviewScreen
import com.max.quotial.ui.screen.PostsScreen
import com.max.quotial.ui.screen.ProfileScreen
import com.max.quotial.ui.screen.SubmissionScreen

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation(
    navController: NavHostController,
    user: FirebaseUser,
    postRepository: PostRepository,
) {
    var selectedIndex by rememberSaveable { mutableIntStateOf(0) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        topBar = {
            Column {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = getTitleForRoute(currentRoute),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Light
                        )
                    },
                    navigationIcon = {
                        if (currentRoute != null && currentRoute !in bottomNavigationItems.map { it.route }) {
                            IconButton(onClick = {
                                navController.popBackStack()
                            }) {
                                Icon(
                                    Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "back"
                                )
                            }
                        }
                    },
                    actions = {
                        IconButton(onClick = {
                            navController.navigate("profile_screen")
                            selectedIndex = 1000
                        }) {
                            Icon(Icons.Default.Person, contentDescription = "Profile")
                        }
                    }
                )

                HorizontalDivider(
                    thickness = 1.dp,
                    color = Color.LightGray
                )
            }
        },
        bottomBar = {
            Column {
                HorizontalDivider(
                    thickness = 1.dp,
                    color = Color.LightGray
                )

                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.background
                ) {
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
        }
    ) { innerPadding ->
        NavHost(
            navController,
            startDestination = "posts_screen",
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            composable("posts_screen") {
                PostsScreen(onGroupClick = { groupId -> navController.navigate("group_screen/$groupId") })
            }

            composable("submission_screen") {
                SubmissionScreen(onSubmit = { navController.navigate("posts_screen") })
            }

            composable("groups_overview_screen") {
                GroupsOverviewScreen(
                    onCreateGroupClick = {
                        navController.navigate(
                            "group_creation_screen"
                        )
                    },
                    onGroupClick = { groupId -> navController.navigate("group_screen/$groupId") }
                )
            }

            composable(
                "group_screen/{groupId}",
                arguments = listOf(navArgument("groupId") { type = NavType.StringType })
            ) {
                val groupId = it.arguments?.getString("groupId")

                if (groupId == null) {
                    LaunchedEffect(Unit) {
                        navController.popBackStack()
                    }

                    return@composable
                }

                GroupScreen(
                    groupId,
                    onGroupClick = { groupId -> navController.navigate("group_screen/$groupId") })
            }

            composable("group_creation_screen") {
                GroupCreationScreen(onGroupCreated = {
                    //TODO: navigate to individual group screen
                    navController.navigate("groups_overview_screen")
                })
            }

            composable("profile_screen") {
                ProfileScreen(
                    user,
                    onSignOut = {
                        postRepository.stopListening()
                    }
                )
            }
        }
    }
}

fun getTitleForRoute(route: String?): String = when {
    route == "submission_screen" -> "Post a quote"
    route == "groups_overview_screen" -> "Groups"
    route == "group_creation_screen" -> "Create your own group"
    route == "profile_screen" -> "Profile"
    else -> "quotial"
}