package com.meowmakers.pixel.data.data_sources.persistence

interface Persistence {

    suspend fun addFavorite(id: String): Unit
    suspend fun removeFavorite(id: String): Unit
    fun getFavorites(): List<String>
}