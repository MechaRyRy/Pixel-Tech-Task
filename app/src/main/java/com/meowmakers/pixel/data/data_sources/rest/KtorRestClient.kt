package com.meowmakers.pixel.data.data_sources.rest

import android.net.Uri
import io.ktor.client.HttpClient
import io.ktor.client.request.headers
import io.ktor.client.request.request
import io.ktor.http.URLProtocol
import io.ktor.http.path
import io.ktor.util.reflect.TypeInfo
import kotlin.reflect.KClass
import kotlin.reflect.KType
import io.ktor.http.HttpMethod as KtorHttpMethod

class KtorRestClient(
    val client: HttpClient
) : RestClient() {

    @Suppress("UNCHECKED_CAST")
    override suspend fun <T> executeRequest(
        uri: Uri,
        body: Body,
        method: HttpMethod,
        authorization: Authorization,
        kType: KType
    ): RestResponse<T> {
        try {
            val response = client.request {
                url {
                    protocol = URLProtocol.HTTPS
                    host = uri.host ?: ""
                    path(uri.path ?: "")
                }
                this.method = method.asKtorMethod()

                headers {
                    when (authorization) {
                        is Authorization.None -> {}
                    }
                }

                when (body) {
                    is Body.None -> {}
                }
            }
            val typeInfo = TypeInfo(
                type = kType.classifier as KClass<*>,
                kotlinType = kType
            )
            return RestResponse.Success(response.call.body(typeInfo) as T)
        } catch (e: Exception) {
            return RestResponse.Failure(message = e.message ?: "Unknown error")
        }
    }
}

@PublishedApi
internal fun HttpMethod.asKtorMethod(): KtorHttpMethod {
    return when (this) {
        HttpMethod.Get -> KtorHttpMethod.Get
    }
}
