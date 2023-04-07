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
import com.iti.weatherwatch.favorites.viewmodel.FavoritesViewModel
import com.iti.weatherwatch.favorites.viewmodel.FavoritesViewModelFactory
import com.iti.weatherwatch.model.OpenWeatherApi

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
                if (!it.isNullOrEmpty()) {
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
