package com.realsa.data.entities

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class HistoryEntity: RealmObject() {
    @PrimaryKey
    var id: Int = 0

    var createdAt: String? = null
    var description: String? = null
    var latitude: String? = ""
    var longitude: String? = ""
}