package com.meowmakers.pixel.presentation.design_system.network_image

import android.graphics.Bitmap
import android.util.LruCache

/**
 * Simple cache to avoid unnecessary reloading of images against an API.
 */
object NetworkImageCache {
    private val maxMemory: Int = (Runtime.getRuntime().maxMemory() / 1024).toInt()
    private val cacheSize: Int = maxMemory / 8

    private val memoryCache = object : LruCache<String, Bitmap>(cacheSize) {
        override fun sizeOf(key: String, bitmap: Bitmap): Int = bitmap.byteCount / 1024
    }

    fun get(url: String): Bitmap? = memoryCache.get(url)
    fun put(url: String, bitmap: Bitmap) {
        if (get(url) == null) memoryCache.put(url, bitmap)
    }
}