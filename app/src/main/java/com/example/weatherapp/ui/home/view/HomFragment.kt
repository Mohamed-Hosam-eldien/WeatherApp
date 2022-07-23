package com.example.weatherapp.ui.home.view

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.R
import com.example.weatherapp.models.WeatherModel
import com.example.weatherapp.databinding.FragmentHomBinding
import com.example.weatherapp.utils.Common
import io.paperdb.Paper
import java.text.SimpleDateFormat
import java.util.*

class HomFragment : Fragment() {

    private lateinit var binding: FragmentHomBinding
    private var weather : WeatherModel? = Common.weather

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        Paper.init(requireActivity())
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_hom, container, false)
        binding = FragmentHomBinding.bind(view)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setCurrentData()

        setHourlyData()

        setDailyData()

    }

    private fun setDailyData() {
        binding.dailyRecycler.setHasFixedSize(true)
        binding.dailyRecycler.layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL, false)

        val adapter = DailyAdapter(weather?.daily)
        binding.dailyRecycler.adapter = adapter
    }

    override fun onResume() {
        super.onResume()

        weather = Common.weather

        setCurrentData()

        setHourlyData()

        setDailyData()

    }

    private fun setHourlyData() {
        binding.hourlyRecycler.setHasFixedSize(true)
        binding.hourlyRecycler.layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.HORIZONTAL, false)

        val adapter = HourlyAdapter(weather?.hourly)
        binding.hourlyRecycler.adapter = adapter
    }


    @SuppressLint("SetTextI18n")
    private fun setCurrentData() {
        binding.txtTemp.text = (weather?.current?.temp)?.toInt().toString()
        binding.txtWeatherState.text = weather?.current?.weather?.get(0)?.description

        binding.txtHumidity.text = "${weather?.current?.humidity.toString()} %"

        if(Paper.book().read<String>(Common.WindSpeed).toString() == "metre/sec")
            binding.txtWindSpeed.text = "${weather?.current?.wind_speed.toString()} m/s"
        else {
            val convert =(weather?.current?.wind_speed?.times(0.000621))

            var rounded = ""
            rounded = if(Paper.book().read<String>(Common.Language).toString() == "en")
                String.format(Locale.US,"%.3f", convert)
            else
                String.format("%.3f", convert)

            binding.txtWindSpeed.text = "$rounded m/h"
        }


        binding.txtPressure.text = "${weather?.current?.pressure.toString()} hpa"
        binding.txtClouds.text = "${weather?.current?.clouds.toString()} %"


        binding.txtGovernorate.text = Paper.book().read(Common.Country)

        when(Paper.book().read<String>(Common.TempUnit)) {
            "metric" -> binding.txtTempUnit.text = "C"
            "standard" -> binding.txtTempUnit.text = "F"
            "imperial" -> binding.txtTempUnit.text = "I"
        }

        setCurrentDate()

        when(weather?.current?.weather?.get(0)?.icon) {
            "01d" -> {
                binding.imgTemp.setImageResource(R.drawable.sunny)
                //binding.weatherLayoutBackground.setBackgroundResource(R.drawable.clear_sky_gr)
            }
            "01n" -> {
                binding.imgTemp.setImageResource(R.drawable.moon)
                //binding.weatherLayoutBackground.setBackgroundResource(R.drawable.clear_sky_gr)
            }
            "02d" -> {
                binding.imgTemp.setImageResource(R.drawable.few_clouds)
                //binding.weatherLayoutBackground.setBackgroundResource(R.drawable.few_clouds_gr)
            }
            "02n" -> {
                binding.imgTemp.setImageResource(R.drawable.night_cloud)
                //binding.weatherLayoutBackground.setBackgroundResource(R.drawable.few_clouds_gr)
            }
            "03d" -> {
                binding.imgTemp.setImageResource(R.drawable.scattered_clouds)
                //binding.weatherLayoutBackground.setBackgroundResource(R.drawable.scattered_clouds_gr)
            }
            "03n" -> {
                binding.imgTemp.setImageResource(R.drawable.scattered_clouds)
                //binding.weatherLayoutBackground.setBackgroundResource(R.drawable.scattered_clouds_gr)
            }
            "04d" -> {
                binding.imgTemp.setImageResource(R.drawable.broken_clouds)
                //binding.weatherLayoutBackground.setBackgroundResource(R.drawable.broken_clouds_gr)
            }
            "04n" -> {
                binding.imgTemp.setImageResource(R.drawable.broken_clouds)
                //binding.weatherLayoutBackground.setBackgroundResource(R.drawable.broken_clouds_gr)
            }
            "09d" -> {
                binding.imgTemp.setImageResource(R.drawable.shower_rain)
                //binding.weatherLayoutBackground.setBackgroundResource(R.drawable.rain_gr)
            }
            "09n" -> {
                binding.imgTemp.setImageResource(R.drawable.shower_rain)
                //binding.weatherLayoutBackground.setBackgroundResource(R.drawable.rain_gr)
            }
            "10d" -> {
                binding.imgTemp.setImageResource(R.drawable.heavy_rain)
                //binding.weatherLayoutBackground.setBackgroundResource(R.drawable.rain_gr)
            }
            "10n" -> {
                binding.imgTemp.setImageResource(R.drawable.heavy_rain)
                //binding.weatherLayoutBackground.setBackgroundResource(R.drawable.rain_gr)
            }
            "11d" -> {
                binding.imgTemp.setImageResource(R.drawable.thunder)
                //binding.weatherLayoutBackground.setBackgroundResource(R.drawable.thunder_gr)
            }
            "11n" -> {
                binding.imgTemp.setImageResource(R.drawable.thunder)
                //binding.weatherLayoutBackground.setBackgroundResource(R.drawable.thunder_gr)
            }
            "13d" -> {
                binding.imgTemp.setImageResource(R.drawable.snow)
                //binding.weatherLayoutBackground.setBackgroundResource(R.drawable.snow_gr)
            }
            "13n" -> {
                binding.imgTemp.setImageResource(R.drawable.snow)
                //binding.weatherLayoutBackground.setBackgroundResource(R.drawable.snow_gr)
            }

        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun setCurrentDate() {
        val sdf: SimpleDateFormat = if(Paper.book().read<String>(Common.Language).toString() == "en")
            SimpleDateFormat("EEEE dd/M",Locale.US)
        else
            SimpleDateFormat("EEEE dd/M")

        val currentDate = sdf.format(Date())
        binding.txtCurrentDate.text = currentDate
    }

}