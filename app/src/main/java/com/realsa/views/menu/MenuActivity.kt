package com.realsa.views.menu

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.realsa.R
import com.realsa.views.camera.CameraLectorActivity
import kotlinx.android.synthetic.main.activity_menu.*
import kotlinx.android.synthetic.main.content_menu.*
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import android.Manifest.permission
import android.app.Activity

class MenuActivity : AppCompatActivity() {

    private val requestCodeCamera = 9
    private val responseCamera = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        setSupportActionBar(toolbar)
        setupView()
    }

    private fun setupView() {
        initClickListeners()
    }

    private fun initClickListeners() {
        fabCamera.setOnClickListener {
            askForPermission()
        }
    }

    private fun askForPermission() {
        val permission = permission.CAMERA
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                ActivityCompat.requestPermissions(this, arrayOf(permission), requestCodeCamera)
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(permission), requestCodeCamera)
            }
        } else {
            startActivityForResult(Intent(this, CameraLectorActivity::class.java), responseCamera)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED){
            if(requestCode == requestCodeCamera) {
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
                        println("for result: $string")
                    }
                }
            }
        }
    }


}
