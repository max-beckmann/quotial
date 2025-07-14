package com.max.quotial.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxHeight()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = quoteContent,
                onValueChange = onContentChange,
                label = { Text("Post a quote") },
                enabled = !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.LightGray
                )
            )

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = quoteSource,
                onValueChange = onSourceChange,
                label = { Text("Source") },
                enabled = !isLoading,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.LightGray
                )
            )

            GroupSelection(userGroups, onGroupSelected)
        }

        Button(
            onClick = onSubmit,
            enabled = !isLoading && quoteContent.isNotBlank() && quoteSource.isNotBlank(),
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Post quote")
        }
    }
}