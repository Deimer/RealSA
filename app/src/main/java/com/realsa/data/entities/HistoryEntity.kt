package com.realsa.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "histories")
data class HistoryEntity(
    @ColumnInfo(name = "created_at")
    var createdAt: String,
    var description: String,
    var latitude: Float,
    var longitude: Float
){
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}