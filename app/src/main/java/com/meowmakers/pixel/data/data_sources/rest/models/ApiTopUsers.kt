package com.meowmakers.pixel.data.data_sources.rest.models

import com.meowmakers.pixel.domain.entities.TopUsers
import kotlinx.serialization.Serializable

@Serializable
data class ApiTopUsers(val items: List<ApiTopUser>)

fun ApiTopUsers.toTopUsers(): TopUsers {
    return TopUsers(this.items.map {
        it.toTopUser()
    })
}