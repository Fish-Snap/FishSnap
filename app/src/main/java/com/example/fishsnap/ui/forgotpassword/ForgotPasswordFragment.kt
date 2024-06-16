package com.example.fishsnap.ui.forgotpassword

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.fishsnap.R
import com.example.fishsnap.databinding.FragmentForgotPasswordBinding
import com.google.android.material.snackbar.Snackbar

class ForgotPasswordFragment : Fragment() {

    private var _binding: FragmentForgotPasswordBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ForgotPasswordViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentForgotPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.window?.statusBarColor =  ContextCompat.getColor(requireContext(), R.color.darkGreen)
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        WindowCompat.getInsetsController(requireActivity().window, requireActivity().window.decorView).isAppearanceLightStatusBars = false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().popBackStack()
        }

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnSubmit.setOnClickListener {
            val email = binding.emailEditTextLayout.text.toString().trim()

            if (email.isEmpty()) {
                binding.emailEditTextLayout.error = "Field tidak boleh kosong"
                Snackbar.make(binding.root, "Email tidak boleh kosong", Snackbar.LENGTH_SHORT).show()
            } else {
                showLoading(true)
                Handler(Looper.getMainLooper()).postDelayed({
                    viewModel.forgotPassword(email)
                }, 2000)
            }
        }

        viewModel.successMessage.observe(viewLifecycleOwner) { message ->
            showLoading(false)
            Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.action_forgotPasswordFragment_to_signInFragment)
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            showLoading(false)
            Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.loadingButton.root.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}