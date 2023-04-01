package com.iti.weatherwatch.home.view

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.iti.weatherwatch.R
import com.iti.weatherwatch.databinding.FragmentHomeBinding
import com.iti.weatherwatch.datasource.WeatherRepository
import com.iti.weatherwatch.home.viewmodel.HomeViewModel
import com.iti.weatherwatch.model.*
import com.iti.weatherwatch.util.*
import com.iti.weatherwatch.viewmodel.HomeViewModelFactory
import java.util.*

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var tempPerDayAdapter: TempPerDayAdapter
    private lateinit var tempPerTimeAdapter: TempPerTimeAdapter
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    private val viewModel: HomeViewModel by viewModels {
        HomeViewModelFactory(WeatherRepository.getRepository(requireActivity().application))
    }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
//        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (isOnline(requireContext())) {
            if (isSharedPreferencesLocationAndTimeZoneNull(requireContext())) {
                if (!isSharedPreferencesLatAndLongNull(requireContext())) {
                    latitude =
                        getSharedPreferences(requireContext()).getFloat(
                            getString(R.string.lat),
                            0.0f
                        ).toDouble()
                    longitude =
                        getSharedPreferences(requireContext()).getFloat(
                            getString(R.string.lon),
                            0.0f
                        ).toDouble()
                    viewModel.insertData("$latitude", "$longitude")
//                    viewModel.getDataFromDatabase(result.timezone)
//                    updateSharedPreferences(
//                        requireContext(),
//                        result.lat!!,
//                        result.lon!!,
//                getCityText(result.lat!!, result.lon!!),
////                        result.timezone,
//                        result.timezone
//                    )
                }
            } else {
                latitude =
                    getSharedPreferences(requireContext()).getFloat(
                        getString(R.string.lat),
                        0.0f
                    ).toDouble()
                longitude =
                    getSharedPreferences(requireContext()).getFloat(
                        getString(R.string.lon),
                        0.0f
                    ).toDouble()
                Log.i("ziny", "onViewCreated: $latitude, $longitude")
                viewModel.updateData("$latitude", "$longitude")
            }
        } else {
            if (!isSharedPreferencesLocationAndTimeZoneNull(requireContext())) {
                viewModel.getDataFromDatabase(
                    getSharedPreferences(requireContext()).getString(
                        getString(R.string.timeZone),
                        ""
                    ) ?: ""
                )
            }
        }

        viewModel.openWeatherAPI.observe(viewLifecycleOwner) {
            updateSharedPreferences(
                requireContext(),
                it.lat,
                it.lon,
                getCityText(it.lat, it.lon),
                //                it.timezone,
                it.timezone
            )
            setData(it)
            fetchTempPerTimeRecycler(it.hourly as ArrayList<Hourly>)
            fetchTempPerDayRecycler(it.daily as ArrayList<Daily>)
        }

        //tempPerTimeAdapter
        val tempPerTimeLinearLayoutManager = LinearLayoutManager(HomeFragment().context)
        tempPerTimeLinearLayoutManager.orientation = RecyclerView.HORIZONTAL
        tempPerTimeAdapter = TempPerTimeAdapter(this.requireContext())
        binding.recyclerViewTempPerTime.layoutManager = tempPerTimeLinearLayoutManager
        binding.recyclerViewTempPerTime.adapter = tempPerTimeAdapter

        //tempPerDayAdapter
        val tempPerDayLinearLayoutManager = LinearLayoutManager(HomeFragment().context)
        tempPerDayAdapter = TempPerDayAdapter(this.requireContext())
        binding.recyclerViewTempPerDay.layoutManager = tempPerDayLinearLayoutManager
        binding.recyclerViewTempPerDay.adapter = tempPerDayAdapter

//        viewModel.text.observe(viewLifecycleOwner) {
//            binding.textHome.text = it
//        }
    }

    private fun fetchTempPerDayRecycler(daily: ArrayList<Daily>) {
        tempPerDayAdapter.daily = daily
        tempPerDayAdapter.notifyDataSetChanged()
    }

    private fun fetchTempPerTimeRecycler(hourly: ArrayList<Hourly>) {
        tempPerTimeAdapter.hourly = hourly
        tempPerTimeAdapter.notifyDataSetChanged()
    }

    private fun setData(model: OpenWeatherApi) {
        val weather = model.current.weather[0]
        binding.imageWeatherIcon.setImageResource(getIcon(weather.icon))
        binding.textCurrentDay.text = convertCalenderToDayString(Calendar.getInstance())
        binding.textCurrentDate.text = convertCalenderToDayDate(Calendar.getInstance())
        binding.textCurrentTempreture.text = model.current.temp.toString()
        binding.textTempDescription.text = weather.description
        binding.textHumidity.text = model.current.humidity.toString()
        binding.textPressure.text = model.current.pressure.toString()
        binding.textWindSpeed.text = model.current.windSpeed.toString()
        binding.textCity.text = getCityText(model.lat, model.lon)
//        binding.textCity.text = model.timezone
    }

    private fun getCityText(lat: Double, lon: Double): String {
        var city = ""
        val geocoder = Geocoder(requireContext(), Locale("en"))
        val addresses: List<Address>? = geocoder.getFromLocation(lat, lon, 1)
        Log.i("ziny", "getCityText: $lat + $lon + $addresses")
        if (addresses!!.isNotEmpty()) {
            val state = addresses[0].adminArea // damietta
            val country = addresses[0].countryName
            city = "$state, $country"
        }
//        val knownName = addresses[0].featureName // elglaa
        return city
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
