@file:OptIn(ExperimentalSerializationApi::class)

package com.meowmakers.pixel.domain.entities

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@Serializable
data class TopUser(
    @JsonNames("account_id") val id: Int,
    @JsonNames("profile_image") val profileImageLink: String,
    @JsonNames("display_name") val name: String,
    val reputation: Int
)