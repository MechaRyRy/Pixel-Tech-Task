package com.meowmakers.pixel.injection

import android.app.Instrumentation
import com.meowmakers.pixel.TestPixelApplication
import com.meowmakers.pixel.data.data_sources.rest.KtorRestClient
import com.meowmakers.pixel.data.data_sources.rest.RestClient
import com.meowmakers.pixel.data.repositories.StackOverflowRepositoryImpl
import com.meowmakers.pixel.domain.repositories.StackOverflowRepository
import com.meowmakers.pixel.domain.usecases.GetTopUsersUseCase
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
            val mockResponse = responseQueue.firstOrNull()
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

    override val repository: StackOverflowRepository by lazy {
        StackOverflowRepositoryImpl(restClient)
    }

    override val getTopUsersUseCase: GetTopUsersUseCase by lazy {
        GetTopUsersUseCase(repository)
    }

}

fun Instrumentation.dependencies(): TestApplicationContainer {
    val appContext = targetContext.applicationContext
    val app = appContext as TestPixelApplication
    return app.container as TestApplicationContainer
}