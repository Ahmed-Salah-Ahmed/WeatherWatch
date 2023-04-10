package com.iti.weatherwatch.home.view

import android.Manifest
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.core.app.ActivityCompat
import androidx.fragment.app.*
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.iti.weatherwatch.R
import com.iti.weatherwatch.broadcastreceiver.ConnectivityReceiver
import com.iti.weatherwatch.databinding.FragmentHomeBinding
import com.iti.weatherwatch.datasource.MyLocationProvider
import com.iti.weatherwatch.datasource.WeatherRepository
import com.iti.weatherwatch.datasource.model.*
import com.iti.weatherwatch.home.viewmodel.HomeViewModel
import com.iti.weatherwatch.home.viewmodel.HomeViewModelFactory
import com.iti.weatherwatch.util.*
import java.util.*

/*
This class is the implementation of the HomeFragment in the WeatherWatch app. It is a subclass of the Fragment class and is responsible for displaying the main view of the app, including the weather forecast for the user's current location, as well as handling various events, such as network connectivity changes and user interactions.

The class also includes the onCreateView(), onResume(), onViewCreated() methods, which are lifecycle methods of a fragment in Android. In the onCreateView() method, the fragment's layout is inflated, and the view is returned.

In the onResume() method, the ConnectivityReceiverListener is set to the class instance to listen to changes in network connectivity.

In the onViewCreated() method, various views and adapters are initialized, and the current location is obtained to display weather information. This method also observes changes in location and weather data and updates the UI accordingly.

This class has a number of properties and methods for initializing and interacting with views and data. Some of the notable properties include:

    tempPerDayAdapter and tempPerTimeAdapter are instances of TempPerDayAdapter and TempPerTimeAdapter, respectively, which are adapters used to display data in RecyclerViews.
    windSpeedUnit and temperatureUnit are strings representing the current wind speed and temperature units of the weather data being displayed.
    latitude, longitude, language, and units are properties that hold data relevant to the current location and weather data being displayed.
    flagNoConnection is a boolean flag that indicates whether there is currently a network connection.
    viewModel is an instance of HomeViewModel, which is responsible for managing the data displayed in the fragment view.

Overall, this class represents the functionality of the home screen of a weather app and includes methods for displaying weather data and handling changes in network connectivity.
 */
class HomeFragment : Fragment(), ConnectivityReceiver.ConnectivityReceiverListener {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var tempPerDayAdapter: TempPerDayAdapter
    private lateinit var tempPerTimeAdapter: TempPerTimeAdapter
    private lateinit var windSpeedUnit: String
    private lateinit var temperatureUnit: String
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private var language: String = "en"
    private var units: String = "metric"
    private var flagNoConnection: Boolean = false

