package com.meowmakers.pixel.presentation.screens.top_users.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meowmakers.pixel.domain.usecases.FetchTopUsersUseCase
import com.meowmakers.pixel.domain.usecases.ObserveTopUsersUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TopUsersViewModel(
    private val observeTopUsersUseCase: ObserveTopUsersUseCase,
    private val fetchTopUsersUseCase: FetchTopUsersUseCase,
) : ViewModel() {

    private val _state: MutableStateFlow<TopUsersScreenState> =
        MutableStateFlow(TopUsersScreenState.Loading)
    val state: StateFlow<TopUsersScreenState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            observeTopUsersUseCase.observe()
                .collect { response ->
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

        viewModelScope.launch {
            fetchTopUsersUseCase.fetch()
        }
    }

    fun retry() = viewModelScope.launch {
        _state.value = TopUsersScreenState.Loading
        fetchTopUsersUseCase.fetch()
    }
}