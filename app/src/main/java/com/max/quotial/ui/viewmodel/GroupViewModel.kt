package com.max.quotial.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.max.quotial.data.model.Group
import com.max.quotial.data.repository.AuthRepository
import com.max.quotial.data.repository.GroupRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupViewModel @Inject constructor(
    private val groupRepository: GroupRepository,
    private val authRepository: AuthRepository
) : ViewModel() {
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

    fun join(groupId: String) {
        viewModelScope.launch {
            try {
                val userId = authRepository.getUserId()

                groupRepository.joinGroup(groupId, userId)
            } catch (e: Exception) {
                Log.e("GroupViewModel", "createGroup", e)
            }
        }
    }

    fun leave(groupId: String) {
        viewModelScope.launch {
            try {
                val userId = authRepository.getUserId()

                groupRepository.leaveGroup(groupId, userId)
            } catch (e: Exception) {
                Log.e("GroupViewModel", "createGroup", e)
            }
        }
    }

    fun getGroupById(id: String): LiveData<Group?> = groupsLiveData.map { list ->
        list.firstOrNull { it.id == id }
    }
}