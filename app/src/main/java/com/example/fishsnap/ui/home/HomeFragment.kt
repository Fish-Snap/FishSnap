package com.example.fishsnap.ui.home

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.fishsnap.adapter.carouselAdapter.CarouselAdapter
import com.example.fishsnap.adapter.newsAdapter.NewsAdapter
import com.example.fishsnap.auth.ApiClient
import com.example.fishsnap.data.dummy.CarouselItem
import com.example.fishsnap.data.dummy.DummyItemsNews
import com.example.fishsnap.databinding.FragmentHomeBinding
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.carousel.CarouselLayoutManager
import com.google.android.material.carousel.CarouselSnapHelper
import com.google.android.material.carousel.HeroCarouselStrategy
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch

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
        carouselRecyclerView.adapter = CarouselAdapter(carouselItem) { carouselItem ->
            val action = HomeFragmentDirections.actionHomeFragmentToDetailCarouselFragment(carouselItem)
            findNavController().navigate(action)
        }
    }

    private fun getCarouselItems(): List<CarouselItem> {
        val json = requireContext().assets.open("carouselDummyItem.json").bufferedReader().use {
            it.readText()
        }
        val listType = object : TypeToken<List<CarouselItem>>() {}.type
        return Gson().fromJson(json, listType)
    }

//
//    private fun getDummyNewsItems(): MutableList<DummyItemsNews> {
//        return mutableListOf(
//            DummyItemsNews(
//                title = "Title 1",
//                description = "Description 1",
//                author = "Author 1",
//                date = "Date 1",
//                imageUrl = "https://i.pinimg.com/564x/67/9c/dc/679cdc274ea67a113a9cd98ef61ec894.jpg"
//            ),
//            DummyItemsNews(
//                title = "Title 2",
//                description = "Description 2",
//                author = "Author 2",
//                date = "Date 2",
//                imageUrl = "https://i.pinimg.com/564x/06/6f/b2/066fb2bd6a2f623a340239ed25db389d.jpg"
//            ),
//            DummyItemsNews(
//                title = "Title 3",
//                description = "Description 3",
//                author = "Author 3",
//                date = "Date 3",
//                imageUrl = "https://i.pinimg.com/564x/aa/48/fe/aa48fe1ed1162094d329a1cbe720bf8c.jpg"
//            ),
//            DummyItemsNews(
//                title = "Title 4",
//                description = "Description 4",
//                author = "Author 4",
//                date = "Date 4",
//                imageUrl = "https://i.pinimg.com/564x/67/9c/dc/679cdc274ea67a113a9cd98ef61ec894.jpg"
//            ),
//            DummyItemsNews(
//                title = "Title 5",
//                description = "Description 5",
//                author = "Author 5",
//                date = "Date 5",
//                imageUrl = "https://i.pinimg.com/564x/95/68/6a/95686a79fda78c1d70ca6bbc09587977.jpg"
//            ),
//            DummyItemsNews(
//                title = "Title 6",
//                description = "Description 5",
//                author = "Author 5",
//                date = "Date 5",
//                imageUrl = "https://i.pinimg.com/564x/67/9c/dc/679cdc274ea67a113a9cd98ef61ec894.jpg"
//            ),
//        )
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(sharedPreferencesListener)
        _binding = null
    }
}