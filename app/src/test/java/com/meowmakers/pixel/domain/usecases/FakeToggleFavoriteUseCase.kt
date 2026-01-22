package com.meowmakers.pixel.domain.usecases

class FakeToggleFavoriteUseCase() :
    ToggleFavoriteUseCase {

    var toggleFavoriteCalledCount = 0

    override suspend fun toggleFavorite(id: String, currentValue: Boolean) {
        toggleFavoriteCalledCount++
    }

}