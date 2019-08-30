package com.realsa.di.history

import com.realsa.data.interactors.HistoryInteractor
import com.realsa.data.repositories.history.HistoryRepository
import com.realsa.views.menu.HistoryViewModel
import dagger.Component

@Component(modules = [HistoryModule::class])
interface IHistoryComponent {

    fun inject(historyRepository: HistoryRepository)
    fun inject(historyInteractor: HistoryInteractor)
    fun inject(historyViewModel: HistoryViewModel)
}