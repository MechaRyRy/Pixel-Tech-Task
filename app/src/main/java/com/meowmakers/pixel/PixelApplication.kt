package com.meowmakers.pixel

import android.app.Application
import com.meowmakers.pixel.injection.ApplicationContainer
import com.meowmakers.pixel.injection.ProdApplicationContainer

open class PixelApplication : Application() {
    open val container: ApplicationContainer by lazy {
        ProdApplicationContainer(applicationContext)
    }
}