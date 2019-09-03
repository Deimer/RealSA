package com.realsa.data.models

class HistoryModel {
    var id: Int? = 0
    var createdAt: String? = ""
    var description: String? = ""
    var latitude: Float? = 0.toFloat()
    var longitude: Float? = 0.toFloat()

    override fun toString(): String {
        return "HistoryModel(id=$id, createdAt=$createdAt, description=$description, latitude=$latitude, longitude=$longitude)"
    }
}