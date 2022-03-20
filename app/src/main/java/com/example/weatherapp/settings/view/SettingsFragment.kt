package com.example.weatherapp.settings.view

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentSettingsBinding
import com.example.weatherapp.utils.Common
import io.paperdb.Paper

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        Paper.init(requireContext())

        binding = FragmentSettingsBinding.bind(inflater.inflate(R.layout.fragment_settings, container, false))

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.locationLayout.setOnClickListener{
            showLocationDialog()
        }

        binding.layoutUnit.setOnClickListener{
            showUnitsDialog()
        }

        binding.txtCurrentLocation.text = Paper.book().read(Common.Country)


        when {
            Paper.book().read<String>(Common.TempUnit).equals("metric") -> {
                binding.txtCurrentUnit.text = getString(R.string.celsius)
            }
            Paper.book().read<String>(Common.TempUnit).equals("standard") -> {
                binding.txtCurrentUnit.text = getString(R.string.fahrenheit)
            }
            Paper.book().read<String>(Common.TempUnit).equals("imperial") -> {
                binding.txtCurrentUnit.text = getString(R.string.imperial)
            }
        }


        if(Paper.book().read<String>(Common.WindSpeed).equals("metre/sec")) {
            binding.txtCurrentWindSpeed.text = getString(R.string.meter_sec)
        } else {
            binding.txtCurrentWindSpeed.text = getString(R.string.miles_hour)
        }

        if(Paper.book().read<String>(Common.Language).equals("ar")) {
            binding.txtCurrentLang.text = getString(R.string.Arabic)
        } else {
            binding.txtCurrentLang.text = getString(R.string.english)
        }


        binding.layoutWindSpeed.setOnClickListener{
            showWindSpeedDialog()
        }

        binding.layoutLang.setOnClickListener{
            showLanguageDialog()
        }

    }

    private fun showLanguageDialog() {
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.language_layout,null)
        val radioG:RadioGroup = view.findViewById(R.id.langRadioGroup)
        val dialog = Dialog(requireContext(), R.style.Theme_Dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        if(Paper.book().read<String>(Common.Language).equals("ar")) {
            radioG.check(R.id.rbArabic)
        } else {
            radioG.check(R.id.rbEnglish)
        }

        radioG.setOnCheckedChangeListener { group, checkedId ->
            run {
                when(checkedId) {
                    R.id.rbArabic -> {
                        Paper.book().write(Common.Language,"ar")
                        binding.txtCurrentLang.text = getString(R.string.Arabic)
                    }
                    R.id.rbEnglish -> {
                        Paper.book().write(Common.Language,"en")
                        binding.txtCurrentLang.text = getString(R.string.english)
                    }
                }
            }
        }

        dialog.setContentView(view)
        dialog.show()
    }

    private fun showWindSpeedDialog() {

        val view = LayoutInflater.from(requireContext()).inflate(R.layout.wind_speed_layout,null)
        val radioG:RadioGroup = view.findViewById(R.id.windRadioGroup)

        val dialog = Dialog(requireContext(), R.style.Theme_Dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        if(Paper.book().read<String>(Common.WindSpeed).equals("metre/sec")) {
            radioG.check(R.id.rbMeter)
        } else {
            radioG.check(R.id.rbMilles)
        }


        radioG.setOnCheckedChangeListener { group, checkedId ->
            run {
                when(checkedId) {
                    R.id.rbMeter -> {
                        Paper.book().write(Common.WindSpeed,"metre/sec")
                        binding.txtCurrentWindSpeed.text = getString(R.string.meter_sec)
                    }
                    R.id.rbMilles -> {
                        Paper.book().write(Common.WindSpeed,"miles/hour")
                        binding.txtCurrentWindSpeed.text = getString(R.string.miles_hour)
                    }
                }
            }
        }

        dialog.setContentView(view)
        dialog.show()
    }

    private fun showUnitsDialog() {

        val view = LayoutInflater.from(requireContext()).inflate(R.layout.temprature_layout,null)
        val radioG:RadioGroup = view.findViewById(R.id.tempRadioGroup)

        val dialog = Dialog(requireContext(), R.style.Theme_Dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        when {
            Paper.book().read<String>(Common.TempUnit).equals("metric") -> {
                radioG.check(R.id.rbCelsius)
            }
            Paper.book().read<String>(Common.TempUnit).equals("standard") -> {
                radioG.check(R.id.rbFahrenheit)
            }
            Paper.book().read<String>(Common.TempUnit).equals("imperial") -> {
                radioG.check(R.id.rbKelvin)
            }
        }


        radioG.setOnCheckedChangeListener { group, checkedId ->
            run {
                when(checkedId) {
                    R.id.rbCelsius -> {
                        Paper.book().write(Common.TempUnit,"metric")
                        binding.txtCurrentUnit.text = getString(R.string.celsius)
                    }
                    R.id.rbFahrenheit -> {
                        Paper.book().write(Common.TempUnit,"standard")
                        binding.txtCurrentUnit.text = getString(R.string.fahrenheit)
                    }
                    R.id.rbKelvin -> {
                        Paper.book().write(Common.TempUnit,"imperial")
                        binding.txtCurrentUnit.text = getString(R.string.imperial)
                    }
                }
            }
        }

        dialog.setContentView(view)
        dialog.show()
    }

    private fun showLocationDialog() {

    }

}