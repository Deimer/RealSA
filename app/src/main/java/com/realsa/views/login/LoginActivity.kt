package com.realsa.views.login

import com.realsa.R
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.auth.ActionCodeSettings
import com.google.firebase.auth.FirebaseAuth
import com.realsa.views.base.BaseActivity
import com.realsa.views.menu.HistoryViewModel
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity() {

    private lateinit var historyViewModel: HistoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setupView()
    }

    private fun setupView() {
        initClickListeners()
        setupViewModel()
    }

    private fun setupViewModel() {
        historyViewModel = ViewModelProviders.of(this).get(HistoryViewModel::class.java)
        historyViewModel.singleLiveEvent.observe(this, Observer {
            when(it) {
                is HistoryViewModel.ViewEvent.ResponseSaveEmail -> {
                    if(it.success) {
                        setupAuthentication()
                    }
                }
            }
        })
    }

    private fun initClickListeners() {
        butLogin.setOnClickListener {
            if(txtEmail.isValidEmail()) {
                if(txtEmail.isPermittedEmail()) {
                    showLoading()
                    historyViewModel.saveEmail(txtEmail.getValue())
                } else {
                    showMessageBar("Correo no permitido.")
                }
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
                    showMessageToast("Código de enlace enviado a tu correo")
                    val intent = packageManager.getLaunchIntentForPackage("com.google.android.gm")
                    startActivity(intent)
                    finish()
                } else {
                    showMessageToast("error: ${task.exception?.message}")
                }
                hideLoading()
            }
    }

    private fun createActionCodeSettings(): ActionCodeSettings {
        return ActionCodeSettings.newBuilder()
            .setUrl("https://realsa2.page.link/4v3Q")
            .setHandleCodeInApp(true)
            .setAndroidPackageName(
                "com.realsa",
                false,
                "12"
            ).build()
    }
}
