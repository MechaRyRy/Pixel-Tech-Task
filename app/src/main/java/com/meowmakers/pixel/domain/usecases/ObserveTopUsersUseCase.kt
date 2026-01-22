package com.meowmakers.pixel.domain.usecases

import com.meowmakers.pixel.domain.entities.TopUsers
import com.meowmakers.pixel.domain.repositories.StackOverflowRepository
import kotlinx.coroutines.flow.Flow

class ObserveTopUsersUseCase(private val repository: StackOverflowRepository) {

    suspend fun observe(): Flow<Result<TopUsers>> = repository.observeTopUsers()

}