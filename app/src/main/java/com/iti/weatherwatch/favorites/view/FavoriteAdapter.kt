package com.iti.weatherwatch.favorites.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.iti.weatherwatch.R
import com.iti.weatherwatch.databinding.FavoriteCardBinding
import com.iti.weatherwatch.datasource.model.OpenWeatherApi
import com.iti.weatherwatch.favorites.viewmodel.FavoritesViewModel
import com.iti.weatherwatch.util.getCityText
import com.iti.weatherwatch.util.getSharedPreferences

class FavoriteAdapter(private val context: Context, private val viewModel: FavoritesViewModel) :
    RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {
    var favoriteList: List<OpenWeatherApi> = emptyList()

    class ViewHolder(val binding: FavoriteCardBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            FavoriteCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val favorite = favoriteList[position]
        val language = getSharedPreferences(context).getString(
            context.getString(R.string.languageSetting),
            "en"
        )!!
        holder.binding.textFavoriteCountry.text =
            getCityText(context, favorite.lat, favorite.lon, language)
        holder.binding.btnDelete.setOnClickListener {
            viewModel.deleteFavoriteWeather(favorite.id)
        }
        holder.binding.favoriteCardView.setOnClickListener {
            val bundle = bundleOf("id" to favorite.id, "lat" to favorite.lat, "lon" to favorite.lon)
            Navigation.findNavController(it)
                .navigate(R.id.action_navigation_dashboard_to_displayFavoriteWeather, bundle)
        }
    }

    override fun getItemCount(): Int {
        return favoriteList.size
    }
}
