package com.iti.weatherwatch.favorites.view

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.iti.weatherwatch.R
import com.iti.weatherwatch.databinding.DisplayFavoriteWeatherFragmentBinding
import com.iti.weatherwatch.datasource.WeatherRepository
import com.iti.weatherwatch.favorites.viewmodel.DisplayFavoriteViewModelFactory
import com.iti.weatherwatch.favorites.viewmodel.DisplayFavoriteWeatherViewModel
import com.iti.weatherwatch.home.view.*
import com.iti.weatherwatch.model.*
import com.iti.weatherwatch.util.*
import java.util.*

class DisplayFavoriteWeather : Fragment() {
    private lateinit var tempPerDayAdapter: TempPerDayAdapter
    private lateinit var tempPerTimeAdapter: TempPerTimeAdapter
    private lateinit var windSpeedUnit: String
    private lateinit var temperatureUnit: String
    private val viewModel: DisplayFavoriteWeatherViewModel by viewModels {
        DisplayFavoriteViewModelFactory(WeatherRepository.getRepository(requireActivity().application))
    }

    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private var language: String = "en"
    private var units: String = "metric"

    private lateinit var binding: DisplayFavoriteWeatherFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DisplayFavoriteWeatherFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleBackButton()
        binding.btnBack.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
            Navigation.findNavController(it)
                .navigate(R.id.action_displayFavoriteWeather_to_navigation_dashboard)
        }

        //tempPerHourAdapter
        initTimeRecyclerView()

        //tempPerDayAdapter
        initDayRecyclerView()
        val id = requireArguments().getInt("id")
        if (isOnline(requireContext())) {
            getOnlineNeeds()
            viewModel.updateWeather(latitude, longitude, units, language, id)
        } else {
            viewModel.getWeather(id)
        }
        viewModel.weather.observe(viewLifecycleOwner) {
            setUnitSetting(units)
            it?.let { it1 -> setData(it1) }
            fetchTempPerTimeRecycler(it?.hourly as ArrayList<Hourly>, temperatureUnit)
            fetchTempPerDayRecycler(it.daily as ArrayList<Daily>, temperatureUnit)
        }
    }

    private fun getOnlineNeeds() {
        latitude = requireArguments().getDouble("lat")
        longitude = requireArguments().getDouble("lon")
        units = getSharedPreferences(requireContext()).getString(
            getString(R.string.unitsSetting),
            "metric"
        )!!
        language = getSharedPreferences(requireContext()).getString(
            getString(R.string.languageSetting),
            "en"
        )!!
    }

    private fun initTimeRecyclerView() {
        val tempPerTimeLinearLayoutManager = LinearLayoutManager(HomeFragment().context)
        tempPerTimeLinearLayoutManager.orientation = RecyclerView.HORIZONTAL
        tempPerTimeAdapter = TempPerTimeAdapter(this.requireContext())
        binding.recyclerViewTempPerTime.layoutManager = tempPerTimeLinearLayoutManager
        binding.recyclerViewTempPerTime.adapter = tempPerTimeAdapter
    }

    private fun initDayRecyclerView() {
        val tempPerDayLinearLayoutManager = LinearLayoutManager(HomeFragment().context)
        tempPerDayAdapter = TempPerDayAdapter(this.requireContext())
        binding.recyclerViewTempPerDay.layoutManager = tempPerDayLinearLayoutManager
        binding.recyclerViewTempPerDay.adapter = tempPerDayAdapter
    }

    private fun setUnitSetting(units: String) {
        when (units) {
            "metric"   -> {
                temperatureUnit = " °C"
                windSpeedUnit = " m/s"
            }
            "imperial" -> {
                temperatureUnit = " °F"
                windSpeedUnit = " miles/h"
            }
            "standard" -> {
                temperatureUnit = " °K"
                windSpeedUnit = " m/s"
            }
        }
    }

    private fun fetchTempPerDayRecycler(daily: ArrayList<Daily>, temperatureUnit: String) {
        tempPerDayAdapter.apply {
            this.daily = daily
            this.temperatureUnit = temperatureUnit
            notifyDataSetChanged()
        }
    }

    private fun fetchTempPerTimeRecycler(hourly: ArrayList<Hourly>, temperatureUnit: String) {
        tempPerTimeAdapter.apply {
            this.hourly = hourly
            this.temperatureUnit = temperatureUnit
            notifyDataSetChanged()
        }
    }

    private fun setData(model: OpenWeatherApi) {
        val weather = model.current.weather[0]
        binding.apply {
            imageWeatherIcon.setImageResource(getIcon(weather.icon))
            textCurrentDay.text = convertCalenderToDayString(Calendar.getInstance(), language)
            textCurrentDate.text =
                convertLongToDayDate(Calendar.getInstance().timeInMillis, language)
            textCurrentTempreture.text = model.current.temp.toString().plus(temperatureUnit)
            textTempDescription.text = weather.description
            textHumidity.text = model.current.humidity.toString().plus("%")
            textPressure.text = model.current.pressure.toString().plus(" hPa")
            textWindSpeed.text = model.current.windSpeed.toString().plus(windSpeedUnit)
            textCity.text = getCityText(requireContext(), model.lat, model.lon, language)
        }
//        binding.textCity.text = model.timezone
    }

    private fun handleBackButton() {
        binding.root.isFocusableInTouchMode = true
        binding.root.requestFocus()
        binding.root.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
                Navigation.findNavController(v)
                    .navigate(R.id.action_displayFavoriteWeather_to_navigation_dashboard)
                return@OnKeyListener true
            }
            return@OnKeyListener false
        })
    }

}
