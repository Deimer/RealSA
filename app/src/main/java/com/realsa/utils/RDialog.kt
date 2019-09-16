package com.realsa.utils

import android.app.ProgressDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import com.realsa.R

class RDialog {

    companion object {
        fun showLoadingDialog(context: Context): ProgressDialog {
            val progressDialog = ProgressDialog(context).apply {
                this.show()
                this.setContentView(R.layout.content_progress_bar)
                this.isIndeterminate = true
                this.setCancelable(false)
                this.setCanceledOnTouchOutside(false)
            }

            if (progressDialog.window != null) {
                progressDialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }

            return progressDialog
        }
    }
}