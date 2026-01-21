package com.meowmakers.pixel.domain.entities

import kotlinx.serialization.Serializable

@Serializable
data class TopUsers(val items: List<TopUser>)