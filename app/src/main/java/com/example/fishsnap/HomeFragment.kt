package com.example.fishsnap

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.fishsnap.adapter.carouselAdapter.CarouselAdapter
import com.example.fishsnap.adapter.newsAdapter.NewsAdapter
import com.example.fishsnap.data.dummy.DummyItemsNews
import com.example.fishsnap.databinding.FragmentHomeBinding
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.carousel.CarouselLayoutManager
import com.google.android.material.carousel.CarouselSnapHelper
import com.google.android.material.carousel.HeroCarouselStrategy

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var carouselRecyclerView: RecyclerView
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var newsList: MutableList<DummyItemsNews>

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

        updateUserName()

        newsList = getDummyNewsItems()
        newsAdapter = NewsAdapter(requireContext(), newsList)

        setupCarouselRecyclerView()
        setupNewsRecyclerView()

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

    private fun setupCarouselRecyclerView() {
        carouselRecyclerView = binding.carouselRv
        val carouselLayoutManager = CarouselLayoutManager(HeroCarouselStrategy())
        carouselRecyclerView.layoutManager = carouselLayoutManager
        CarouselSnapHelper().attachToRecyclerView(carouselRecyclerView)
        carouselRecyclerView.adapter = CarouselAdapter(images = getImages()) { imageUrl ->
//            val action = HomeFragmentDirections.actionHomeFragmentToDetailFishFragment(
//                imageUrl
//            )
//            findNavController().navigate(action)
        }
    }

    private fun getImages(): List<String> {
        return listOf(
            "https://i.pinimg.com/564x/67/9c/dc/679cdc274ea67a113a9cd98ef61ec894.jpg",
            "https://i.pinimg.com/564x/67/9c/dc/679cdc274ea67a113a9cd98ef61ec894.jpg",
            "https://i.pinimg.com/564x/67/9c/dc/679cdc274ea67a113a9cd98ef61ec894.jpg",
            "https://i.pinimg.com/564x/67/9c/dc/679cdc274ea67a113a9cd98ef61ec894.jpg",
            "https://i.pinimg.com/564x/67/9c/dc/679cdc274ea67a113a9cd98ef61ec894.jpg",
            "https://i.pinimg.com/564x/67/9c/dc/679cdc274ea67a113a9cd98ef61ec894.jpg",
            "https://i.pinimg.com/564x/67/9c/dc/679cdc274ea67a113a9cd98ef61ec894.jpg",
        )
    }

    private fun getDummyNewsItems(): MutableList<DummyItemsNews> {
        return mutableListOf(
            DummyItemsNews(
                title = "Title 1",
                description = "Description 1",
                author = "Author 1",
                date = "Date 1",
                imageUrl = "https://i.pinimg.com/564x/67/9c/dc/679cdc274ea67a113a9cd98ef61ec894.jpg"
            ),
            DummyItemsNews(
                title = "Title 2",
                description = "Description 2",
                author = "Author 2",
                date = "Date 2",
                imageUrl = "https://i.pinimg.com/564x/06/6f/b2/066fb2bd6a2f623a340239ed25db389d.jpg"
            ),
            DummyItemsNews(
                title = "Title 3",
                description = "Description 3",
                author = "Author 3",
                date = "Date 3",
                imageUrl = "https://i.pinimg.com/564x/aa/48/fe/aa48fe1ed1162094d329a1cbe720bf8c.jpg"
            ),
            DummyItemsNews(
                title = "Title 4",
                description = "Description 4",
                author = "Author 4",
                date = "Date 4",
                imageUrl = "https://i.pinimg.com/564x/67/9c/dc/679cdc274ea67a113a9cd98ef61ec894.jpg"
            ),
            DummyItemsNews(
                title = "Title 5",
                description = "Description 5",
                author = "Author 5",
                date = "Date 5",
                imageUrl = "https://i.pinimg.com/564x/95/68/6a/95686a79fda78c1d70ca6bbc09587977.jpg"
            ),
            DummyItemsNews(
                title = "Title 6",
                description = "Description 5",
                author = "Author 5",
                date = "Date 5",
                imageUrl = "https://i.pinimg.com/564x/67/9c/dc/679cdc274ea67a113a9cd98ef61ec894.jpg"
            ),
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(sharedPreferencesListener)
        _binding = null
    }
}