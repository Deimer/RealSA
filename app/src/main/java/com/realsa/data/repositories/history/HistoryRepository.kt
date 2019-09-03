package com.realsa.data.repositories.history

import android.util.Log
import com.realsa.data.entities.HistoryEntity
import com.realsa.di.history.DaggerIHistoryComponent
import com.realsa.di.history.HistoryModule
import io.reactivex.Observable
import io.realm.Realm

class HistoryRepository: IHistoryRepository {

    init {
        DaggerIHistoryComponent.builder().historyModule(HistoryModule()).build().inject(this)
    }

    private fun generateIdIncrement(): Int {
        val realmInstance = Realm.getDefaultInstance()
        val currentIdNum = realmInstance.where(HistoryEntity::class.java).max("id")
        val nextId: Int
        nextId = if (currentIdNum == null) {
            1
        } else {
            currentIdNum.toInt() + 1
        }
        return nextId
    }

    override fun insert(historyEntity: HistoryEntity): Observable<Boolean> {
        try {
            val realmInstance = Realm.getDefaultInstance()
            historyEntity.id = generateIdIncrement()
            realmInstance.executeTransaction { realm -> realm.insertOrUpdate(historyEntity) }
        } catch (e: Exception) {
            Log.e(this.javaClass.name, e.message)
            return Observable.just(false)
        }
        return Observable.just(true)
    }

    override fun update(historyEntity: HistoryEntity): Observable<Boolean> {
        val realmInstance = Realm.getDefaultInstance()
        return try {
            realmInstance.beginTransaction()
            realmInstance.insertOrUpdate(historyEntity)
            realmInstance.commitTransaction()
            Observable.just(true)
        } catch (e: Exception) {
            Log.e(this.javaClass.name, e.message)
            Observable.just(false)
        } finally {
            realmInstance.close()
        }
    }

    override fun delete(historyEntity: HistoryEntity): Observable<Boolean> {
        try {
            val realmInstance = Realm.getDefaultInstance()
            realmInstance.executeTransaction { realm -> realm.deleteAll() }
        } catch (e: Exception) {
            Log.e(this.javaClass.name, e.message)
            return Observable.just(false)
        }
        return Observable.just(true)
    }

    override fun get(): Observable<List<HistoryEntity>>? {
        var entities = mutableListOf<HistoryEntity>()
        try {
            val realmInstance = Realm.getDefaultInstance()
            entities = realmInstance.where(HistoryEntity::class.java).findAll()
        } catch (e: Exception) {
            Log.e(this.javaClass.name, e.message)
        }
        return Observable.just(entities)
    }
}