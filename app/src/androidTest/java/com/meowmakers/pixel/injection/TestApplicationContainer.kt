package com.meowmakers.pixel.injection

import android.app.Instrumentation
import com.meowmakers.pixel.TestPixelApplication
import com.meowmakers.pixel.data.data_sources.persistence.InMemoryPersistence
import com.meowmakers.pixel.data.data_sources.persistence.Persistence
import com.meowmakers.pixel.data.data_sources.rest.KtorRestClient
import com.meowmakers.pixel.data.data_sources.rest.RestClient
import com.meowmakers.pixel.data.repositories.StackOverflowRepositoryImpl
import com.meowmakers.pixel.domain.repositories.StackOverflowRepository
import com.meowmakers.pixel.domain.usecases.FetchTopUsersUseCase
import com.meowmakers.pixel.domain.usecases.LoadProfileImageUseCase
import com.meowmakers.pixel.domain.usecases.ObserveTopUsersUseCase
import com.meowmakers.pixel.domain.usecases.ToggleFavoriteUseCase
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockRequestHandleScope
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.HttpResponseData
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class MockResponse(
    val forPath: String,
    val respondWith: (MockRequestHandleScope) -> HttpResponseData
)

class TestApplicationContainer : ApplicationContainer {

    val responseQueue = ArrayDeque<MockResponse>()

    private val mockEngine: MockEngine by lazy {
        MockEngine { request ->
            val mockResponse = responseQueue.removeFirstOrNull()
            when (request.url.encodedPath) {
                mockResponse?.forPath -> {
                    mockResponse.respondWith(this)
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

    private val persistence: Persistence by lazy {
        InMemoryPersistence()
    }

    override val repository: StackOverflowRepository by lazy {
        StackOverflowRepositoryImpl(persistence = persistence, restClient = restClient)
    }

    override val fetchTopUsersUseCase: FetchTopUsersUseCase by lazy {
        FetchTopUsersUseCase(repository)
    }

    override val observeTopUsersUseCase: ObserveTopUsersUseCase by lazy {
        ObserveTopUsersUseCase(repository)
    }

    override val loadProfileImageUseCase: LoadProfileImageUseCase by lazy {
        LoadProfileImageUseCase(repository)
    }

    override val toggleFavoriteUseCase: ToggleFavoriteUseCase by lazy {
        ToggleFavoriteUseCase(repository)
    }
}

fun Instrumentation.dependencies(): TestApplicationContainer {
    val appContext = targetContext.applicationContext
    val app = appContext as TestPixelApplication
    return app.container as TestApplicationContainer
}