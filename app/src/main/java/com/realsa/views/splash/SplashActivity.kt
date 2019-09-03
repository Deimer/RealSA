package com.realsa.views.splash

import android.Manifest.permission
import android.animation.Animator
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding2.view.RxView
import com.realsa.R
import com.realsa.views.menu.MenuActivity
import kotlinx.android.synthetic.main.activity_splash.*

@SuppressLint("CheckResult")
class SplashActivity : FragmentActivity() {

    private val requestPermissionLocation = 12
    private lateinit var welcomeViewModel: SplasViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        setupView()
    }

    private fun setupView() {
        setupViewModel()
        initSubscriptions()
        initClickListeners()
    }

    private fun initClickListeners() {
        RxView.clicks(imgLogo).subscribe {
            askForPermission(permission.ACCESS_COARSE_LOCATION, requestPermissionLocation)
        }
    }

    private fun setupViewModel() {
        welcomeViewModel = ViewModelProviders.of(this).get(SplasViewModel::class.java)
        welcomeViewModel.launchAnimationLogo(700)
    }

    private fun initSubscriptions() {
        welcomeViewModel.singleLiveEvent.observe(this, Observer {
            when(it) {
                is SplasViewModel.ViewEvent.AnimationLogo -> {
                    if(it.success) {
                        animateLogo()
                    }
                }
            }
        })
    }

    private fun animateLogo() {
        YoYo.with(Techniques.SlideInUp)
            .duration(700)
            .withListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {}
                override fun onAnimationCancel(animation: Animator) {}
                override fun onAnimationRepeat(animation: Animator) {}
                override fun onAnimationEnd(animation: Animator) {
                    animateLogoPulse()
                }
            }).playOn(imgLogo)
        imgLogo.visibility = View.VISIBLE
    }

    private fun animateLogoPulse() {
        YoYo.with(Techniques.Tada)
            .duration(700)
            .withListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {}
                override fun onAnimationCancel(animation: Animator) {}
                override fun onAnimationRepeat(animation: Animator) {}
                override fun onAnimationEnd(animation: Animator) {
                    askForPermission(permission.ACCESS_FINE_LOCATION, requestPermissionLocation)
                }
            }).playOn(imgLogo)
    }

    private fun askForPermission(valPermission: String, requestPermission: Int) {
        val arrayPermissions = arrayOf(valPermission, permission.ACCESS_FINE_LOCATION)
        if (ContextCompat.checkSelfPermission(this, valPermission) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, valPermission)) {
                ActivityCompat.requestPermissions(this, arrayPermissions, requestPermission)
            } else {
                ActivityCompat.requestPermissions(this, arrayPermissions, requestPermission)
            }
        } else {
            openActivityTimeline()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == requestPermissionLocation) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openActivityTimeline()
            } else {
                Snackbar.make(imgLogo, "Para continuar, debes aceptar los permisos.", Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun openActivityTimeline() {
        startActivity(Intent(this, MenuActivity::class.java))
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        finish()
    }
}
