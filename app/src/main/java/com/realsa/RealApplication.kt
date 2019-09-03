package com.realsa

import android.app.Application
import android.content.Context
import com.realsa.utils.RUtil.Companion.rString
import io.realm.Realm
import io.realm.RealmConfiguration
import java.util.*

class RealApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        val locale = Locale("es","ES")
        Locale.setDefault(locale)
        setupRealmConfiguration()
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

    private fun setupRealmConfiguration() {
        val key = ByteArray(64)
        Realm.init(this)
        val realmConfiguration = RealmConfiguration.Builder()
            .name(rString(R.string.app_name))
            .schemaVersion(0)
            .encryptionKey(key)
            .deleteRealmIfMigrationNeeded()
            .build()
        Realm.setDefaultConfiguration(realmConfiguration)
    }
}