package com.meowmakers.pixel.data.data_sources.persistence

import kotlinx.coroutines.flow.Flow

interface Persistence {

    suspend fun addFavorite(id: String): Unit
    suspend fun removeFavorite(id: String): Unit
    fun observeFavorites(): Flow<List<String>>
}