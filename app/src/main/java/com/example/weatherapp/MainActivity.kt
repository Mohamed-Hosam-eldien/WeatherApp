package com.example.weatherapp

import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import androidx.activity.viewModels
import androidx.core.view.GravityCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.coroutineScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.weatherapp.data.network.WeatherInterface
import com.example.weatherapp.data.network.WeatherService
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.home.viewModel.HomeViewModel
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController : NavController
    private lateinit var appBarConfig : AppBarConfiguration

    private lateinit var listener : NavController.OnDestinationChangedListener


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        navController = findNavController(R.id.fragmentContainerView)
        binding.navView.setupWithNavController(navController)


        appBarConfig = AppBarConfiguration(navController.graph, binding.drawerLayout)

        binding.imgDrawer.setOnClickListener { binding.drawerLayout.openDrawer(GravityCompat.START) }

        //setupActionBarWithNavController(navController, appBarConfig)


        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            listener = NavController.OnDestinationChangedListener{controller, destination, arguments ->
                if(destination.id == R.id.home) {
                    supportActionBar?.setBackgroundDrawable(ColorDrawable(getColor(R.color.purple_500)))
                } else if(destination.id == R.id.favorite) {
                    supportActionBar?.setBackgroundDrawable(ColorDrawable(getColor(R.color.purple_700)))
                }
            }
        }*/

    }


    override fun onSupportNavigateUp(): Boolean {
        navController = findNavController(R.id.fragmentContainerView)
        return navController.navigateUp(appBarConfig) || super.onSupportNavigateUp()
    }


}