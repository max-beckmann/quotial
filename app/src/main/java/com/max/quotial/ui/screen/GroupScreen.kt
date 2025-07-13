package com.max.quotial.ui.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.max.quotial.ui.component.PostList
import com.max.quotial.ui.viewmodel.GroupViewModel
import com.max.quotial.ui.viewmodel.PostViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun GroupScreen(
    groupId: String,
    onGroupClick: (String) -> Unit,
    groupViewModel: GroupViewModel = viewModel(),
    postViewModel: PostViewModel = viewModel()
) {
    val group by groupViewModel.getGroupById(groupId).observeAsState()
    val memberships by groupViewModel.membershipsLiveData.observeAsState(initial = emptyList())
    val isMember = memberships.contains(groupId)
    val posts by postViewModel.getPostsByGroup(groupId).observeAsState(initial = emptyList())
    val userVotes by postViewModel.userVotesLiveData.observeAsState(initial = emptyMap())
    val activeVotePostIds by postViewModel.activeVotePostIds.observeAsState(initial = emptySet())

    if (group == null) {
        Text("no group with ID $groupId could be found")
    } else {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = group!!.name,
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                text = group!!.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outline
            )

            Button(onClick = {
                if (isMember) {
                    groupViewModel.leave(groupId)
                } else {
                    groupViewModel.join(groupId)
                }
            }) {
                if (isMember) Text("leave") else Text("join")
            }

            HorizontalDivider()

            PostList(
                posts,
                userVotes,
                activeVotePostIds,
                onVote = { postId, voteType ->
                    postViewModel.vote(postId, voteType)
                },
                onGroupClick,
            )
        }
    }
}