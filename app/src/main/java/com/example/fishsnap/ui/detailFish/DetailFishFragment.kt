package com.example.fishsnap.ui.detailFish

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.fishsnap.auth.FishScanResponse
import com.example.fishsnap.databinding.FragmentDetailFishBinding

class DetailFishFragment : Fragment() {
    private var _binding: FragmentDetailFishBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailFishBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            val fishScanResponse = DetailFishFragmentArgs.fromBundle(it).fishScanResponse
            displayFishDetails(fishScanResponse)
        }

//        findNavController().popBackStack()
    }

    private fun displayFishDetails(fishScanResponse: FishScanResponse) {
        with(binding) {
            fishNameTextView.text = fishScanResponse.name
            fishScientificNameTextView.text = fishScanResponse.scientificName
            fishDescriptionTextView.text = fishScanResponse.description.joinToString("\n")

            Glide.with(this@DetailFishFragment)
                .load(fishScanResponse.urlImg)
                .into(fishImageView)

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}