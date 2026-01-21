package com.meowmakers.pixel.presentation.screens.top_users.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.meowmakers.pixel.presentation.screens.top_users.view_models.TopUsersScreenState

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

