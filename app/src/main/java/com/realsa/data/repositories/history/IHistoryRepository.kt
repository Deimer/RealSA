package com.realsa.data.repositories.history

import com.realsa.data.entities.HistoryEntity
import io.reactivex.Observable

interface IHistoryRepository {

    fun saveEmail(email: String): Observable<Boolean>?

    fun getEmail(keyName: String): Observable<String>?

    fun insertFirebase(historyEntity: HistoryEntity): Observable<Boolean>

    fun insert(historyEntity: HistoryEntity): Observable<Boolean>

    fun update(historyEntity: HistoryEntity): Observable<Boolean>

    fun delete(historyEntity: HistoryEntity): Observable<Boolean>

    fun get(): Observable<List<HistoryEntity>>?
}