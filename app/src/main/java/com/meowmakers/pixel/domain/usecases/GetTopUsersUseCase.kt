package com.meowmakers.pixel.domain.usecases

import com.meowmakers.pixel.domain.entities.TopUsers
import com.meowmakers.pixel.domain.repositories.StackOverflowRepository

class GetTopUsersUseCase(private val repository: StackOverflowRepository) {

    suspend fun get(): Result<TopUsers> = repository.getTopUsers()

}