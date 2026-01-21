package com.meowmakers.pixel

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner

class PixelTestRunner : AndroidJUnitRunner() {
    override fun newApplication(cl: ClassLoader?, name: String?, context: Context?): Application {
        return super.newApplication(cl, TestPixelApplication::class.java.name, context)
    }
}