package com.meowmakers.pixel.presentation.screens.top_users.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.meowmakers.pixel.injection.AppContainer
import com.meowmakers.pixel.injection.ViewModelFactory
import com.meowmakers.pixel.presentation.screens.top_users.view_models.TopUsersScreenState
import com.meowmakers.pixel.presentation.screens.top_users.view_models.TopUsersViewModel

@Composable
fun TopUsersScreen(
    modifier: Modifier,
    viewModel: TopUsersViewModel = viewModel(
        factory = ViewModelFactory(AppContainer)
    )
) {
    val state by viewModel.state.collectAsState()
    TopUsersScreenContent(
        modifier,
        state
    )
}

@Composable
fun TopUsersScreenContent(modifier: Modifier, state: TopUsersScreenState) {
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

