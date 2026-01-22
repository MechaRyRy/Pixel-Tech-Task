package com.meowmakers.pixel.domain.entities

data class TopUser(
    val id: Int,
    val profileImageLink: String,
    val name: String,
    val reputation: Int,
    val following: Boolean
)