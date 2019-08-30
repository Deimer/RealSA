package com.realsa.data.database.factories

import androidx.room.*
import com.realsa.R
import com.realsa.RealApplication
import com.realsa.data.database.dao.HistoryDao
import com.realsa.data.entities.HistoryEntity
import com.realsa.utils.RUtil.Companion.rString

@Database(entities = [HistoryEntity::class], version = 1)
abstract class HistoryDaoFactory: RoomDatabase() {

    init {
        getInstance()
    }

    abstract fun historyDao(): HistoryDao

    companion object {

        private var instance: HistoryDaoFactory? = null

        fun build(): HistoryDaoFactory? {
            return getInstance()
        }

        private fun getInstance(): HistoryDaoFactory? {
            if(instance == null) {
                synchronized(HistoryDaoFactory::class) {
                    instance = Room.databaseBuilder(
                        RealApplication.getInstance().applicationContext,
                        HistoryDaoFactory::class.java, rString(R.string.app_name)
                    )
                        .allowMainThreadQueries()
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return instance as HistoryDaoFactory
        }
    }
}