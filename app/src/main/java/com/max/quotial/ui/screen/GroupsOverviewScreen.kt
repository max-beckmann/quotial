package com.max.quotial.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.max.quotial.ui.component.GroupCard
import com.max.quotial.ui.viewmodel.GroupViewModel

@Composable
fun GroupsOverviewScreen(
    onCreateGroupClick: () -> Unit,
    onGroupClick: (String) -> Unit,
    groupViewModel: GroupViewModel = hiltViewModel()
) {
    val groups by groupViewModel.groupsLiveData.observeAsState(initial = emptyList())
    val memberships by groupViewModel.membershipsLiveData.observeAsState(initial = emptyList())

    Column {
        if (groups.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(groups, key = { it.id }) { group ->
                    GroupCard(
                        group,
                        isMember = memberships.contains(group.id),
                        onGroupClick,
                        onGroupJoin = { groupId -> groupViewModel.join(groupId) }
                    )
                }
            }
        } else {
            Text("No groups found.")
        }

        HorizontalDivider()

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(
            onClick = onCreateGroupClick,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(Icons.Default.AddCircle, contentDescription = "add a group")
                Text("create a new group")
            }
        }
    }
}