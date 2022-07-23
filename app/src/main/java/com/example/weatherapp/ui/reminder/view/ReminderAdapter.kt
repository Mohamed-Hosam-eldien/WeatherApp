package com.example.weatherapp.ui.reminder.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.models.ReminderModel

class ReminderAdapter(
    private val onClick: (Int) -> Unit,
    private val remModels: List<ReminderModel>?
    ) : RecyclerView.Adapter<ReminderAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view : View = LayoutInflater.from(parent.context)
            .inflate(R.layout.rem_layot, parent, false)

        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val rem = remModels?.get(position)
        holder.txtFrom.text = "From : ${rem?.dateFrom}"
        holder.txtTo.text = "To : ${rem?.dateTo}"

        holder.imgDelete.setOnClickListener {
            onClick(rem?.id!!)
        }

    }

    override fun getItemCount(): Int {
        return remModels?.size!!
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtFrom:TextView = itemView.findViewById(R.id.txtFromDate)
        val txtTo:TextView = itemView.findViewById(R.id.txtToDate)
        val imgDelete:ImageView = itemView.findViewById(R.id.imgDelete)
    }

}