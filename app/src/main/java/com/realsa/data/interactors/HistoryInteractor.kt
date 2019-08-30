package com.realsa.data.interactors

import com.realsa.data.models.HistoryModel
import com.realsa.data.repositories.history.IHistoryRepository
import com.realsa.di.history.DaggerIHistoryComponent
import com.realsa.di.history.HistoryModule
import javax.inject.Inject

class HistoryInteractor {

    @Inject
    lateinit var historyRepository: IHistoryRepository

    init {
        DaggerIHistoryComponent.builder().historyModule(HistoryModule()).build().inject(this)
    }

    fun insert(historyModel: HistoryModel) {
        historyRepository.insert(historyModel.toEntity())
    }

    fun update(historyModel: HistoryModel) {
        historyRepository.update(historyModel.toEntity())
    }

    fun delete(historyModel: HistoryModel) {
        historyRepository.delete(historyModel.toEntity())
    }
}