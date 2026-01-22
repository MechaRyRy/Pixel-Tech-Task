package com.meowmakers.pixel.domain.usecases

import com.meowmakers.pixel.domain.repositories.StackOverflowRepository

interface LoadProfileImageUseCase {
    suspend fun get(url: String): Result<ByteArray>
}

class LoadProfileImageUseCaseImpl(private val repository: StackOverflowRepository) :
    LoadProfileImageUseCase {

    override suspend fun get(url: String): Result<ByteArray> = repository.getProfileImage(url)

}