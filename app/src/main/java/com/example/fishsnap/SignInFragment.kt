package com.example.fishsnap

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.fishsnap.databinding.FragmentSignInBinding


class SignInFragment : Fragment() {
    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.window?.statusBarColor =  ContextCompat.getColor(requireContext(), R.color.darkGreen)
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        WindowCompat.getInsetsController(requireActivity().window, requireActivity().window.decorView).isAppearanceLightStatusBars = false
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnSignIn.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_homeFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        activity?.window?.statusBarColor = Color.TRANSPARENT
        WindowCompat.getInsetsController(requireActivity().window, requireActivity().window.decorView).isAppearanceLightStatusBars = true
        _binding = null
    }
}