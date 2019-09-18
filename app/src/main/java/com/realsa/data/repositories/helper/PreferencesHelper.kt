package com.realsa.data.repositories.helper

import android.content.Context
import android.content.SharedPreferences
import com.realsa.R
import com.realsa.RealApplication
import com.realsa.di.preference.DaggerIHelperComponent
import com.realsa.di.preference.HelperModule
import com.realsa.utils.RUtil.Companion.rString

class PreferencesHelper {

    private val preferencesName = rString(R.string.app_name)

    private val sharedPreferences: SharedPreferences = RealApplication
        .getInstance()
        .getSharedPreferences(
            preferencesName,
            Context.MODE_PRIVATE
        )

    init {
        DaggerIHelperComponent.builder().helperModule(HelperModule()).build().inject(this)
    }

    fun save(keyName: String, value: String?) {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(keyName, value)
        editor.apply()
    }

    fun getValueString(keyValue: String): String? {
        return sharedPreferences.getString(keyValue, "")
    }
}