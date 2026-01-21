package com.meowmakers.pixel.presentation.screens.top_users.view_models

import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meowmakers.pixel.data.data_sources.rest.RestClient
import com.meowmakers.pixel.data.data_sources.rest.RestResponse
import com.meowmakers.pixel.domain.entities.TopUsers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TopUsersViewModel(
    private val restClient: RestClient
) : ViewModel() {

    private val _state: MutableStateFlow<TopUsersScreenState> =
        MutableStateFlow(TopUsersScreenState.Loading)
    val state: StateFlow<TopUsersScreenState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            delay(2000)
            val response = restClient.request<TopUsers>(
                uri = "https://api.stackexchange.com/2.2/users?page=1&pagesize=20&order=desc&sort=reputation&site=stackoverflow".toUri()
            )
            when (response) {
                is RestResponse.Failure<TopUsers> -> _state.value = TopUsersScreenState.Error(
                    response.message
                )

                is RestResponse.Success<TopUsers> -> _state.value = TopUsersScreenState.Loaded(
                    users = response.value
                )
            }
        }
    }
}