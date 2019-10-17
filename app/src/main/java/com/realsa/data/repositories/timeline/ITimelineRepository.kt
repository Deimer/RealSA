package com.realsa.data.repositories.timeline

import com.realsa.data.entities.HistoryEntity
import io.reactivex.Observable

interface ITimelineRepository {

    fun get(): Observable<MutableList<HistoryEntity?>>?

    fun remove(): Observable<Boolean>?
}