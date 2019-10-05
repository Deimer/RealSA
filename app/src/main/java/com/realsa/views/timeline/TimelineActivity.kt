package com.realsa.views.timeline

import android.annotation.SuppressLint
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
import androidx.core.content.FileProvider
import com.realsa.BuildConfig

@SuppressLint("CheckResult")
class TimelineActivity : BaseActivity() {

    private lateinit var timelineViewModel: TimelineViewModel

    private val permissionWrite: Int = 1
    private var dataList: MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timeline)
        setupView()
    }

    private fun setupView() {
        setupViewModel()
        initClickListeners()
        timelineViewModel.get()
    }

    private fun setupViewModel() {
        timelineViewModel = ViewModelProviders.of(this).get(TimelineViewModel::class.java)
        timelineViewModel.singleLiveEvent.observe(this, Observer {
            when(it) {
                is TimelineViewModel.ViewEvent.ResponseHistories -> {
                    setupRecyclerClients(it.histories)
                }
                is TimelineViewModel.ViewEvent.ResponseError -> {
                    showMessageBar(it.errorMessage)
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
            showLoading()
            generateFile()
        }
    }

    private fun generateFile() {
        val easyCsv = EasyCsv(this)
        val headerList = mutableListOf<String>()
        headerList.add("Fecha|Data|Celular|Latitud|Longitud¿")
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
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("dvilla@quqo.com"))
        intent.putExtra(Intent.EXTRA_SUBJECT, "Principal")
        intent.putExtra(Intent.EXTRA_BCC, arrayOf("contacto@ideamosweb.com"))
        intent.putExtra(Intent.EXTRA_TEXT, "Mi Reporte")
        intent.putExtra(Intent.EXTRA_STREAM, attached)
        startActivity(intent)
    }
}
