package com.realsa.views.timeline

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.realsa.R
import com.realsa.data.models.HistoryModel
import com.realsa.views.adapter.HistoryAdapterRecycler
import com.realsa.views.base.BaseActivity
import kotlinx.android.synthetic.main.content_history.*
import net.ozaydin.serkan.easy_csv.EasyCsv
import net.ozaydin.serkan.easy_csv.FileCallback
import java.io.File
import android.content.Intent
import android.view.LayoutInflater
import androidx.core.content.FileProvider
import com.realsa.BuildConfig
import kotlinx.android.synthetic.main.content_toolbar.*
import kotlinx.android.synthetic.main.dialog_base.view.*

@SuppressLint("CheckResult")
class TimelineActivity : BaseActivity() {

    private lateinit var timelineViewModel: TimelineViewModel

    private val permissionWrite: Int = 1
    private var dataList: MutableList<String> = mutableListOf()

    private var canClick: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timeline)
        setupView()
    }

    private fun setupView() {
        setupToolbar()
        setupViewModel()
        initClickListeners()
        timelineViewModel.get()
    }

    private fun setupToolbar() {
        butDelete.visibility = View.VISIBLE
        butDelete.setOnClickListener {
            showDialogDeleteHistory()
        }
    }

    private fun setupViewModel() {
        timelineViewModel = ViewModelProviders.of(this).get(TimelineViewModel::class.java)
        timelineViewModel.singleLiveEvent.observe(this, Observer {
            when(it) {
                is TimelineViewModel.ViewEvent.ResponseHistories -> {
                    canClick = true
                    setupRecyclerClients(it.histories.reversed())
                }
                is TimelineViewModel.ViewEvent.ResponseError -> {
                    showMessageBar(it.errorMessage)
                }
                is TimelineViewModel.ViewEvent.ResponseRemove -> {
                    hideLoading()
                    showMessageToast("Historial eliminado correctamente.")
                    finish()
                }
            }
        })
    }

    private fun setupRecyclerClients(histories: List<HistoryModel>) {
        val adapter = HistoryAdapterRecycler(histories, this)
        val staggeredGridLayoutManager = LinearLayoutManager(this)
        recyclerHistory.layoutManager = staggeredGridLayoutManager
        recyclerHistory.adapter = adapter
        recyclerHistory.visibility = View.VISIBLE
        if(histories.isNotEmpty()) generateListWithData(histories)
    }

    private fun generateListWithData(histories: List<HistoryModel>) {
        histories.forEach {
            val description = it.description
                ?.replace("\r", " ")
                ?.replace("\n", " ")
                ?.replace(",", " ")
            val value = "${it.createdAt}|${description}|${it.numberPhone?.validateNull()}|${it.latitude}|${it.longitude}¿"
            dataList.add(value)
        }
    }

    private fun initClickListeners() {
        fabCreateFile.setOnClickListener {
            if(canClick) {
                showLoading()
                generateFile()
            } else {
                showMessageToast("Aún no han cargado los datos.")
            }
        }
    }

    private fun generateFile() {
        val easyCsv = EasyCsv(this)
        val headerList = mutableListOf<String>()
        headerList.add("Fecha|Data|IMEI|Latitud|Longitud¿")
        easyCsv.setSeparatorColumn("|")
        easyCsv.setSeperatorLine("¿")
        easyCsv.createCsvFile("Mi Reporte", headerList, dataList, permissionWrite, object: FileCallback {
            override fun onSuccess(file: File?) {
                hideLoading()
                sendFileEmail(file)
            }
            override fun onFail(error: String?) {
                showMessageToast(error.toString())
                hideLoading()
            }
        })
    }

    private fun sendFileEmail(file: File?) {
        val attached = file?.let {
            FileProvider.getUriForFile(this, "${BuildConfig.APPLICATION_ID}.provider", it)
        }
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/html"
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("segreal@segreal.com"))
        intent.putExtra(Intent.EXTRA_SUBJECT, "Principal")
        intent.putExtra(Intent.EXTRA_BCC, arrayOf("contacto@ideamosweb.com"))
        intent.putExtra(Intent.EXTRA_TEXT, "Mi Reporte")
        intent.putExtra(Intent.EXTRA_STREAM, attached)
        startActivity(intent)
    }

    @SuppressLint("InflateParams")
    private fun showDialogDeleteHistory() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_base, null)
        val builder = AlertDialog.Builder(this).setView(dialogView).setTitle("")
        val alertDialog = builder.show()

        dialogView.butNot.setOnClickListener {
            alertDialog.dismiss()
        }
        dialogView.butOk.setOnClickListener {
            alertDialog.dismiss()
            showLoading()
            timelineViewModel.remove()
        }
    }
}
