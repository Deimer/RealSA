package com.realsa.di.preference

import com.realsa.data.repositories.helper.PreferencesHelper
import dagger.Module
import dagger.Provides

@Module
class HelperModule {

    @Provides
    fun providePreferenceHelper(): PreferencesHelper {
        return PreferencesHelper()
    }
}