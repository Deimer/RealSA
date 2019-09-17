package com.realsa.data.interactors

import com.realsa.data.entities.HistoryEntity
import com.realsa.data.models.HistoryModel
import com.realsa.data.repositories.timeline.ITimelineRepository
import com.realsa.di.timeline.DaggerITimelineComponent
import com.realsa.di.timeline.TimelineModule
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class TimelineInteractor {

    @Inject
    lateinit var timelineRepository: ITimelineRepository

    init {
        DaggerITimelineComponent.builder().timelineModule(TimelineModule()).build().inject(this)
    }

    fun get(): Observable<List<HistoryModel>>? {
        return timelineRepository.get()
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.flatMap { entityList ->
                Observable.just(convertHistoryEntityListToModels(entityList))
            }
    }

    private fun convertHistoryEntityListToModels(entityList: MutableList<HistoryEntity?>): List<HistoryModel> {
        val models = mutableListOf<HistoryModel>()
        entityList.forEach {
            val model = HistoryModel().apply {
                createdAt = it?.createdAt
                description = it?.description
                latitude = it?.latitude
                longitude = it?.longitude
            }
            models.add(model)
        }
        return models
    }
}