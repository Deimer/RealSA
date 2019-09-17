package com.realsa.data.models

class HistoryModel {
    var id: Int? = 0
    var createdAt: String? = ""
    var description: String? = ""
    var latitude: String? = ""
    var longitude: String? = ""

    override fun toString(): String {
        return "HistoryModel(id=$id, createdAt=$createdAt, description=$description, latitude=$latitude, longitude=$longitude)"
    }
}