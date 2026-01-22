package com.meowmakers.pixel

import com.meowmakers.pixel.injection.ApplicationContainer
import com.meowmakers.pixel.injection.TestApplicationContainer

class TestPixelApplication : PixelApplication() {
    override var container: ApplicationContainer = TestApplicationContainer()

    fun resetContainer() {
        container = TestApplicationContainer()
    }
}