package com.realsa.views.menu

import androidx.lifecycle.ViewModel
import com.realsa.data.interactors.HistoryInteractor
import com.realsa.data.models.HistoryModel
import com.realsa.di.history.DaggerIHistoryComponent
import com.realsa.di.history.HistoryModule
import com.realsa.livedata.SingleLiveEvent
import javax.inject.Inject

class HistoryViewModel: ViewModel() {

    @Inject
    lateinit var historyInteractor: HistoryInteractor

    init {
        DaggerIHistoryComponent.builder().historyModule(HistoryModule()).build().inject(this)
    }

    var singleLiveEvent: SingleLiveEvent<ViewEvent> = SingleLiveEvent()

    sealed class ViewEvent {
        class AnimationLogo (val success: Boolean): ViewEvent()
    }

    fun insertHistory(historyModel: HistoryModel) {
        historyInteractor.insert(historyModel)
    }
}