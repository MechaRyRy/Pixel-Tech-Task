@file:OptIn(ExperimentalSerializationApi::class)

package com.meowmakers.pixel.presentation.screens.top_users.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@Composable
fun TopUsersScreen(modifier: Modifier, state: TopUsersScreenState) {
    when (state) {
        is TopUsersScreenState.Error -> ErrorContent(
            modifier = modifier,
            errorMessage = state.errorMessage,
            onRetry = {}
        )

        TopUsersScreenState.Loading -> LoadingContent(modifier)
        is TopUsersScreenState.Loaded -> LoadedContent(
            modifier = modifier,
            items = state.users.items
        )
    }
}

sealed class TopUsersScreenState {
    data object Loading : TopUsersScreenState()
    data class Loaded(val users: TopUsers) : TopUsersScreenState()
    data class Error(val errorMessage: String) : TopUsersScreenState()
}

@Serializable
data class TopUsers(val items: List<TopUser>)

@Serializable
data class TopUser(
    @JsonNames("account_id") val id: Int,
    @JsonNames("profile_image") val profileImageLink: String,
    @JsonNames("display_name") val name: String,
    val reputation: Int
)