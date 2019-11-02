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

    fun remove(): Observable<Boolean>? {
        return timelineRepository.remove()
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.flatMap { success ->
                Observable.just(success)
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
                numberPhone = it?.numberPhone
            }
            models.add(model)
        }
        return models
    }
}