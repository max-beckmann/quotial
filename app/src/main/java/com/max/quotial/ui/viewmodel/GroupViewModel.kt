package com.max.quotial.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.max.quotial.data.repository.AuthRepository
import com.max.quotial.data.repository.GroupRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class GroupViewModel : ViewModel() {
    private val groupRepository = GroupRepository()
    private val authRepository = AuthRepository()

    private val groups = groupRepository.getAllGroups()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val groupsLiveData = groups.asLiveData()

    private val memberships = groupRepository.getUserGroups(authRepository.getUserId())
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val membershipsLiveData = memberships.asLiveData()

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