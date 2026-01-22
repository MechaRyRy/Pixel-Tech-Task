package com.meowmakers.pixel.domain.usecases

import com.meowmakers.pixel.domain.repositories.StackOverflowRepository

class LoadProfileImageUseCase(private val repository: StackOverflowRepository) {

    suspend fun get(url: String): Result<ByteArray> = repository.getProfileImage(url)

}