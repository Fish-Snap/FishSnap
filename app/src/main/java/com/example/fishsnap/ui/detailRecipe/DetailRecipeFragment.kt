package com.example.fishsnap.ui.detailRecipe

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.fishsnap.R
import com.example.fishsnap.auth.ProductRecipe
import com.example.fishsnap.databinding.FragmentDetailRecipeBinding

class DetailRecipeFragment : Fragment() {
    private var _binding: FragmentDetailRecipeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailRecipeBinding.inflate(inflater, container, false)
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
            val recipe = DetailRecipeFragmentArgs.fromBundle(it).productRecipe
            displayRecipeDetails(recipe)
        }

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun displayRecipeDetails(recipe: ProductRecipe) {
        with(binding) {
            recipeNameTextView.text = recipe.name
            recipeDescriptionTextView.text = recipe.description

            ingredientsTextView.text = recipe.material.joinToString("\n") { "- $it" }
            stepsTextView.text = recipe.steps.joinToString("\n") { "- $it" }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

