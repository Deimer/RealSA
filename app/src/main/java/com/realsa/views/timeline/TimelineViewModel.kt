package com.realsa.views.timeline

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import com.realsa.data.interactors.TimelineInteractor
import com.realsa.data.models.HistoryModel
import com.realsa.di.timeline.DaggerITimelineComponent
import com.realsa.di.timeline.TimelineModule
import com.realsa.livedata.SingleLiveEvent
import javax.inject.Inject

@SuppressLint("CheckResult")
class TimelineViewModel: ViewModel() {

    @Inject
    lateinit var timelineInteractor: TimelineInteractor

    init {
        DaggerITimelineComponent.builder().timelineModule(TimelineModule()).build().inject(this)
    }

    var singleLiveEvent: SingleLiveEvent<ViewEvent> = SingleLiveEvent()

    sealed class ViewEvent {
        class ResponseHistories(val histories: List<HistoryModel>): ViewEvent()
        class ResponseError(val errorMessage: String): ViewEvent()
    }

    fun get() {
        timelineInteractor.get()?.subscribe ({
            if(it.isNotEmpty()) {
                singleLiveEvent.value = ViewEvent.ResponseHistories(it)
            } else {
                singleLiveEvent.value = ViewEvent.ResponseError("Lista vacía.")
            }
        }, {
            singleLiveEvent.value = ViewEvent.ResponseError(it.message.toString())
        })
    }
}