package com.meowmakers.pixel.domain.usecases

import com.meowmakers.pixel.domain.repositories.StackOverflowRepository

interface ToggleFavoriteUseCase {
    suspend fun toggleFavorite(id: String, currentValue: Boolean)
}

class ToggleFavoriteUseCaseImpl(private val repository: StackOverflowRepository) :
    ToggleFavoriteUseCase {

    override suspend fun toggleFavorite(id: String, currentValue: Boolean): Unit {
        if (currentValue) {
            repository.removeFavorite(id)
        } else {
            repository.addFavorite(id)
        }
    }

}