package com.meowmakers.pixel.domain.repositories

import android.graphics.Bitmap
import com.meowmakers.pixel.domain.entities.TopUsers

interface StackOverflowRepository {

    suspend fun getTopUsers(): Result<TopUsers>
    suspend fun getProfileImage(url: String): Result<ByteArray>
}