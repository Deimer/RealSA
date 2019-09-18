package com.realsa.views.menu

import android.Manifest.permission
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import com.realsa.R
import com.realsa.views.camera.CameraLectorActivity
import kotlinx.android.synthetic.main.activity_menu.*
import kotlinx.android.synthetic.main.content_menu.*
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import android.app.Activity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.location.LocationRequest
import com.realsa.data.models.HistoryModel
import com.jakewharton.rxbinding2.view.RxView
import com.patloew.rxlocation.RxLocation
import com.realsa.views.base.BaseActivity
import com.realsa.views.login.LoginActivity
import com.realsa.views.timeline.TimelineActivity
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

@SuppressLint("CheckResult")
class MenuActivity : BaseActivity() {

    private lateinit var historyViewModel: HistoryViewModel

    private lateinit var rxLocation: RxLocation
    private var mLatitude: Float = 0.toFloat()
    private var mLongitude: Float = 0.toFloat()

    private val requestPermissionCamera = 9
    private val responseCamera = 10

    private var isLogged = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        setSupportActionBar(toolbar)
        setupView()
    }

    private fun setupView() {
        setupViewModel()
        getMyLocation()
        initClickListeners()
    }

    private fun setupViewModel() {
        historyViewModel = ViewModelProviders.of(this).get(HistoryViewModel::class.java)
        historyViewModel.getEmail()
        responseViewModel()
    }

    private fun responseViewModel() {
        historyViewModel.singleLiveEvent.observe(this, Observer {
            when(it) {
                is HistoryViewModel.ViewEvent.ResponseHistories -> {
                    if(it.histories.isNotEmpty()) {
                        println(it.histories.toString())
                    }
                }
                is HistoryViewModel.ViewEvent.ResponseEmail -> {
                    if(it.email.isNotEmpty()) {
                        isLogged = true
                    }
                }
            }
        })
    }

    @SuppressLint("MissingPermission")
    private fun getMyLocation() {
        rxLocation = RxLocation(this).apply {
            setDefaultTimeout(5, TimeUnit.SECONDS)
        }
        val locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 5000
        }
        rxLocation.location().updates(locationRequest)
            .flatMap { location ->
                rxLocation.geocoding().fromLocation(location).toObservable()
            }.subscribe { address ->
                mLatitude = address.latitude.toFloat()
                mLongitude = address.longitude.toFloat()
            }
    }

    private fun initClickListeners() {
        RxView.clicks(fabCamera).subscribe {
            val havePermission = askForPermission(permission.CAMERA, requestPermissionCamera)
            if(havePermission) {
                startActivityForResult(Intent(this, CameraLectorActivity::class.java), responseCamera)
            }
        }
        RxView.clicks(fabHistories).subscribe {
            if(isLogged) {
                startActivity(Intent(this, TimelineActivity::class.java))
            } else {
                startActivity(Intent(this, HistoryActivity::class.java))
            }
        }
        RxView.clicks(imgIcon).subscribe {
            if(isLogged) {
                showMessageToast("Ya estas logueado.")
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
            }
        }
    }

    private fun askForPermission(valPermission: String, requestPermission: Int): Boolean {
        return if (ContextCompat.checkSelfPermission(this, valPermission) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, valPermission)) {
                ActivityCompat.requestPermissions(this, arrayOf(valPermission), requestPermission)
                false
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(valPermission), requestPermission)
                false
            }
        } else {
            true
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED){
            if(requestCode == requestPermissionCamera) {
                startActivityForResult(Intent(this, CameraLectorActivity::class.java), responseCamera)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            responseCamera -> {
                if(resultCode == Activity.RESULT_OK) {
                    if(data != null) {
                        val string = data.getStringExtra("text_code")
                        createHistoryModel(string)
                    }
                }
            }
        }
    }

    private fun createHistoryModel(result: String) {
        val history = HistoryModel().apply {
            description = result
            createdAt = getDate()
            latitude = mLatitude.toString()
            longitude = mLongitude.toString()
        }
        historyViewModel.insertHistoryFirebase(history)
        historyViewModel.insertHistory(history)
    }

    private fun getDate(): String {
        val sdf = SimpleDateFormat(
            "yyyy/M/dd hh:mm", Locale.getDefault())
        return sdf.format(Date())
    }
}
