package com.meowmakers.pixel.data.data_sources.persistence

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow

class InMemoryPersistence() : Persistence {

    val favorites: MutableList<String> = mutableListOf()

    private val idFlow = MutableStateFlow<List<String>>(emptyList())

    override suspend fun addFavorite(id: String) {
        favorites.add(id)
        persistChanges()
    }

    override suspend fun removeFavorite(id: String) {
        favorites.remove(id)
        persistChanges()
    }

    private suspend fun persistChanges() = idFlow.emit(favorites.toList())

    override fun observeFavorites(): Flow<List<String>> = idFlow.asSharedFlow()
}