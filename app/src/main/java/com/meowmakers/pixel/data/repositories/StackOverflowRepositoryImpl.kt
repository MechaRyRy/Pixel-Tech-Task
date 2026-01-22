package com.meowmakers.pixel.data.repositories

import androidx.core.net.toUri
import com.meowmakers.pixel.data.data_sources.persistence.Persistence
import com.meowmakers.pixel.data.data_sources.rest.RestClient
import com.meowmakers.pixel.data.data_sources.rest.RestResponse
import com.meowmakers.pixel.data.data_sources.rest.models.ApiTopUsers
import com.meowmakers.pixel.data.data_sources.rest.models.toTopUsers
import com.meowmakers.pixel.domain.entities.TopUsers
import com.meowmakers.pixel.domain.repositories.StackOverflowRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.combine

class StackOverflowRepositoryImpl(
    private val persistence: Persistence,
    private val restClient: RestClient
) : StackOverflowRepository {

    private val networkStateFlow: MutableSharedFlow<RestResponse<ApiTopUsers>> = MutableSharedFlow()

    override suspend fun observeTopUsers(): Flow<Result<TopUsers>> {
        return combine(
            networkStateFlow,
            persistence.observeFavorites(),
            { networkResult, favorites ->
                when (networkResult) {
                    is RestResponse.Failure<ApiTopUsers> -> Result.failure(networkResult.error)
                    is RestResponse.Success<ApiTopUsers> -> Result.success(
                        networkResult.value.toTopUsers(favorites)
                    )
                }
            })
    }

    override suspend fun fetchTopUsers() {
        val response = restClient.request<ApiTopUsers>(
            uri = "https://api.stackexchange.com/2.2/users?page=1&pagesize=20&order=desc&sort=reputation&site=stackoverflow".toUri()
        )
        networkStateFlow.emit(response)
    }

    override suspend fun getProfileImage(url: String): Result<ByteArray> {
        return when (val response = restClient.request<ByteArray>(uri = url.toUri())) {
            is RestResponse.Failure<ByteArray> -> Result.failure(response.error)
            is RestResponse.Success<ByteArray> -> Result.success(response.value)
        }
    }
}