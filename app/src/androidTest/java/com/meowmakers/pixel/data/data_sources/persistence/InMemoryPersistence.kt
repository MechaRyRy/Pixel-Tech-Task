package com.meowmakers.pixel.data.data_sources.persistence

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow

class InMemoryPersistence() : Persistence {

    val favorites: MutableList<String> = mutableListOf()
    val _idFlow = MutableStateFlow<List<String>>(favorites)

    override suspend fun addFavorite(id: String) {
        favorites.add(id)
        persistChanges()
    }

    override suspend fun removeFavorite(id: String) {
        favorites.remove(id)
        persistChanges()
    }

    private suspend fun persistChanges() = _idFlow.emit(favorites)

    override fun observeFavorites(): Flow<List<String>> = _idFlow.asSharedFlow()
}