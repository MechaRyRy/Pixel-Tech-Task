package com.meowmakers.pixel

import android.app.Application
import com.meowmakers.pixel.injection.ApplicationContainer

class PixelApplication : Application() {

    val applicationContainer = ApplicationContainer()
}