package com.meowmakers.pixel.domain.repositories

import com.meowmakers.pixel.domain.entities.TopUsers
import kotlinx.coroutines.flow.Flow

interface StackOverflowRepository {

    val observeTopUsers: Flow<Result<TopUsers>>
    suspend fun fetchTopUsers()
    suspend fun getProfileImage(url: String): Result<ByteArray>
    suspend fun addFavorite(id: String)
    suspend fun removeFavorite(id: String)
}