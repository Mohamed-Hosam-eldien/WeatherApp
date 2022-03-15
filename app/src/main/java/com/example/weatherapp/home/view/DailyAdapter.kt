package com.example.weatherapp.home.view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.data.model.Daily
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
            holder.txtDay.text = holder.imgDaily.context.getString(R.string.tomorrow)
        } else {
            holder.txtDay.text = convertFromUnixToTime(dailyDetail?.dt?.toLong())

        }

        holder.txtDescription.text = dailyDetail?.weather?.get(0)?.description

        holder.txtTemp.text = StringBuilder().append(dailyDetail?.temp?.night?.toInt().toString())
            .append("/")
            .append(dailyDetail?.temp?.morn?.toInt().toString())


        when(dailyDetail?.weather?.get(0)?.icon) {
            "01d" -> {
                holder.imgDaily.setImageResource(R.drawable.sunny)
                //binding.weatherLayoutBackground.setBackgroundResource(R.drawable.clear_sky_gr)
            }
            "01n" -> {
                holder.imgDaily.setImageResource(R.drawable.moon)
                //binding.weatherLayoutBackground.setBackgroundResource(R.drawable.clear_sky_gr)
            }
            "02d" -> {
                holder.imgDaily.setImageResource(R.drawable.few_clouds)
                //binding.weatherLayoutBackground.setBackgroundResource(R.drawable.few_clouds_gr)
            }
            "02n" -> {
                holder.imgDaily.setImageResource(R.drawable.night_cloud)
                //binding.weatherLayoutBackground.setBackgroundResource(R.drawable.few_clouds_gr)
            }
            "03d" -> {
                holder.imgDaily.setImageResource(R.drawable.scattered_clouds)
                //binding.weatherLayoutBackground.setBackgroundResource(R.drawable.scattered_clouds_gr)
            }
            "03n" -> {
                holder.imgDaily.setImageResource(R.drawable.scattered_clouds)
                //binding.weatherLayoutBackground.setBackgroundResource(R.drawable.scattered_clouds_gr)
            }
            "04d" -> {
                holder.imgDaily.setImageResource(R.drawable.broken_clouds)
                //binding.weatherLayoutBackground.setBackgroundResource(R.drawable.broken_clouds_gr)
            }
            "04n" -> {
                holder.imgDaily.setImageResource(R.drawable.broken_clouds)
                //binding.weatherLayoutBackground.setBackgroundResource(R.drawable.broken_clouds_gr)
            }
            "09d" -> {
                holder.imgDaily.setImageResource(R.drawable.shower_rain)
                //binding.weatherLayoutBackground.setBackgroundResource(R.drawable.rain_gr)
            }
            "09n" -> {
                holder.imgDaily.setImageResource(R.drawable.shower_rain)
                //binding.weatherLayoutBackground.setBackgroundResource(R.drawable.rain_gr)
            }
            "10d" -> {
                holder.imgDaily.setImageResource(R.drawable.heavy_rain)
                //binding.weatherLayoutBackground.setBackgroundResource(R.drawable.rain_gr)
            }
            "10n" -> {
                holder.imgDaily.setImageResource(R.drawable.heavy_rain)
                //binding.weatherLayoutBackground.setBackgroundResource(R.drawable.rain_gr)
            }
            "11d" -> {
                holder.imgDaily.setImageResource(R.drawable.thunder)
                //binding.weatherLayoutBackground.setBackgroundResource(R.drawable.thunder_gr)
            }
            "11n" -> {
                holder.imgDaily.setImageResource(R.drawable.thunder)
                //binding.weatherLayoutBackground.setBackgroundResource(R.drawable.thunder_gr)
            }
            "13d" -> {
                holder.imgDaily.setImageResource(R.drawable.snow)
                //binding.weatherLayoutBackground.setBackgroundResource(R.drawable.snow_gr)
            }
            "13n" -> {
                holder.imgDaily.setImageResource(R.drawable.snow)
                //binding.weatherLayoutBackground.setBackgroundResource(R.drawable.snow_gr)
            }

        }
    }
    override fun getItemCount(): Int {
        return dailyList?.size!! -1
    }

    @SuppressLint("SimpleDateFormat")
    fun convertFromUnixToTime(time: Long?): String {
        if(time != null) {
            val sdf = SimpleDateFormat("EEEE")
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