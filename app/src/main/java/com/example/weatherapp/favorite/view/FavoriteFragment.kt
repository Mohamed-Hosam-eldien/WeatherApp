package com.example.weatherapp.favorite.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.R
import com.example.weatherapp.data.local.FavModel
import com.example.weatherapp.databinding.FragmentFavoriteBinding
import com.example.weatherapp.favorite.viewModel.FavViewModelFactory
import com.example.weatherapp.favorite.viewModel.FavoriteViewModel
import com.example.weatherapp.utils.Common
import io.paperdb.Paper
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FavoriteFragment : Fragment() {

    private lateinit var binding : FragmentFavoriteBinding
    private var countryName: String? = null

    private val viewModel: FavoriteViewModel by viewModels {
        FavViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        Paper.init(requireContext())
        val view: View = inflater.inflate(R.layout.fragment_favorite, container, false)
        binding = FragmentFavoriteBinding.bind(view)

        viewModel.initDatabase()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.favRecycler.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        binding.favRecycler.setHasFixedSize(true)

        binding.btnFab.setOnClickListener {
            val intent = Intent(requireContext(), MapsActivity::class.java)
            startActivity(intent)
        }

        getAllFavData()

    }

    private fun getAllFavData() {
        viewModel.getFav().observe(requireActivity()) {
            val lambdaDelete = { id:Int -> deleteFromFav(id) }
            val lambdaOpenWeatherData = { model:FavModel -> getWeatherData(model) }
            val favAdapter = FavoriteAdapter(lambdaDelete,lambdaOpenWeatherData,it)
            binding.favRecycler.adapter = favAdapter
        }
    }

    private fun getWeatherData(model: FavModel){

        val intent = Intent(requireActivity(),LocationDetails::class.java)

        intent.putExtra("Country",model.countryName)
        intent.putExtra("Lat",model.latitude)
        intent.putExtra("Lon",model.longitude)

        startActivity(intent)
        Log.d("TAG", "getWeatherData BY Before:" )
    }

    private fun deleteFromFav(id: Int) {
        lifecycleScope.launch{viewModel
            viewModel.deleteFromFav(id)
        }
    }

}