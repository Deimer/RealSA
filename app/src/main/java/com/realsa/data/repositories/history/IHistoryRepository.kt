package com.realsa.data.repositories.history

import com.realsa.data.entities.HistoryEntity
import io.reactivex.Observable

interface IHistoryRepository {

    fun insert(historyEntity: HistoryEntity): Observable<Boolean>

    fun update(historyEntity: HistoryEntity): Observable<Boolean>

    fun delete(historyEntity: HistoryEntity): Observable<Boolean>

    fun get(): Observable<List<HistoryEntity>>?
}