package com.realsa.views.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.realsa.R
import com.realsa.data.models.HistoryModel
import kotlinx.android.synthetic.main.card_item_history.view.*

class HistoryAdapterRecycler(
    private val histories: List<HistoryModel>,
    private val context: Context
): RecyclerView.Adapter<ViewHolderHistory>() {

    override fun getItemCount(): Int {
        return histories.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderHistory {
        return ViewHolderHistory(LayoutInflater.from(context).inflate(R.layout.card_item_history, parent, false))
    }

    @SuppressLint("SetTextI18n", "DefaultLocale")
    override fun onBindViewHolder(holder: ViewHolderHistory, position: Int) {
        val history = histories[position]
        holder.lblDescription.text = "${history.description}".toLowerCase().capitalize()
        holder.lblLocation.text = "Coordenadas: ${history.latitude}, ${history.longitude}"
        holder.lblDateTime.text = "Fecha: ${history.createdAt}"
    }
}

class ViewHolderHistory(view: View): RecyclerView.ViewHolder(view) {
    val lblDescription: TextView = view.lblDescription
    val lblLocation: TextView = view.lblCoordinates
    val lblDateTime: TextView = view.lblDateTime
}