package com.max.quotial.ui.component

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.max.quotial.data.model.Post
import com.max.quotial.data.model.VoteType

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PostList(
    posts: List<Post>,
    userVotes: Map<String, VoteType>,
    activeVotePostIds: Set<String>,
    onVote: (String, VoteType) -> Unit,
    onGroupClick: (String) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(posts, key = { it.id }) { post ->
            PostCard(
                post,
                userVote = userVotes[post.id] ?: VoteType.NONE,
                isVoting = activeVotePostIds.contains(post.id),
                onVote = { vote -> onVote(post.id, vote) },
                onGroupClick,
            )
        }
    }
}