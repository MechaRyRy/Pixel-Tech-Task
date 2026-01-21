package com.meowmakers.pixel.domain.repositories

import com.meowmakers.pixel.domain.entities.TopUsers

interface StackOverflowRepository {

    suspend fun getTopUsers(): Result<TopUsers>
}