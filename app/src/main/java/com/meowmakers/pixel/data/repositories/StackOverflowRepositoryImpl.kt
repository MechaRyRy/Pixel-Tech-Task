package com.meowmakers.pixel.data.repositories

import androidx.core.net.toUri
import com.meowmakers.pixel.data.data_sources.rest.RestClient
import com.meowmakers.pixel.data.data_sources.rest.RestResponse
import com.meowmakers.pixel.domain.entities.TopUsers
import com.meowmakers.pixel.domain.repositories.StackOverflowRepository

class StackOverflowRepositoryImpl(private val restClient: RestClient) : StackOverflowRepository {
    override suspend fun getTopUsers(): Result<TopUsers> {
        val response = restClient.request<TopUsers>(
            uri = "https://api.stackexchange.com/2.2/users?page=1&pagesize=20&order=desc&sort=reputation&site=stackoverflow".toUri()
        )
        return when (response) {
            is RestResponse.Failure<TopUsers> -> Result.failure(response.error)
            is RestResponse.Success<TopUsers> -> Result.success(response.value)
        }
    }

    override suspend fun getProfileImage(url: String): Result<ByteArray> {
        return when (val response = restClient.request<ByteArray>(uri = url.toUri())) {
            is RestResponse.Failure<ByteArray> -> Result.failure(response.error)
            is RestResponse.Success<ByteArray> -> Result.success(response.value)
        }
    }
}