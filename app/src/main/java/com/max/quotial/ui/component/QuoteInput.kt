package com.max.quotial.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.max.quotial.data.model.Group

@Composable
fun QuoteInput(
    quoteContent: String,
    quoteSource: String,
    userGroups: List<Group> = emptyList(),
    onContentChange: (String) -> Unit,
    onSourceChange: (String) -> Unit,
    onGroupSelected: (Group) -> Unit,
    onSubmit: () -> Unit,
    isLoading: Boolean,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                value = quoteContent,
                onValueChange = onContentChange,
                label = { Text("Post a quote") },
                enabled = !isLoading
            )
            Button(
                onClick = onSubmit,
                enabled = !isLoading && quoteContent.isNotBlank() && quoteSource.isNotBlank(),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(8.dp)
            ) {
                Text("Send")
            }
        }

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = quoteSource,
            onValueChange = onSourceChange,
            label = { Text("Source") },
            enabled = !isLoading,
        )

        GroupSelection(userGroups, onGroupSelected)
    }
}