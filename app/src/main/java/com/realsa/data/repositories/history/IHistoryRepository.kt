package com.realsa.data.repositories.history

import androidx.lifecycle.LiveData
import com.realsa.data.entities.HistoryEntity

interface IHistoryRepository {

    fun insert(historyEntity: HistoryEntity)

    fun update(historyEntity: HistoryEntity)

    fun delete(historyEntity: HistoryEntity)

    fun get(): LiveData<List<HistoryEntity>>
}