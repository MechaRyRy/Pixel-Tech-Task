package com.meowmakers.pixel.data.data_sources.rest

import android.net.Uri
import kotlin.reflect.KType
import kotlin.reflect.typeOf

/**
 * Has to be an abstract class so that developers can call `request` without passing TypeInfo
 * or having to create an extension function that developers need to remember to call.
 */
abstract class RestClient {

    suspend inline fun <reified T> request(
        uri: Uri,
        body: Body = Body.None,
        method: HttpMethod = HttpMethod.Get,
        authorization: Authorization = Authorization.None
    ): RestResponse<T> = executeRequest(uri, body, method, authorization, typeOf<T>())

    @PublishedApi
    internal abstract suspend fun <T> executeRequest(
        uri: Uri,
        body: Body,
        method: HttpMethod,
        authorization: Authorization,
        kType: KType
    ): RestResponse<T>
}

sealed class RestResponse<T> {
    data class Success<T>(val value: T) : RestResponse<T>()
    data class Failure<T>(val message: String) : RestResponse<T>()
}

enum class HttpMethod {
    Get
}

sealed class Authorization {
    data object None : Authorization()
}

sealed class Body {
    data object None : Body()

}