package com.realsa.data.repositories.history

import androidx.lifecycle.LiveData
import com.realsa.data.database.factories.HistoryDaoFactory
import com.realsa.data.entities.HistoryEntity
import com.realsa.di.history.DaggerIHistoryComponent
import com.realsa.di.history.HistoryModule

class HistoryRepository: IHistoryRepository {

    init {
        DaggerIHistoryComponent.builder().historyModule(HistoryModule()).build().inject(this)
    }

    override fun insert(historyEntity: HistoryEntity) {
        HistoryDaoFactory.build()?.historyDao()?.insert(historyEntity)
    }

    override fun update(historyEntity: HistoryEntity) {
        HistoryDaoFactory.build()?.historyDao()?.update(historyEntity)
    }

    override fun delete(historyEntity: HistoryEntity) {
        HistoryDaoFactory.build()?.historyDao()?.delete(historyEntity)
    }

    override fun get(): LiveData<List<HistoryEntity>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}