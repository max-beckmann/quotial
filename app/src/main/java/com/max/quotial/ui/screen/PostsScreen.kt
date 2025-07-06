package com.max.quotial.ui.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.max.quotial.data.model.Quote
import com.max.quotial.ui.component.PostCard
import com.max.quotial.ui.component.QuoteInput
import com.max.quotial.ui.viewmodel.PostViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PostsScreen(viewModel: PostViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val posts by viewModel.posts.collectAsState(initial = emptyList())

    var quoteContent by rememberSaveable { mutableStateOf("") }
    var quoteSource by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            quoteContent = ""
            quoteSource = ""
            viewModel.clear()
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
            onSubmit = { viewModel.submitPost(Quote(quoteContent, quoteSource)) },
            isLoading = uiState.isLoading
        )
        Text("Posts:")
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(posts, key = { it.id }) { post ->
                PostCard(post)
            }
        }
    }
}