package com.meowmakers.pixel.injection

import com.meowmakers.pixel.Fixtures
import com.meowmakers.pixel.data.data_sources.rest.KtorRestClient
import com.meowmakers.pixel.data.data_sources.rest.RestClient
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

    override val restClient: RestClient by lazy {
        KtorRestClient(
            client = ktorClient
        )
    }

}
