package com.realsa.di.timeline

import com.realsa.data.interactors.TimelineInteractor
import com.realsa.data.repositories.timeline.ITimelineRepository
import com.realsa.data.repositories.timeline.TimelineRepository
import dagger.Module
import dagger.Provides

@Module
class TimelineModule {

    @Provides
    fun provideTimelineRepository(): ITimelineRepository {
        return TimelineRepository()
    }
    @Provides
    fun provideTimelineInteractor(): TimelineInteractor {
        return TimelineInteractor()
    }
}