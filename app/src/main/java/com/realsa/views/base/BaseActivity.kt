package com.realsa.views.base

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.util.Patterns
import android.view.Gravity
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.agrawalsuneet.loaderspack.loaders.RingAndCircleLoader
import com.google.android.material.snackbar.Snackbar
import com.realsa.R
import com.realsa.utils.RDialog.Companion.showLoadingDialog
import com.realsa.utils.RUtil.Companion.rString

abstract class BaseActivity: AppCompatActivity() {

    private var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
    }

    fun EditText.getValue(): String = this.text.toString().trim()

    fun EditText.isValidEmail():
            Boolean = this.text.toString().isNotEmpty() &&
            Patterns.EMAIL_ADDRESS.matcher(this.text.toString()).matches()

    fun EditText.isValidCellphone():
            Boolean = this.text.toString().isNotEmpty() &&
            this.text.toString().length == 10

    fun showMessageBar(resource: Int) {
        showSnackBar(rString(resource))
    }

    fun showMessageToast(resource: Int) {
        showToast(rString(resource))
    }

    private fun showSnackBar(message: String) {
        val snackBar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
        val view = snackBar.view
        val params = view.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.TOP
        view.layoutParams = params

        val textView = view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        textView.setTextColor(ContextCompat.getColor(this, R.color.colorAccent))
        snackBar.show()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    fun showLoading() {
        hideLoading()
        hideKeyboard()
        progressDialog = showLoadingDialog(this)
    }

    fun hideLoading() {
        if (progressDialog != null && (progressDialog?.isShowing == true)) {
            progressDialog?.cancel()
        }
    }
}