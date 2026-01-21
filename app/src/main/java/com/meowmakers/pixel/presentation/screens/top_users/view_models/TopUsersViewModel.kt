package com.meowmakers.pixel.presentation.screens.top_users.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meowmakers.pixel.Fixtures
import com.meowmakers.pixel.domain.entities.TopUsers
import com.meowmakers.pixel.load
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TopUsersViewModel : ViewModel() {

    private val _state: MutableStateFlow<TopUsersScreenState> =
        MutableStateFlow(TopUsersScreenState.Loading)
    val state: StateFlow<TopUsersScreenState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            delay(2000)
            val topUsers = Fixtures.topUsersJson.load<TopUsers>()
            _state.value = TopUsersScreenState.Loaded(topUsers)
        }
    }
}