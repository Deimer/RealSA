package com.realsa.data.repositories.history

import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import com.realsa.data.entities.HistoryEntity
import com.realsa.data.repositories.helper.PreferencesHelper
import com.realsa.di.history.DaggerIHistoryComponent
import com.realsa.di.history.HistoryModule
import io.reactivex.Observable
import io.realm.Realm
import javax.inject.Inject

class HistoryRepository: IHistoryRepository {

    @Inject
    lateinit var preferencesHelper: PreferencesHelper

    init {
        DaggerIHistoryComponent.builder().historyModule(HistoryModule()).build().inject(this)
    }

    override fun saveEmail(email: String): Observable<Boolean>? {
        preferencesHelper.save("email", email)
        return Observable.just(true)
    }

    override fun getEmail(keyName: String): Observable<String>? {
        return Observable.just(preferencesHelper.getValueString(keyName))
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

    override fun insertFirebase(historyEntity: HistoryEntity): Observable<Boolean> {
        val database = FirebaseDatabase.getInstance().reference
        println("Reference: ${database.toString()}")
        return Observable.just(database.child("histories").push().setValue(historyEntity).isSuccessful)
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