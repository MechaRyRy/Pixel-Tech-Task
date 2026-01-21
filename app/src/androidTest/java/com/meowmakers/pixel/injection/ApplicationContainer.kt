package com.meowmakers.pixel.injection

import com.meowmakers.pixel.Fixtures
import com.meowmakers.pixel.data.data_sources.rest.KtorRestClient
import com.meowmakers.pixel.data.data_sources.rest.RestClient
import com.meowmakers.pixel.data.repositories.StackOverflowRepositoryImpl
import com.meowmakers.pixel.domain.repositories.StackOverflowRepository
import com.meowmakers.pixel.domain.usecases.GetTopUsersUseCase
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class TestApplicationContainer : ApplicationContainer {

    private val mockEngine: MockEngine by lazy {
        MockEngine { request ->
            when (request.url.encodedPath) {
                "/2.2/users" -> {
                    respond(
                        content = Fixtures.topUsersJson.raw,
                        status = HttpStatusCode.OK,
                        headers = headersOf(HttpHeaders.ContentType, "application/json")
                    )
                }

                else -> error("Unhandled ${request.url.encodedPath}")
            }
        }
    }

    private val ktorClient: HttpClient by lazy {
        HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
        }
    }

    private val restClient: RestClient by lazy {
        KtorRestClient(
            client = ktorClient
        )
    }

    override val repository: StackOverflowRepository by lazy {
        StackOverflowRepositoryImpl(restClient)
    }

    override val getTopUsersUseCase: GetTopUsersUseCase by lazy {
        GetTopUsersUseCase(repository)
    }

}
