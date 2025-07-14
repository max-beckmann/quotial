package com.max.quotial.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.max.quotial.data.model.Quote
import com.max.quotial.ui.component.QuoteInput
import com.max.quotial.ui.viewmodel.GroupViewModel
import com.max.quotial.ui.viewmodel.SubmissionViewModel

@Composable
fun SubmissionScreen(
    onSubmit: () -> Unit,
    submissionViewModel: SubmissionViewModel = viewModel(),
    groupViewModel: GroupViewModel = viewModel(),
) {
    val uiState by submissionViewModel.uiState.collectAsState()
    val userGroups by groupViewModel.groupsLiveData.observeAsState(initial = emptyList())

    var quoteContent by rememberSaveable { mutableStateOf("") }
    var quoteSource by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            quoteContent = ""
            quoteSource = ""
            submissionViewModel.clear()
        }
    }

    QuoteInput(
        quoteContent,
        quoteSource,
        userGroups,
        onContentChange = { quoteContent = it },
        onSourceChange = { quoteSource = it },
        onGroupSelected = { submissionViewModel.selectGroup(it) },
        onSubmit = {
            submissionViewModel.submitPost(Quote(quoteContent, quoteSource))
            onSubmit()
        },
        isLoading = uiState.isLoading
    )
}