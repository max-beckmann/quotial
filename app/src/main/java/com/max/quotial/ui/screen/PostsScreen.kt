package com.max.quotial.ui.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.max.quotial.data.model.Quote
import com.max.quotial.data.model.VoteType
import com.max.quotial.ui.component.PostCard
import com.max.quotial.ui.component.QuoteInput
import com.max.quotial.ui.viewmodel.PostViewModel
import com.max.quotial.ui.viewmodel.SubmissionViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PostsScreen(
    submissionViewModel: SubmissionViewModel = viewModel(),
    postViewModel: PostViewModel = viewModel()
) {
    val uiState by submissionViewModel.uiState.collectAsState()
    val posts by postViewModel.postsLiveData.observeAsState(initial = emptyList())
    val userVotes by postViewModel.userVotesLiveData.observeAsState(initial = emptyMap())
    val activeVotePostIds by postViewModel.activeVotePostIds.observeAsState(initial = emptySet())

    var quoteContent by rememberSaveable { mutableStateOf("") }
    var quoteSource by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            quoteContent = ""
            quoteSource = ""
            submissionViewModel.clear()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        QuoteInput(
            quoteContent,
            quoteSource,
            onContentChange = { quoteContent = it },
            onSourceChange = { quoteSource = it },
            onSubmit = { submissionViewModel.submitPost(Quote(quoteContent, quoteSource)) },
            isLoading = uiState.isLoading
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text("Posts:")
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(posts, key = { it.id }) { post ->
                PostCard(
                    post,
                    userVote = userVotes[post.id] ?: VoteType.NONE,
                    isVoting = activeVotePostIds.contains(post.id),
                    onVote = { vote ->
                        postViewModel.vote(post.id, vote)
                    })
            }
        }
    }
}