package com.meowmakers.pixel.presentation.screens.top_users.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meowmakers.pixel.domain.usecases.GetTopUsersUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TopUsersViewModel(
    private val getTopUsersUseCase: GetTopUsersUseCase
) : ViewModel() {

    private val _state: MutableStateFlow<TopUsersScreenState> =
        MutableStateFlow(TopUsersScreenState.Loading)
    val state: StateFlow<TopUsersScreenState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            delay(2000)
            val response = getTopUsersUseCase.get()
            response.fold(
                onFailure = { error ->
                    _state.value = TopUsersScreenState.Error(
                        error.message ?: "Unknown error"
                    )
                },
                onSuccess = { topUsers ->
                    _state.value = TopUsersScreenState.Loaded(
                        users = topUsers
                    )
                }
            )
        }
    }
}