package com.max.quotial.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.max.quotial.data.repository.AuthRepository
import com.max.quotial.data.repository.GroupRepository
import kotlinx.coroutines.launch

class GroupViewModel : ViewModel() {
    private val groupRepository = GroupRepository()
    private val authRepository = AuthRepository()

    fun createGroup(name: String, description: String?) {
        viewModelScope.launch {
            try {
                val userId = authRepository.getUserId()

                groupRepository.createGroup(name, userId, description)
                    .onSuccess { group ->
                        groupRepository.joinGroup(group.id, userId)
                    }
            } catch (e: Exception) {
                Log.e("GroupViewModel", "createGroup", e)
            }
        }
    }
}