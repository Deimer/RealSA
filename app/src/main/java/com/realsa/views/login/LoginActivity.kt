package com.realsa.views.login

import com.realsa.R
import android.os.Bundle
import com.google.firebase.auth.ActionCodeSettings
import com.google.firebase.auth.FirebaseAuth
import com.realsa.utils.RUtil.Companion.rString
import com.realsa.views.base.BaseActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setupView()
    }

    private fun setupView() {
        initClickListeners()
    }

    private fun initClickListeners() {
        butLogin.setOnClickListener {
            if(txtEmail.isValidEmail()) {
                showLoading()
                setupAuthentication()
            } else {
                showMessageBar(R.string.error_email)
            }
        }
    }

    private fun setupAuthentication() {
        val actionCodeSetting = createActionCodeSettings()
        val firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.sendSignInLinkToEmail(txtEmail.getValue(), actionCodeSetting)
            .addOnCompleteListener { task ->
                if(task.isSuccessful) {
                    println("email send")
                } else {
                    println("error: ${task.exception?.message}")
                }
                hideLoading()
            }
    }

    private fun createActionCodeSettings(): ActionCodeSettings {
        return ActionCodeSettings.newBuilder()
            .setUrl("https://realsa.page.link/summer-sale")
            .setHandleCodeInApp(true)
            .setAndroidPackageName(
                "com.realsa",
                false,
                "12"
            ).build()
    }
}
