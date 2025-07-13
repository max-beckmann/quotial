package com.max.quotial.ui.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import com.max.quotial.data.model.Group
import com.max.quotial.data.model.Quote
import com.max.quotial.ui.component.PostList
import com.max.quotial.ui.component.QuoteInput
import com.max.quotial.ui.viewmodel.GroupViewModel
import com.max.quotial.ui.viewmodel.PostViewModel
import com.max.quotial.ui.viewmodel.SubmissionViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PostsScreen(
    onGroupClick: (String) -> Unit,
    submissionViewModel: SubmissionViewModel = viewModel(),
    postViewModel: PostViewModel = viewModel(),
    groupViewModel: GroupViewModel = viewModel()
) {
    val uiState by submissionViewModel.uiState.collectAsState()
    val posts by postViewModel.postsLiveData.observeAsState(initial = emptyList())
    val userVotes by postViewModel.userVotesLiveData.observeAsState(initial = emptyMap())
    val activeVotePostIds by postViewModel.activeVotePostIds.observeAsState(initial = emptySet())
    val userGroups by groupViewModel.groupsLiveData.observeAsState(initial = emptyList())

    var quoteContent by rememberSaveable { mutableStateOf("") }
    var quoteSource by rememberSaveable { mutableStateOf("") }
    var selectedGroup by rememberSaveable { mutableStateOf<Group?>(null) }

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            quoteContent = ""
            quoteSource = ""
            submissionViewModel.clear()
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        QuoteInput(
            quoteContent,
            quoteSource,
            userGroups,
            onContentChange = { quoteContent = it },
            onSourceChange = { quoteSource = it },
            onGroupSelected = { selectedGroup = it },
            onSubmit = {
                submissionViewModel.submitPost(
                    Quote(quoteContent, quoteSource),
                    selectedGroup
                )
            },
            isLoading = uiState.isLoading
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text("Posts:")
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