package com.meowmakers.pixel.presentation.design_system.network_image

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel

class NetworkImageViewModel : ViewModel() {

    private val loadingUrls: List<String> = mutableListOf()

    fun getImage(url: String): Bitmap? {
        val cachedImage = NetworkImageCache.get(url)
        if (cachedImage != null) {
            return cachedImage
        }

        if (loadingUrls.contains(url)) {
            return null
        }

        // Start loading via http request
        return null
    }
}