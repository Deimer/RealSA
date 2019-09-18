package com.realsa.data.repositories.timeline

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.realsa.data.entities.HistoryEntity
import com.realsa.di.timeline.DaggerITimelineComponent
import com.realsa.di.timeline.TimelineModule
import io.reactivex.Observable

class TimelineRepository: ITimelineRepository {

    init {
        DaggerITimelineComponent.builder().timelineModule(TimelineModule()).build().inject(this)
    }

    override fun get(): Observable<MutableList<HistoryEntity?>>? {
        val histories = mutableListOf<HistoryEntity?>()
        val database = FirebaseDatabase.getInstance().reference
        val query = database.child("histories")

        return Observable.create { emitter ->
            query.addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onCancelled(snapshotError: DatabaseError) {
                    println("Error: ${snapshotError.message}")
                }
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach {
                        val history: HistoryEntity? = it.getValue(HistoryEntity::class.java)
                        histories.add(history)
                    }
                    emitter.onNext(histories)
                    emitter.onComplete()
                }
            })
        }
    }
}