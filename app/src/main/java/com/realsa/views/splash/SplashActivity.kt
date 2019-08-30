package com.realsa.views.splash

import android.animation.Animator
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.realsa.R
import com.realsa.views.menu.MenuActivity
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : FragmentActivity() {

    private lateinit var welcomeViewModel: SplasViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        setupView()
    }

    private fun setupView() {
        setupViewModel()
        initSubscriptions()
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
                    openActivityTimeline()
                    animateLogoPulse()
                }
            }).playOn(imgLogo)
        imgLogo.visibility = View.VISIBLE
    }

    private fun animateLogoPulse() {
        YoYo.with(Techniques.Tada)
            .duration(700)
            .repeat(700)
            .playOn(imgLogo)
    }

    private fun openActivityTimeline() {
        startActivity(Intent(this, MenuActivity::class.java))
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        finish()
    }
}
