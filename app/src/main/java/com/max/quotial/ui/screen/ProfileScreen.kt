package com.max.quotial.ui.screen

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.max.quotial.AuthActivity

@Composable
fun ProfileScreen(user: FirebaseUser) {
    val context = LocalContext.current

    Column {
        Text(user.displayName ?: "Anonymous", style = MaterialTheme.typography.titleMedium)
        Button(onClick = {
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