package com.max.quotial.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.max.quotial.data.repository.AuthRepository
import com.max.quotial.data.repository.VoteRepository

class PostViewModel : ViewModel() {
    private val voteRepository = VoteRepository()
    private val authRepository = AuthRepository()

    fun vote(postId: String, vote: Int) {
        val user = authRepository.getUser()
        if (user == null) {
            return
        }

        voteRepository.vote(postId, user.uid, vote)
    }
}