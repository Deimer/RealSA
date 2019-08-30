package com.realsa.views.splash

import android.os.Handler
import androidx.lifecycle.ViewModel
import com.realsa.livedata.SingleLiveEvent

class SplasViewModel: ViewModel() {

    var singleLiveEvent: SingleLiveEvent<ViewEvent> = SingleLiveEvent()

    sealed class ViewEvent {
        class AnimationLogo (val success: Boolean): ViewEvent()
    }

    fun launchAnimationLogo(timeMs: Long) {
        Handler().postDelayed({
            singleLiveEvent.value = ViewEvent.AnimationLogo(true)
        }, timeMs)
    }
}