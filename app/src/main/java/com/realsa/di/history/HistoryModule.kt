package com.realsa.di.history

import com.realsa.data.interactors.HistoryInteractor
import com.realsa.data.repositories.history.HistoryRepository
import com.realsa.data.repositories.history.IHistoryRepository
import dagger.Module
import dagger.Provides

@Module
class HistoryModule {

    @Provides
    fun provideHistoryRepository(): IHistoryRepository {
        return HistoryRepository()
    }
    @Provides
    fun provideClientInteractor(): HistoryInteractor {
        return HistoryInteractor()
    }
}