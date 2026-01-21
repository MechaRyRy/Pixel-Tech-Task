package com.meowmakers.pixel.presentation.screens.top_users.view_models

import com.meowmakers.pixel.domain.entities.TopUsers

sealed class TopUsersScreenState {
    data object Loading : TopUsersScreenState()
    data class Loaded(val users: TopUsers) : TopUsersScreenState()
    data class Error(val errorMessage: String) : TopUsersScreenState()
}