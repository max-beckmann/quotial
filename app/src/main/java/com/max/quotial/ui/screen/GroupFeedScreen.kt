package com.max.quotial.ui.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.max.quotial.ui.component.PostList
import com.max.quotial.ui.viewmodel.GroupViewModel
import com.max.quotial.ui.viewmodel.PostViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun GroupFeedScreen(
    groupId: String,
    onGroupClick: (String) -> Unit,
    groupViewModel: GroupViewModel = hiltViewModel(),
    postViewModel: PostViewModel = hiltViewModel()
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
        Column {
            Text(
                text = group!!.name,
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = buildAnnotatedString {
                    append(group!!.memberCount.toString())
                    append(if (group!!.memberCount == 1) " member" else " members")
                },
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = group!!.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outline
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                if (isMember) {
                    groupViewModel.leave(groupId)
                } else {
                    groupViewModel.join(groupId)
                }
            }) {
                if (isMember) Text("leave") else Text("join")
            }

            Spacer(modifier = Modifier.height(8.dp))

            HorizontalDivider()

            PostList(
                posts,
                userVotes,
                activeVotePostIds,
                onVote = { postId, voteType ->
                    postViewModel.vote(postId, voteType)
                },
                onGroupClick,
                onDelete = { id ->
                    postViewModel.deletePost(id)
                }
            )
        }
    }
}