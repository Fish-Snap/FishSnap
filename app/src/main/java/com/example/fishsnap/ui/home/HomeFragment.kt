package com.example.fishsnap.ui.home

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.fishsnap.R
import com.example.fishsnap.adapter.carouselAdapter.CarouselAdapter
import com.example.fishsnap.adapter.newsAdapter.NewsAdapter
import com.example.fishsnap.data.dummy.CarouselItem
import com.example.fishsnap.databinding.FragmentHomeBinding
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.carousel.CarouselLayoutManager
import com.google.android.material.carousel.CarouselSnapHelper
import com.google.android.material.carousel.HeroCarouselStrategy
import com.google.android.material.transition.MaterialFadeThrough
import com.google.android.material.transition.SlideDistanceProvider
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var carouselRecyclerView: RecyclerView
    private lateinit var newsAdapter: NewsAdapter
    private val newsViewModel: NewsViewModel by viewModels()


    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var sharedPreferencesListener: SharedPreferences.OnSharedPreferenceChangeListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val fadeThrough = MaterialFadeThrough().apply {
            duration = 300L
        }
        enterTransition = fadeThrough
        exitTransition = fadeThrough
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        setupSharedPreferencesListener()
        val token = sharedPreferences.getString("USER_TOKEN", "") ?: ""
        newsAdapter = NewsAdapter(requireContext(), emptyList())
        setupNewsRecyclerView()

        updateUserName()

        newsViewModel.newsList.observe(viewLifecycleOwner, { newsList ->
            newsAdapter.updateNewsList(newsList)
        })

        newsViewModel.showNoNews.observe(viewLifecycleOwner, { showNoNews ->
            if (showNoNews) {
                showNoNewsLayout()
            } else {
                hideNoNewsLayout()
            }
        })

//        newsViewModel.isLoading.observe(viewLifecycleOwner, { isLoading ->
//            // loading state kalo mau
//        })

        newsViewModel.fetchNews(token)

        setupCarouselRecyclerView()

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().finish()
            }
        })
    }

    private fun setupSharedPreferencesListener() {
        sharedPreferencesListener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (key == "USER_NAME") {
                updateUserName()
            }
        }
        sharedPreferences.registerOnSharedPreferenceChangeListener(sharedPreferencesListener)
    }

    private fun updateUserName() {
        val userName = sharedPreferences.getString("USER_NAME", "User")
        val toolbar: MaterialToolbar = binding.topBar
        toolbar.title = "Hi, $userName!"
    }

    private fun setupNewsRecyclerView() {
        val staggeredGridLayoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.recyclerNews.layoutManager = staggeredGridLayoutManager
        binding.recyclerNews.adapter = newsAdapter
    }

    private fun showNoNewsLayout() {
        binding.noNewsLayout.visibility = View.VISIBLE
        binding.recyclerNews.visibility = View.GONE
    }

    private fun hideNoNewsLayout() {
        binding.noNewsLayout.visibility = View.GONE
        binding.recyclerNews.visibility = View.VISIBLE
    }

    private fun setupCarouselRecyclerView() {
        carouselRecyclerView = binding.carouselRv
        val carouselLayoutManager = CarouselLayoutManager(HeroCarouselStrategy())
        carouselRecyclerView.layoutManager = carouselLayoutManager
        CarouselSnapHelper().attachToRecyclerView(carouselRecyclerView)
        val carouselItem = getCarouselItems()
        carouselRecyclerView.adapter = CarouselAdapter(carouselItem) { carouselItems ->
            val action = HomeFragmentDirections.actionHomeFragmentToDetailCarouselFragment(carouselItems)
            findNavController().navigate(action)
        }
    }

    private fun getCarouselItems(): List<CarouselItem> {
        val inputStream = requireContext().resources.openRawResource(R.raw.dummy_item)
        val json = inputStream.bufferedReader().use {
            it.readText()
        }
        val listType = object : TypeToken<List<CarouselItem>>() {}.type
        return Gson().fromJson(json, listType)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(sharedPreferencesListener)
        _binding = null
    }
}