package com.iti.weatherwatch.favorites.view

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.iti.weatherwatch.R
import com.iti.weatherwatch.databinding.FragmentFavoritesBinding
import com.iti.weatherwatch.datasource.WeatherRepository
import com.iti.weatherwatch.datasource.model.OpenWeatherApi
import com.iti.weatherwatch.favorites.viewmodel.FavoritesViewModel
import com.iti.weatherwatch.favorites.viewmodel.FavoritesViewModelFactory

/*
The FavoritesFragment class represents a fragment that displays a list of favorite locations with their current weather information. The class inflates the FragmentFavoritesBinding layout and initializes the FavoritesViewModel using FavoritesViewModelFactory. It also initializes a FavoriteAdapter instance, which is used to populate the list of favorite locations.

The onViewCreated method of the class is responsible for initializing the RecyclerView, fetching the list of favorite locations using viewModel.getFavorites(), and observing changes to the list using the favorites property of the FavoritesViewModel. The floatingActionButton on the screen is used to navigate to the MapsFragment to add a new favorite location.

The fetchFavoritesRecycler method is responsible for updating the FavoriteAdapter with the latest list of favorite locations, and the initFavoritesRecyclerView method is used to initialize the RecyclerView with the appropriate layout manager and adapter.

Finally, the handleBackButton method is responsible for handling the back button press on the device, by navigating to the HomeFragment using the NavController.
 */
class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val viewModel: FavoritesViewModel by viewModels {
        FavoritesViewModelFactory(WeatherRepository.getRepository(requireActivity().application))
    }

    private lateinit var favoriteAdapter: FavoriteAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleBackButton()

        initFavoritesRecyclerView()

        viewModel.getFavorites()

        binding.floatingActionButton.setOnClickListener {
            val action = FavoritesFragmentDirections.actionNavigationDashboardToMapsFragment(true)
            findNavController().navigate(action)
        }

        lifecycleScope.launchWhenStarted {
            viewModel.favorites.collect {
                if (it.isNullOrEmpty()) {
                    binding.textEmptyList.visibility = View.VISIBLE
                } else {
                    binding.textEmptyList.visibility = View.GONE
                }
                fetchFavoritesRecycler(it)
            }
        }
    }

    private fun fetchFavoritesRecycler(list: List<OpenWeatherApi>?) {
        favoriteAdapter.favoriteList = list ?: emptyList()
        favoriteAdapter.notifyDataSetChanged()
    }

    private fun initFavoritesRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(FavoritesFragment().context)
        favoriteAdapter = FavoriteAdapter(this.requireContext(), viewModel)
        binding.favoriteRecyclerView.layoutManager = linearLayoutManager
        binding.favoriteRecyclerView.adapter = favoriteAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun handleBackButton() {
        binding.root.isFocusableInTouchMode = true
        binding.root.requestFocus()
        binding.root.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
                Navigation.findNavController(v)
                    .navigate(R.id.action_navigation_dashboard_to_navigation_home)
                return@OnKeyListener true
            }
            return@OnKeyListener false
        })
    }

}
