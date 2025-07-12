package com.max.quotial.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.max.quotial.data.model.Group
import com.max.quotial.data.model.Quote
import com.max.quotial.data.repository.AuthRepository
import com.max.quotial.data.repository.PostRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SubmissionViewModel : ViewModel() {
    private val postRepository = PostRepository()
    private val authRepository = AuthRepository()

    private val _uiState = MutableStateFlow(PostUiState())
    val uiState: StateFlow<PostUiState> = _uiState.asStateFlow()

    fun submitPost(quote: Quote, group: Group?) {
        if (quote.content.isBlank() || quote.source.isBlank()) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null,
            )

            val user = authRepository.getUser()
            postRepository.createPost(quote, user, group).fold(
                onSuccess = {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isSuccess = true,
                    )
                },
                onFailure = { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message,
                    )
                },
            )
        }
    }

    fun clear() {
        _uiState.value = _uiState.value.copy(
            isSuccess = false,
        )
    }
}

data class PostUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null,
)