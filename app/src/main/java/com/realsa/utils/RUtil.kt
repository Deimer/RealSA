package com.realsa.utils

import com.realsa.RealApplication

class RUtil {

    companion object {
        fun rString(resId: Int): String {
            return RealApplication.getInstance().getString(resId)
        }
    }
}