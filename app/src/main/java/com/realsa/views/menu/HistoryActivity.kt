package com.realsa.views.menu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.realsa.R
import com.realsa.data.models.HistoryModel
import com.realsa.views.adapter.HistoryAdapterRecycler
import kotlinx.android.synthetic.main.content_history.*

class HistoryActivity : AppCompatActivity() {

    private lateinit var historyViewModel: HistoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        setupView()
    }

    private fun setupView() {
        setupViewModel()
        historyViewModel.getHistories()
    }

    private fun setupViewModel() {
        historyViewModel = ViewModelProviders.of(this).get(HistoryViewModel::class.java)
        historyViewModel.singleLiveEvent.observe(this, Observer {
            when(it) {
                is HistoryViewModel.ViewEvent.ResponseHistories -> {
                    if(it.histories.isNotEmpty()) {
                        setupRecyclerClients(it.histories)
                    }
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
