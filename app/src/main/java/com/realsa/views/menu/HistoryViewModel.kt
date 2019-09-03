package com.realsa.views.menu

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import com.realsa.data.interactors.HistoryInteractor
import com.realsa.data.models.HistoryModel
import com.realsa.di.history.DaggerIHistoryComponent
import com.realsa.di.history.HistoryModule
import com.realsa.livedata.SingleLiveEvent
import javax.inject.Inject

@SuppressLint("CheckResult")
class HistoryViewModel: ViewModel() {

    @Inject
    lateinit var historyInteractor: HistoryInteractor

    init {
        DaggerIHistoryComponent.builder().historyModule(HistoryModule()).build().inject(this)
    }

    var singleLiveEvent: SingleLiveEvent<ViewEvent> = SingleLiveEvent()

    sealed class ViewEvent {
        class ResponseHistories(val histories: List<HistoryModel>): ViewEvent()
        class ResponseError(val errorMessage: String): ViewEvent()
    }

    fun insertHistory(historyModel: HistoryModel) {
        historyInteractor.insert(historyModel)
    }

    fun getHistories() {
        historyInteractor.getHistories()?.subscribe({
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