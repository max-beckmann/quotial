package com.max.quotial.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun QuoteInput(
    text: String,
    onTextChange: (String) -> Unit,
    onSubmit: () -> Unit,
    isLoading: Boolean,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            value = text,
            onValueChange = onTextChange,
            label = { Text("Post a quote") },
            enabled = !isLoading
        )
        Button(
            onClick = onSubmit,
            enabled = !isLoading && text.isNotBlank(),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(8.dp)
        ) {
            Text("Send")
        }
    }
}