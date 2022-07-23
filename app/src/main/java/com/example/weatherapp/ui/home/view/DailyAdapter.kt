package com.example.weatherapp.ui.home.view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.models.Daily
import com.example.weatherapp.utils.Common
import io.paperdb.Paper
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*

class DailyAdapter(private val dailyList: List<Daily>?): RecyclerView.Adapter<DailyAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view : View = LayoutInflater.from(parent.context)
            .inflate(R.layout.daily_layot, parent, false)

        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val dailyDetail = dailyList?.get(position+1)

        if(position == 0) {

            if(Paper.book().read<String>(Common.Language).toString() == "en")
                holder.txtDay.text = "Tomorrow"
            else
                holder.txtDay.text = "غداً"

        } else {
            holder.txtDay.text = convertFromUnixToTime(dailyDetail?.dt?.toLong())
        }

        holder.txtDescription.text = dailyDetail?.weather?.get(0)?.description

        var unit = ""
        when(Paper.book().read<String>(Common.TempUnit)) {
            "metric" -> unit = "C"
            "standard" -> unit = "F"
            "imperial" -> unit = "I"
        }

        holder.txtTemp.text = StringBuilder().append(dailyDetail?.temp?.night?.toInt().toString())
            .append("/")
            .append(dailyDetail?.temp?.morn?.toInt().toString())
            .append(" ")
            .append(unit)


        when(dailyDetail?.weather?.get(0)?.icon) {
            "01d" -> {
                holder.imgDaily.setImageResource(R.drawable.sunny)
            }
            "01n" -> {
                holder.imgDaily.setImageResource(R.drawable.moon)
            }
            "02d" -> {
                holder.imgDaily.setImageResource(R.drawable.few_clouds)
            }
            "02n" -> {
                holder.imgDaily.setImageResource(R.drawable.night_cloud)
            }
            "03d" -> {
                holder.imgDaily.setImageResource(R.drawable.scattered_clouds)
            }
            "03n" -> {
                holder.imgDaily.setImageResource(R.drawable.scattered_clouds)
            }
            "04d" -> {
                holder.imgDaily.setImageResource(R.drawable.broken_clouds)
            }
            "04n" -> {
                holder.imgDaily.setImageResource(R.drawable.broken_clouds)
            }
            "09d" -> {
                holder.imgDaily.setImageResource(R.drawable.shower_rain)
            }
            "09n" -> {
                holder.imgDaily.setImageResource(R.drawable.shower_rain)
            }
            "10d" -> {
                holder.imgDaily.setImageResource(R.drawable.heavy_rain)
            }
            "10n" -> {
                holder.imgDaily.setImageResource(R.drawable.heavy_rain)
            }
            "11d" -> {
                holder.imgDaily.setImageResource(R.drawable.thunder)
            }
            "11n" -> {
                holder.imgDaily.setImageResource(R.drawable.thunder)
            }
            "13d" -> {
                holder.imgDaily.setImageResource(R.drawable.snow)
            }
            "13n" -> {
                holder.imgDaily.setImageResource(R.drawable.snow)
            }

        }
    }
    override fun getItemCount(): Int {
        return dailyList?.size!! -1
    }

    @SuppressLint("SimpleDateFormat")
    fun convertFromUnixToTime(time: Long?): String {
        if(time != null) {

            val sdf: SimpleDateFormat = if(Paper.book().read<String>(Common.Language).toString() == "en")
                SimpleDateFormat("EEEE", Locale.US)
            else
                SimpleDateFormat("EEEE")

            val date = Date(time * 1000)
            return sdf.format(date)
        }
        return "not available"
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtTemp:TextView = itemView.findViewById(R.id.txtTemp)
        val txtDay:TextView = itemView.findViewById(R.id.txtDay)
        val txtDescription:TextView = itemView.findViewById(R.id.txtDailyDescription)
        val imgDaily:ImageView = itemView.findViewById(R.id.imgDaily)
    }

}