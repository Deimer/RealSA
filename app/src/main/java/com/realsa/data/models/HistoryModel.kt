package com.realsa.data.models

import com.realsa.data.entities.HistoryEntity

class HistoryModel {
    var id: Int? = 0
    var createdAt: String = ""
    var description: String = ""
    var latitude: Float = 0.toFloat()
    var longitude: Float = 0.toFloat()

    fun toEntity() = HistoryEntity(
        createdAt = createdAt,
        description = description,
        latitude = latitude,
        longitude = longitude
    )

    override fun toString(): String {
        return "HistoryModel(id=$id, createdAt=$createdAt, description=$description, latitude=$latitude, longitude=$longitude)"
    }
}