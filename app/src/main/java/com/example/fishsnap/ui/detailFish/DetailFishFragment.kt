package com.example.fishsnap.ui.detailFish

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.fishsnap.R
import com.example.fishsnap.adapter.foodAdapter.LocationAdapter
import com.example.fishsnap.adapter.foodAdapter.RecommendationAdapter
import com.example.fishsnap.auth.FishScanResponse
import com.example.fishsnap.auth.Location
import com.example.fishsnap.auth.ProductRecipe
import com.example.fishsnap.databinding.FragmentDetailFishBinding

class DetailFishFragment : Fragment() {
    private var _binding: FragmentDetailFishBinding? = null
    private val binding get() = _binding!!

    private lateinit var recommendationAdapter: RecommendationAdapter
    private lateinit var locationAdapter: LocationAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailFishBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        arguments?.let {
            val fishScanResponse = DetailFishFragmentArgs.fromBundle(it).fishScanResponse
            displayFishDetails(fishScanResponse)
            setupRecyclerView(fishScanResponse.productRecipe)
            setupLocationRecyclerView(fishScanResponse.location)
        }

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun displayFishDetails(fishScanResponse: FishScanResponse) {
        with(binding) {
            fishNameTextView.text = fishScanResponse.name
            fishScientificNameTextView.text = fishScanResponse.scientificName
            fishDescriptionTextView.text = fishScanResponse.description.joinToString("\n")

            Glide.with(this@DetailFishFragment)
                .load(fishScanResponse.urlImg)
                .transform(FitCenter(), RoundedCorners(16))
                .into(annotatedImageView)
        }
    }

    private fun setupRecyclerView(recipes: List<ProductRecipe>) {
        recommendationAdapter = RecommendationAdapter { recipe ->
            val action = DetailFishFragmentDirections.actionDetailFishFragmentToDetailRecipeFragment(recipe)
            findNavController().navigate(action)
        }

        binding.recommendationRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = recommendationAdapter
        }

        recommendationAdapter.submitList(recipes)
    }

    private fun setupLocationRecyclerView(locations: List<Location>) {
        locationAdapter = LocationAdapter(locations)

        binding.locationRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = locationAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
