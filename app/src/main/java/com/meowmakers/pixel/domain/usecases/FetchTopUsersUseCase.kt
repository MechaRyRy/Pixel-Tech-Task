package com.meowmakers.pixel.domain.usecases

import com.meowmakers.pixel.domain.repositories.StackOverflowRepository

class FetchTopUsersUseCase(private val repository: StackOverflowRepository) {

    suspend fun fetch(): Unit = repository.fetchTopUsers()

}