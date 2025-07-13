package com.max.quotial.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.max.quotial.data.model.VoteType
import com.max.quotial.data.repository.AuthRepository
import com.max.quotial.data.repository.PostRepository
import com.max.quotial.data.repository.VoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PostViewModel : ViewModel() {
    //TODO: replace by dependency injection
    private val postRepository = PostRepository()
    private val voteRepository = VoteRepository()
    private val authRepository = AuthRepository()


    private val posts = postRepository.getPosts()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val postsLiveData = posts.asLiveData()

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
                val userId = authRepository.getUserId();

                _activeVotePostIds.value.plus(postId)
                voteRepository.vote(postId, userId, vote)
            } catch (e: Exception) {
                Log.e("PostViewModel", "vote", e);
            } finally {
                _activeVotePostIds.value.minus(postId)
            }
        }
    }

    fun getPostsByGroup(groupId: String) = postsLiveData.map { post ->
        post.filter { it.groupId == groupId }
    }
}