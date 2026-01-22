package com.meowmakers.pixel.domain.usecases

import com.meowmakers.pixel.domain.repositories.StackOverflowRepository

class ToggleFavoriteUseCase(private val repository: StackOverflowRepository) {

    suspend fun toggleFavorite(id: String, currentValue: Boolean): Unit {
        if (currentValue) {
            repository.removeFavorite(id)
        } else {
            repository.addFavorite(id)
        }
    }

}