package com.max.quotial.ui.screen

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.max.quotial.AuthActivity
import com.max.quotial.ui.viewmodel.ThemeViewModel

@Composable
fun ProfileScreen(
    user: FirebaseUser,
    onSignOut: () -> Unit,
    themeViewModel: ThemeViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val isDarkModeEnabled by themeViewModel.darkModeFlow.collectAsState(initial = false)

    Column {
        Text(
            text = user.displayName ?: "Anonymous",
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text("Dark Mode")

            Switch(
                checked = isDarkModeEnabled,
                onCheckedChange = { themeViewModel.setDarkMode(it) }
            )
        }

        Button(onClick = {
            onSignOut()
            Firebase.auth.signOut()

            val intent = Intent(context, AuthActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)

            (context as? Activity)?.finish()
        }) {
            Text("Log out")
        }
    }
}