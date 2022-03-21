package com.example.weatherapp.favorite.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.data.local.FavModel

class FavoriteAdapter(
    private val onClick: (Int) -> Unit,
    private val sendLocation: (FavModel) -> Unit,
    private val favModels: List<FavModel>?
    ) : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view : View = LayoutInflater.from(parent.context)
            .inflate(R.layout.fav_layot, parent, false)

        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val fav = favModels?.get(position)
        holder.txtCountry.text = fav?.countryName
        holder.txtLoyality.text = fav?.localityName

        holder.imgDelete.setOnClickListener {
            onClick(fav?.id!!)
        }

        holder.favLayout.setOnClickListener {
            // send location to api
            sendLocation(fav!!)
        }
    }

    override fun getItemCount(): Int {
        return favModels?.size!!
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtCountry:TextView = itemView.findViewById(R.id.txtFavCountry)
        val txtLoyality:TextView = itemView.findViewById(R.id.txtFavLoyality)
        val imgDelete:ImageView = itemView.findViewById(R.id.imgDelete)
        val favLayout:RelativeLayout = itemView.findViewById(R.id.favLayout)
    }

}