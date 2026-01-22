package com.meowmakers.pixel.data.data_sources.rest

import android.net.Uri
import io.ktor.client.HttpClient
import io.ktor.client.request.headers
import io.ktor.client.request.request
import io.ktor.client.statement.readRawBytes
import io.ktor.http.ContentType
import io.ktor.http.contentType
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
            val response = client.request(uri.toString()) {
                this.method = method.asKtorMethod()
                contentType(ContentType.Application.Json)

                headers {
                    when (authorization) {
                        is Authorization.None -> {}
                    }
                }

                when (body) {
                    is Body.None -> {}
                }
            }

            return if (kType.classifier == ByteArray::class) {
                val bytes = response.readRawBytes()
                RestResponse.Success(bytes as T)
            } else {
                val typeInfo = TypeInfo(
                    type = kType.classifier as KClass<*>,
                    kotlinType = kType
                )
                RestResponse.Success(response.call.body(typeInfo) as T)
            }
        } catch (e: Exception) {
            return RestResponse.Failure(error = e)
        }
    }
}

@PublishedApi
internal fun HttpMethod.asKtorMethod(): KtorHttpMethod {
    return when (this) {
        HttpMethod.Get -> KtorHttpMethod.Get
    }
}
