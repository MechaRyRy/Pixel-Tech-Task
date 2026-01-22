package com.meowmakers.pixel.domain.usecases

import com.meowmakers.pixel.domain.entities.TopUsers
import com.meowmakers.pixel.domain.repositories.StackOverflowRepository
import kotlinx.coroutines.flow.Flow

interface ObserveTopUsersUseCase {
    suspend fun observe(): Flow<Result<TopUsers>>
}

class ObserveTopUsersUseCaseImpl(private val repository: StackOverflowRepository) :
    ObserveTopUsersUseCase {

    override suspend fun observe(): Flow<Result<TopUsers>> = repository.observeTopUsers

}