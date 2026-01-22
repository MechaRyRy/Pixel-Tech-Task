package com.meowmakers.pixel.presentation.design_system.network_image

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModel
import com.meowmakers.pixel.domain.usecases.LoadProfileImageUseCase

class NetworkImageViewModel(private val loadProfileImageUseCase: LoadProfileImageUseCase) :
    ViewModel() {

    private val loadingUrls: MutableList<String> = mutableListOf()

    suspend fun getImage(url: String): Bitmap? {
        val cachedImage = NetworkImageCache.get(url)
        if (cachedImage != null) {
            return cachedImage
        }

        if (loadingUrls.contains(url)) {
            return null
        }

        loadingUrls.add(url)
        val bytesOrFailure = loadProfileImageUseCase.get(url)
        val bitmap = bytesOrFailure.getOrNull()?.let {
            BitmapFactory.decodeByteArray(it, 0, it.size)
        }

        if (bitmap != null) {
            NetworkImageCache.put(url, bitmap)
        }
        loadingUrls.remove(url)
        return bitmap
    }
}