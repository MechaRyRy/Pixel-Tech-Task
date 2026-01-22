package com.meowmakers.pixel.injection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.meowmakers.pixel.presentation.design_system.network_image.NetworkImageViewModel
import com.meowmakers.pixel.presentation.screens.top_users.view_models.TopUsersViewModel

class ViewModelFactory(private val container: ApplicationContainer) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TopUsersViewModel::class.java)) {
            return TopUsersViewModel(
                observeTopUsersUseCase = container.observeTopUsersUseCase,
                fetchTopUsersUseCase = container.fetchTopUsersUseCase
            ) as T
        }

        if (modelClass.isAssignableFrom(NetworkImageViewModel::class.java)) {
            return NetworkImageViewModel(container.loadProfileImageUseCase) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}