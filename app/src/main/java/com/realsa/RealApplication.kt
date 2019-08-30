package com.realsa

import android.app.Application
import android.content.Context
import java.util.*

class RealApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        val locale = Locale("es","ES")
        Locale.setDefault(locale)
    }

    companion object {
        private lateinit var instance: RealApplication

        fun getInstance(): RealApplication {
            return instance
        }

        fun getAppContext(): Context {
            return instance.applicationContext
        }
    }
}