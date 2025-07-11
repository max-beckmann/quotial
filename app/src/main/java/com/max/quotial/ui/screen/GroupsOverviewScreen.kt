package com.max.quotial.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.max.quotial.ui.viewmodel.GroupViewModel

@Composable
fun GroupsOverviewScreen(
    onCreateGroupClick: () -> Unit,
    groupViewModel: GroupViewModel = viewModel()
) {
    val groups by groupViewModel.groupsLiveData.observeAsState(initial = emptyList())

    Column {
        if (groups.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(groups, key = { it.id }) { group ->
                    Text(group.name)
                }
            }
        } else {
            Text("No groups found.")
        }

        Spacer(modifier = Modifier.height(8.dp))

        IconButton(onClick = onCreateGroupClick) {
            Icon(Icons.Default.AddCircle, contentDescription = "add a group")
        }
    }
}