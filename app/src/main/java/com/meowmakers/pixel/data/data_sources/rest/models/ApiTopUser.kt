@file:OptIn(ExperimentalSerializationApi::class)

package com.meowmakers.pixel.data.data_sources.rest.models

import com.meowmakers.pixel.domain.entities.TopUser
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@Serializable
data class ApiTopUser(
    @JsonNames("account_id") val id: Int,
    @JsonNames("profile_image") val profileImageLink: String,
    @JsonNames("display_name") val name: String,
    val reputation: Int
)

fun ApiTopUser.toTopUser(): TopUser {
    return TopUser(
        id = id,
        profileImageLink = profileImageLink,
        name = name,
        reputation = reputation,
        following = false
    )
}