package com.meowmakers.pixel.presentation.screens.top_users.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.meowmakers.pixel.injection.AppContainer
import com.meowmakers.pixel.injection.ViewModelFactory
import com.meowmakers.pixel.presentation.design_system.network_image.LoadImageCallback
import com.meowmakers.pixel.presentation.design_system.network_image.NetworkImageViewModel
import com.meowmakers.pixel.presentation.screens.top_users.view_models.TopUsersScreenState
import com.meowmakers.pixel.presentation.screens.top_users.view_models.TopUsersViewModel

@Composable
fun TopUsersScreen(
    modifier: Modifier,
    userViewModel: TopUsersViewModel = viewModel(
        factory = ViewModelFactory(AppContainer)
    ),
    imageLoaderViewModel: NetworkImageViewModel = viewModel(
        factory = ViewModelFactory(AppContainer)
    )
) {
    val state by userViewModel.state.collectAsState()
    TopUsersScreenContent(
        modifier,
        state,
        onRetry = { userViewModel.loadData() },
        loadImage = { imageLoaderViewModel.getImage(it) }
    )
}

@Composable
fun TopUsersScreenContent(
    modifier: Modifier,
    state: TopUsersScreenState,
    onRetry: () -> Unit,
    loadImage: LoadImageCallback
) {
    when (state) {
        is TopUsersScreenState.Error -> ErrorContent(
            modifier = modifier,
            errorMessage = state.errorMessage,
            onRetry = onRetry
        )

        TopUsersScreenState.Loading -> LoadingContent(modifier)
        is TopUsersScreenState.Loaded -> LoadedContent(
            modifier = modifier,
            items = state.users.items,
            loadImage = loadImage
        )
    }
}

