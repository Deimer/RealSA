package com.realsa.views.camera

import android.app.Activity
import android.content.Intent
import android.graphics.PointF
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.dlazaro66.qrcodereaderview.QRCodeReaderView
import com.google.android.material.snackbar.Snackbar
import com.realsa.R
import kotlinx.android.synthetic.main.activity_camera_lector.*

class CameraLectorActivity : AppCompatActivity(), QRCodeReaderView.OnQRCodeReadListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera_lector)
        setupView()
    }

    override fun onResume() {
        super.onResume()
        qrScanner.startCamera()
    }

    override fun onPause() {
        super.onPause()
        qrScanner.stopCamera()
    }

    private fun setupView() {
        setupQrScanner()
    }

    private fun setupQrScanner() {
        qrScanner.setOnQRCodeReadListener(this)
        qrScanner.setQRDecodingEnabled(true)
        qrScanner.setAutofocusInterval(2000L)
        qrScanner.setTorchEnabled(true)
        qrScanner.setFrontCamera()
        qrScanner.setBackCamera()
    }

    override fun onQRCodeRead(text: String?, points: Array<out PointF>?) {
        Toast.makeText(this, "Detectado con exito!", Snackbar.LENGTH_LONG).show()
        val result = Intent()
        result.putExtra("text_code", text)
        setResult(Activity.RESULT_OK, result)
        finish()
    }
}
