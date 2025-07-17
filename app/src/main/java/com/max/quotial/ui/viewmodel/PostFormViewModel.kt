package com.max.quotial.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.max.quotial.data.model.Group
import com.max.quotial.data.model.Quote
import com.max.quotial.data.repository.AuthRepository
import com.max.quotial.data.repository.GroupRepository
import com.max.quotial.data.repository.PostRepository
import com.max.quotial.ui.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PostFormUiState(
    val groups: List<Group> = emptyList(),
)

sealed class UiEvent {
    object Success : UiEvent()
    data class Error(val message: String) : UiEvent()
}

@HiltViewModel
class PostFormViewModel @Inject constructor(
    private val postRepository: PostRepository,
    private val groupRepository: GroupRepository,
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState<PostFormUiState>>(UiState.Loading)
    val uiState: StateFlow<UiState<PostFormUiState>> = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent: SharedFlow<UiEvent> = _uiEvent.asSharedFlow()

    init {
        loadUserGroups()
    }

    fun loadUserGroups() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                groupRepository.getAllGroups().collect { groups ->
                    _uiState.value = UiState.Success(PostFormUiState(groups))
                }
            } catch (e: Exception) {
                _uiState.value =
                    UiState.Error(e.localizedMessage ?: "Unknown Error while loading user groups")
            }
        }
    }

    private var selectedGroup by mutableStateOf<Group?>(null)

    fun selectGroup(group: Group?) {
        selectedGroup = group
    }

    fun submitPost(content: String, source: String) {
        if (content.isBlank() || source.isBlank()) return

        viewModelScope.launch {
//            _uiEvent.emit(UiEvent.Loading)
            try {
                val quote = Quote(content, source)
                val user = authRepository.getUser()

                postRepository.createPost(quote, user, selectedGroup).fold(
                    onSuccess = {
                        _uiEvent.emit(UiEvent.Success)
                    },
                    onFailure = { exception ->
                        _uiEvent.emit(
                            UiEvent.Error(
                                exception.localizedMessage ?: "Unknown Error while creating post"
                            )
                        )
                    }
                )
            } catch (e: Exception) {
                _uiEvent.emit(
                    UiEvent.Error(
                        e.localizedMessage ?: "Unknown Error while submitting post"
                    )
                )
            }
        }
    }

    fun clear() {
        _uiState.value = UiState.Loading
    }
}