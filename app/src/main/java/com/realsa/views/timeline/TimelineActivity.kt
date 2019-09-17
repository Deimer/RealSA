package com.realsa.views.timeline

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

class TimelineActivity : BaseActivity() {

    private lateinit var timelineViewModel: TimelineViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timeline)
        setupView()
    }

    private fun setupView() {
        setupViewModel()
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
    }
}
