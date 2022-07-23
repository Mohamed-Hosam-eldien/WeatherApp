package com.example.weatherapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.weatherapp.R
import com.example.weatherapp.databinding.ActivityMainBinding

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

//        binding.imgDrawer.setOnClickListener { binding.drawerLayout.openDrawer(GravityCompat.START) }

        setupActionBarWithNavController(navController, appBarConfig)


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