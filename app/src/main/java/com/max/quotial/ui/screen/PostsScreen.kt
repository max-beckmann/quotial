package com.max.quotial.ui.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.max.quotial.ui.component.PostList
import com.max.quotial.ui.viewmodel.PostViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PostsScreen(
    onGroupClick: (String) -> Unit,
    postViewModel: PostViewModel = viewModel(),
) {
    val posts by postViewModel.postsLiveData.observeAsState(initial = emptyList())
    val userVotes by postViewModel.userVotesLiveData.observeAsState(initial = emptyMap())
    val activeVotePostIds by postViewModel.activeVotePostIds.observeAsState(initial = emptySet())

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
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