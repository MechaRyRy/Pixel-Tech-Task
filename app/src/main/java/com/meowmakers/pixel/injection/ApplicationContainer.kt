package com.meowmakers.pixel.injection

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.meowmakers.pixel.PixelApplication
import com.meowmakers.pixel.data.data_sources.rest.KtorRestClient
import com.meowmakers.pixel.data.data_sources.rest.RestClient
import com.meowmakers.pixel.data.repositories.StackOverflowRepositoryImpl
import com.meowmakers.pixel.domain.repositories.StackOverflowRepository
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient

interface ApplicationContainer {
    val repository: StackOverflowRepository
}

class ProdApplicationContainer : ApplicationContainer {

    private val okHttpInstance: OkHttpClient by lazy {
        OkHttpClient()
    }

    private val ktorClient: HttpClient by lazy {
        HttpClient(OkHttp) {
            engine {
                preconfigured = okHttpInstance
            }
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

}

val AppContainer: ApplicationContainer
    @Composable
    get() = (LocalContext.current.applicationContext as PixelApplication).container