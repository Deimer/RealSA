package com.realsa.di.timeline

import com.realsa.data.interactors.TimelineInteractor
import com.realsa.data.repositories.timeline.TimelineRepository
import com.realsa.views.timeline.TimelineViewModel
import dagger.Component

@Component(modules = [TimelineModule::class])
interface ITimelineComponent {

    fun inject(timelineRepository: TimelineRepository)
    fun inject(timelineInteractor: TimelineInteractor)
    fun inject(timelineViewModel: TimelineViewModel)
}