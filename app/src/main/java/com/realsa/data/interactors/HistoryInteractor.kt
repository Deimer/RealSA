package com.realsa.data.interactors

import com.realsa.data.entities.HistoryEntity
import com.realsa.data.models.HistoryModel
import com.realsa.data.repositories.history.IHistoryRepository
import com.realsa.di.history.DaggerIHistoryComponent
import com.realsa.di.history.HistoryModule
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class HistoryInteractor {

    @Inject
    lateinit var historyRepository: IHistoryRepository

    init {
        DaggerIHistoryComponent.builder().historyModule(HistoryModule()).build().inject(this)
    }

    fun saveEmail(email: String): Observable<Boolean>? {
        return historyRepository.saveEmail(email)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.flatMap {
                Observable.just(it)
            }
    }

    fun getEmail(): Observable<String>? {
        return historyRepository.getEmail("email")
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.flatMap {
                Observable.just(it)
            }
    }

    fun insertFirebase(historyModel: HistoryModel): Observable<Boolean> {
        return historyRepository.insertFirebase(convertModelToEntity(historyModel))
    }

    fun insert(historyModel: HistoryModel): Observable<Boolean> {
        return historyRepository.insert(convertModelToEntity(historyModel))
    }

    fun getHistories(): Observable<List<HistoryModel>>? {
        return historyRepository.get()
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.flatMap {
                Observable.just(convertEntitiesToModels(it))
            }
    }

    private fun convertEntitiesToModels(entities: List<HistoryEntity>): List<HistoryModel> {
        val models = mutableListOf<HistoryModel>()
        entities.forEach { entity ->
            val model = convertEntityToModel(entity)
            models.add(model)
        }
        return models
    }

    private fun convertEntityToModel(entity: HistoryEntity): HistoryModel {
        return HistoryModel().apply {
            id = entity.id
            description = entity.description
            createdAt = entity.createdAt
            latitude = entity.latitude
            longitude = entity.longitude
        }
    }

    private fun convertModelToEntity(entity: HistoryModel): HistoryEntity {
        return HistoryEntity().apply {
            id = Math.random().toInt()
            description = entity.description
            createdAt = entity.createdAt
            latitude = entity.latitude
            longitude = entity.longitude
        }
    }
}