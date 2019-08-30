package com.realsa.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.realsa.data.entities.HistoryEntity

@Dao
interface HistoryDao {

    @Insert
    fun insert(historyEntity: HistoryEntity)

    @Update
    fun update(historyEntity: HistoryEntity)

    @Delete
    fun delete(historyEntity: HistoryEntity)

    @Query("SELECT * FROM histories ORDER BY created_at DESC")
    fun get(): LiveData<List<HistoryEntity>>
}