    private val viewModel: HomeViewModel by viewModels {
        HomeViewModelFactory(
            WeatherRepository.getRepository(requireActivity().application),
            MyLocationProvider(this)
        )
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
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        ConnectivityReceiver.connectivityReceiverListener = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireContext().registerReceiver(
            ConnectivityReceiver(),
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )

        if (!flagNoConnection) {
            if (isSharedPreferencesLocationAndTimeZoneNull(requireContext())) {
                if (!isSharedPreferencesLatAndLongNull(requireContext())) {
                    setValuesFromSharedPreferences()
                    try {
                        viewModel.getDataFromRemoteToLocal(
                            "$latitude",
                            "$longitude",
                            language,
                            units
                        )
                    } catch (e: Exception) {
                        val snackBar =
                            Snackbar.make(binding.root, "${e.message}", Snackbar.LENGTH_SHORT)
                        snackBar.show()
                    }
                } else if (getIsMap()) {
                    Navigation.findNavController(view)
                        .navigate(R.id.action_navigation_home_to_mapsFragment)
                } else {
                    //dialog to get fresh location
                    val location = MyLocationProvider(this)
                    if (location.checkPermission() && location.isLocationEnabled()) {
                        viewModel.getFreshLocation()
                    } else {
                        binding.homeView.visibility = View.GONE
                        binding.cardLocation.visibility = View.VISIBLE
                        if (!location.checkPermission()) {
                            binding.textDialog.text = getString(R.string.location_permission)
                        } else if (!location.isLocationEnabled()) {
                            binding.textDialog.text = getString(R.string.location_enabled)
                        }

                        binding.btnEnable.setOnClickListener {
                            if (ActivityCompat.shouldShowRequestPermissionRationale(
                                    requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION
                                )) {
                                viewModel.getFreshLocation()
                            } else {
                                val intent = Intent(
                                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                    Uri.parse("package:" + requireContext().applicationContext.packageName)
                                )
                                startActivity(intent)
                            }
                        }
                    }
                }
            } else {
                setValuesFromSharedPreferences()
                try {
                    viewModel.getDataFromRemoteToLocal(
                        "$latitude",
                        "$longitude",
                        language,
                        units
                    )
                } catch (e: Exception) {
                    val snackBar =
                        Snackbar.make(binding.root, "${e.message}", Snackbar.LENGTH_SHORT)
                    snackBar.show()
                }
            }
        }

        //tempPerHourAdapter
        initTimeRecyclerView()

        //tempPerDayAdapter
        initDayRecyclerView()

        initSwipeRefresh()

        viewModel.observeLocation().observe(viewLifecycleOwner) {
            binding.homeView.visibility = View.VISIBLE
            binding.cardLocation.visibility = View.GONE
            if (it[0] != 0.0 && it[1] != 0.0) {
                latitude = it[0]
                longitude = it[1]
                val local = getCurrentLocale(requireContext())
                language = getSharedPreferences(requireContext()).getString(
                    getString(R.string.languageSetting), local?.language
                )!!
                units = getSharedPreferences(requireContext()).getString(
                    getString(R.string.unitsSetting),
                    "metric"
                )!!
                try {
                    viewModel.getDataFromRemoteToLocal(
                        "$latitude",
                        "$longitude",
                        language,
                        units
                    )
                } catch (e: Exception) {
                    val snackBar =
                        Snackbar.make(binding.root, "${e.message}", Snackbar.LENGTH_SHORT)
                    snackBar.show()
                }
            }
        }

        viewModel.openWeatherAPI.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.swiperefresh.isRefreshing = false
                updateSharedPreferences(
                    requireContext(),
                    it.lat,
                    it.lon,
                    getCityText(requireContext(), it.lat, it.lon, language),
                    it.timezone
                )
                setUnitSetting(units)
                setData(it)
                fetchTempPerTimeRecycler(it.hourly as ArrayList<Hourly>, temperatureUnit)
                fetchTempPerDayRecycler(it.daily as ArrayList<Daily>, temperatureUnit)
            }
        }
    }

    private fun initSwipeRefresh() {
        binding.swiperefresh.setOnRefreshListener {
            if (getIsMap()) {
                if (!isSharedPreferencesLatAndLongNull(requireContext())) {
                    binding.swiperefresh.isRefreshing = true
                    setValuesFromSharedPreferences()
                    try {
                        viewModel.getDataFromRemoteToLocal(
                            "$latitude",
                            "$longitude",
                            language,
                            units
                        )
                    } catch (e: Exception) {
                        val snackBar =
                            Snackbar.make(binding.root, "${e.message}", Snackbar.LENGTH_SHORT)
                        snackBar.show()
                    }
                }
            } else {
                binding.swiperefresh.isRefreshing = true
                viewModel.getFreshLocation()
            }
        }
    }

    private fun getIsMap(): Boolean {
        return getSharedPreferences(requireContext()).getBoolean(getString(R.string.isMap), false)
    }

    private fun setUnitSetting(units: String) {
        if (language == "en") {
            setEnglishUnits(units)
        } else {
            setArabicUnit(units)
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
            textTempDescription.text = weather.description
            textCity.text = getCityText(requireContext(), model.lat, model.lon, language)
            if (language == "ar") {
                bindArabicUnits(model)
            } else {
                bindEnglishUnits(model)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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

    private fun setValuesFromSharedPreferences() {
        getSharedPreferences(requireContext()).apply {
            latitude = getFloat(getString(R.string.lat), 0.0f).toDouble()
            longitude = getFloat(getString(R.string.lon), 0.0f).toDouble()
            language = getString(getString(R.string.languageSetting), "en")!!
            units = getString(getString(R.string.unitsSetting), "metric")!!
        }
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        if (_binding != null) {
            if (isConnected) {
                if (flagNoConnection) {
                    val snackBar = Snackbar.make(binding.root, "Back Online", Snackbar.LENGTH_SHORT)
                    snackBar.view.setBackgroundColor(Color.GREEN)
                    snackBar.show()
                    flagNoConnection = false
                    refreshFragment()
                }
            } else {
                flagNoConnection = true
                val snackBar = Snackbar.make(binding.root, "You are offline", Snackbar.LENGTH_LONG)
                snackBar.view.setBackgroundColor(Color.RED)
                snackBar.show()
                getLocalData()
            }
        }
    }

    private fun getLocalData() {
        if (!isSharedPreferencesLocationAndTimeZoneNull(requireContext())) {
            viewModel.getDataFromDatabase()
        }
    }

    private fun refreshFragment() {
        val ft: FragmentTransaction = requireFragmentManager().beginTransaction()
        ft.setReorderingAllowed(false)
        ft.detach(this).attach(this).commit()
    }

    private fun setArabicUnit(units: String) {
        when (units) {
            "metric"   -> {
                temperatureUnit = " °م"
                windSpeedUnit = " م/ث"
            }
            "imperial" -> {
                temperatureUnit = " °ف"
                windSpeedUnit = " ميل/س"
            }
            "standard" -> {
                temperatureUnit = " °ك"
                windSpeedUnit = " م/ث"
            }
        }
    }

    private fun setEnglishUnits(units: String) {
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

    private fun bindArabicUnits(model: OpenWeatherApi) {
        binding.apply {
            textCurrentTempreture.text =
                convertNumbersToArabic(model.current.temp.toInt()).plus(temperatureUnit)
            textHumidity.text = convertNumbersToArabic(model.current.humidity)
                .plus("٪")
            textPressure.text = convertNumbersToArabic(model.current.pressure)
                .plus(" هب")
            textClouds.text = convertNumbersToArabic(model.current.clouds)
                .plus("٪")
            textVisibility.text = convertNumbersToArabic(model.current.visibility)
                .plus("م")
            textUvi.text = convertNumbersToArabic(model.current.uvi.toInt())
            textWindSpeed.text =
                convertNumbersToArabic(model.current.windSpeed).plus(windSpeedUnit)
        }
    }

    private fun bindEnglishUnits(model: OpenWeatherApi) {
        binding.apply {
            textCurrentTempreture.text = model.current.temp.toInt().toString().plus(temperatureUnit)
            textHumidity.text = model.current.humidity.toString().plus("%")
            textPressure.text = model.current.pressure.toString().plus(" hPa")
            textClouds.text = model.current.clouds.toString().plus("%")
            textVisibility.text = model.current.visibility.toString().plus("m")
            textUvi.text = model.current.uvi.toString()
            textWindSpeed.text = model.current.windSpeed.toString().plus(windSpeedUnit)
        }
    }
}
