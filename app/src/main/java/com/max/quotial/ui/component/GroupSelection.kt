package com.max.quotial.ui.component

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.max.quotial.data.model.Group

@Composable
fun GroupSelection(
    userGroups: List<Group> = emptyList(),
    onGroupSelected: (Group) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedGroup by remember { mutableStateOf<Group?>(null) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.TopStart)
    ) {
        Text(
            text = selectedGroup?.name ?: "General",
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true }
                .border(
                    width = 1.dp,
                    color = Color.DarkGray,
                    shape = RoundedCornerShape(4.dp)
                )
                .padding(16.dp),
        )

        DropdownMenu(
            expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("General") },
                onClick = {
                    selectedGroup = null
                }
            )

            userGroups.forEach { group ->
                DropdownMenuItem(
                    text = { Text(group.name) },
                    onClick = {
                        selectedGroup = group
                        onGroupSelected(group)
                        expanded = false
                    }
                )
            }
        }
    }
}