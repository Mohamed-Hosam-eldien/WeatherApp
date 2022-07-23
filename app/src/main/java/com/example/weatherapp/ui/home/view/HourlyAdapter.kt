package com.example.weatherapp.ui.home.view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.models.Hourly
import com.example.weatherapp.utils.Common
import io.paperdb.Paper
import java.text.SimpleDateFormat
import java.util.*

class HourlyAdapter(private val hourlyList: List<Hourly>?): RecyclerView.Adapter<HourlyAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view : View = LayoutInflater.from(parent.context)
            .inflate(R.layout.hourly_layout, parent, false)

        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val hourDetail = hourlyList?.get(position+1)

        holder.txtHour.text = convertFromUnixToTime(hourDetail?.dt?.toLong())
        holder.txtTemp.text = hourDetail?.temp?.toInt().toString()


        when(Paper.book().read<String>(Common.TempUnit)) {
            "metric" -> holder.txtUnit.text = "C"
            "standard" -> holder.txtUnit.text = "F"
            "imperial" -> holder.txtUnit.text = "I"
        }

        when(hourDetail?.weather?.get(0)?.icon) {
            "01d" -> {
                holder.imgHour.setImageResource(R.drawable.sunny)
                //binding.weatherLayoutBackground.setBackgroundResource(R.drawable.clear_sky_gr)
            }
            "01n" -> {
                holder.imgHour.setImageResource(R.drawable.moon)
                //binding.weatherLayoutBackground.setBackgroundResource(R.drawable.clear_sky_gr)
            }
            "02d" -> {
                holder.imgHour.setImageResource(R.drawable.few_clouds)
                //binding.weatherLayoutBackground.setBackgroundResource(R.drawable.few_clouds_gr)
            }
            "02n" -> {
                holder.imgHour.setImageResource(R.drawable.night_cloud)
                //binding.weatherLayoutBackground.setBackgroundResource(R.drawable.few_clouds_gr)
            }
            "03d" -> {
                holder.imgHour.setImageResource(R.drawable.scattered_clouds)
                //binding.weatherLayoutBackground.setBackgroundResource(R.drawable.scattered_clouds_gr)
            }
            "03n" -> {
                holder.imgHour.setImageResource(R.drawable.scattered_clouds)
                //binding.weatherLayoutBackground.setBackgroundResource(R.drawable.scattered_clouds_gr)
            }
            "04d" -> {
                holder.imgHour.setImageResource(R.drawable.broken_clouds)
                //binding.weatherLayoutBackground.setBackgroundResource(R.drawable.broken_clouds_gr)
            }
            "04n" -> {
                holder.imgHour.setImageResource(R.drawable.broken_clouds)
                //binding.weatherLayoutBackground.setBackgroundResource(R.drawable.broken_clouds_gr)
            }
            "09d" -> {
                holder.imgHour.setImageResource(R.drawable.shower_rain)
                //binding.weatherLayoutBackground.setBackgroundResource(R.drawable.rain_gr)
            }
            "09n" -> {
                holder.imgHour.setImageResource(R.drawable.shower_rain)
                //binding.weatherLayoutBackground.setBackgroundResource(R.drawable.rain_gr)
            }
            "10d" -> {
                holder.imgHour.setImageResource(R.drawable.heavy_rain)
                //binding.weatherLayoutBackground.setBackgroundResource(R.drawable.rain_gr)
            }
            "10n" -> {
                holder.imgHour.setImageResource(R.drawable.heavy_rain)
                //binding.weatherLayoutBackground.setBackgroundResource(R.drawable.rain_gr)
            }
            "11d" -> {
                holder.imgHour.setImageResource(R.drawable.thunder)
                //binding.weatherLayoutBackground.setBackgroundResource(R.drawable.thunder_gr)
            }
            "11n" -> {
                holder.imgHour.setImageResource(R.drawable.thunder)
                //binding.weatherLayoutBackground.setBackgroundResource(R.drawable.thunder_gr)
            }
            "13d" -> {
                holder.imgHour.setImageResource(R.drawable.snow)
                //binding.weatherLayoutBackground.setBackgroundResource(R.drawable.snow_gr)
            }
            "13n" -> {
                holder.imgHour.setImageResource(R.drawable.snow)
                //binding.weatherLayoutBackground.setBackgroundResource(R.drawable.snow_gr)
            }

        }
    }
    override fun getItemCount(): Int {
        return hourlyList?.size!!/2
    }

    @SuppressLint("SimpleDateFormat")
    fun convertFromUnixToTime(time: Long?): String {
        if(time != null) {

            val sdf: SimpleDateFormat = if(Paper.book().read<String>(Common.Language).toString() == "en")
                SimpleDateFormat("HH:mm",Locale.US)
            else
                SimpleDateFormat("HH:mm")

            val date = Date(time * 1000)
            return sdf.format(date)
        }
        return "00:00"
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtTemp:TextView = itemView.findViewById(R.id.txtHourlyTemp)
        val txtUnit:TextView = itemView.findViewById(R.id.txtHourlyUnit)
        val txtHour:TextView = itemView.findViewById(R.id.txtHour)
        val imgHour:ImageView = itemView.findViewById(R.id.imgHourly)
    }

}