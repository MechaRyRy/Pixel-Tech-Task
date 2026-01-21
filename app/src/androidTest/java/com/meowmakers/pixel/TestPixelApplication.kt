package com.meowmakers.pixel

import com.meowmakers.pixel.injection.ApplicationContainer
import com.meowmakers.pixel.injection.TestApplicationContainer

class TestPixelApplication : PixelApplication() {
    override val container: ApplicationContainer = TestApplicationContainer()
}