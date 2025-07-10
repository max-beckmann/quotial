package com.max.quotial.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.max.quotial.data.model.VoteType
import com.max.quotial.data.repository.AuthRepository
import com.max.quotial.data.repository.VoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PostViewModel : ViewModel() {
    //TODO: replace by dependency injection
    private val voteRepository = VoteRepository()
    private val authRepository = AuthRepository()

    private val userVotes = voteRepository.getVotesForUser(authRepository.getUser()!!.uid)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyMap()
        )

    val userVotesLiveData = userVotes.asLiveData();

    private val _activeVotePostIds = MutableStateFlow(emptySet<String>())
    val activeVotePostIds = _activeVotePostIds.asLiveData()

    fun vote(postId: String, vote: VoteType) {
        viewModelScope.launch {
            try {
                //TODO: replace by dependency injection
                val user = authRepository.getUser()
                if (user == null) {
                    return@launch
                }
                val userId = user.uid;

                _activeVotePostIds.value.plus(postId)
                voteRepository.vote(postId, userId, vote)
            } catch (e: Exception) {
                Log.e("PostViewModel", "vote", e);
            } finally {
                _activeVotePostIds.value.minus(postId)
            }
        }
    }
}