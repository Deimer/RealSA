package com.realsa.di.preference

import com.realsa.data.repositories.helper.PreferencesHelper
import com.realsa.data.repositories.history.HistoryRepository
import dagger.Component

@Component(modules = [HelperModule::class])
interface IHelperComponent {

    fun inject(preferencesHelper: PreferencesHelper)
    fun inject(historyRepository: HistoryRepository)
}