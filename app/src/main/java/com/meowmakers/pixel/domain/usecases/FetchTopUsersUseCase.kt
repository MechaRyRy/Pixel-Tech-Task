package com.meowmakers.pixel.domain.usecases

import com.meowmakers.pixel.domain.repositories.StackOverflowRepository

interface FetchTopUsersUseCase {
    suspend fun fetch()
}

class FetchTopUsersUseCaseImpl(private val repository: StackOverflowRepository) :
    FetchTopUsersUseCase {

    override suspend fun fetch(): Unit = repository.fetchTopUsers()

}