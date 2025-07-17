package com.max.quotial.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.max.quotial.data.model.Group
import com.max.quotial.data.model.Quote
import com.max.quotial.data.repository.AuthRepository
import com.max.quotial.data.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubmissionViewModel @Inject constructor(
    private val postRepository: PostRepository,
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(PostUiState())
    val uiState: StateFlow<PostUiState> = _uiState.asStateFlow()

    private var selectedGroup by mutableStateOf<Group?>(null)

    fun selectGroup(group: Group?) {
        selectedGroup = group
    }

    fun submitPost(quote: Quote) {
        if (quote.content.isBlank() || quote.source.isBlank()) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null,
            )

            val user = authRepository.getUser()
            postRepository.createPost(quote, user, selectedGroup).fold(
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