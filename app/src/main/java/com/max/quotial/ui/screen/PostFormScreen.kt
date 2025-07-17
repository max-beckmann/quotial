package com.max.quotial.ui.screen

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.max.quotial.ui.UiState
import com.max.quotial.ui.component.QuoteInput
import com.max.quotial.ui.viewmodel.PostFormViewModel
import com.max.quotial.ui.viewmodel.UiEvent

@Composable
fun PostFormScreen(
    onSubmit: () -> Unit,
    viewModel: PostFormViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsState()

    var quoteContent by rememberSaveable { mutableStateOf("") }
    var quoteSource by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Success -> onSubmit()
                is UiEvent.Error -> {}
            }
        }
    }

    when (val state = state) {
        is UiState.Loading -> {
            CircularProgressIndicator()
        }

        is UiState.Success -> {
            QuoteInput(
                quoteContent,
                quoteSource,
                userGroups = state.data.groups,
                onContentChange = { quoteContent = it },
                onSourceChange = { quoteSource = it },
                onGroupSelected = { viewModel.selectGroup(it) },
                onSubmit = { viewModel.submitPost(quoteContent, quoteSource) },
                isLoading = false
            )
        }

        is UiState.Error -> {}
    }
}