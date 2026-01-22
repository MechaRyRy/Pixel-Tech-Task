package com.meowmakers.pixel.injection

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.meowmakers.pixel.PixelApplication
import com.meowmakers.pixel.data.data_sources.persistence.Persistence
import com.meowmakers.pixel.data.data_sources.persistence.SharedPreferencesPersistence
import com.meowmakers.pixel.data.data_sources.rest.KtorRestClient
import com.meowmakers.pixel.data.data_sources.rest.RestClient
import com.meowmakers.pixel.data.repositories.StackOverflowRepositoryImpl
import com.meowmakers.pixel.domain.repositories.StackOverflowRepository
import com.meowmakers.pixel.domain.usecases.FetchTopUsersUseCase
import com.meowmakers.pixel.domain.usecases.FetchTopUsersUseCaseImpl
import com.meowmakers.pixel.domain.usecases.LoadProfileImageUseCase
import com.meowmakers.pixel.domain.usecases.LoadProfileImageUseCaseImpl
import com.meowmakers.pixel.domain.usecases.ObserveTopUsersUseCase
import com.meowmakers.pixel.domain.usecases.ObserveTopUsersUseCaseImpl
import com.meowmakers.pixel.domain.usecases.ToggleFavoriteUseCase
import com.meowmakers.pixel.domain.usecases.ToggleFavoriteUseCaseImpl
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient

interface ApplicationContainer {
    val repository: StackOverflowRepository
    val fetchTopUsersUseCase: FetchTopUsersUseCase
    val observeTopUsersUseCase: ObserveTopUsersUseCase
    val loadProfileImageUseCase: LoadProfileImageUseCase
    val toggleFavoriteUseCase: ToggleFavoriteUseCase
}

class ProdApplicationContainer(applicationContext: Context) : ApplicationContainer {

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

    private val persistence: Persistence by lazy {
        val sharedPreferences =
            applicationContext.getSharedPreferences("persistence", Context.MODE_PRIVATE)
        SharedPreferencesPersistence(sharedPreferences = sharedPreferences)
    }

    override val repository: StackOverflowRepository by lazy {
        StackOverflowRepositoryImpl(persistence = persistence, restClient = restClient)
    }

    override val fetchTopUsersUseCase: FetchTopUsersUseCase by lazy {
        FetchTopUsersUseCaseImpl(repository)
    }

    override val observeTopUsersUseCase: ObserveTopUsersUseCase by lazy {
        ObserveTopUsersUseCaseImpl(repository)
    }

    override val loadProfileImageUseCase: LoadProfileImageUseCase by lazy {
        LoadProfileImageUseCaseImpl(repository)
    }

    override val toggleFavoriteUseCase: ToggleFavoriteUseCase by lazy {
        ToggleFavoriteUseCaseImpl(repository)
    }
}

val AppContainer: ApplicationContainer
    @Composable get() = (LocalContext.current.applicationContext as PixelApplication).container