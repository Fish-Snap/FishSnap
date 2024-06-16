package com.example.fishsnap.ui.singin

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.fishsnap.R
import com.example.fishsnap.auth.ApiClient
import com.example.fishsnap.auth.repository.AuthRepository
import com.example.fishsnap.databinding.FragmentSignInBinding
import com.example.fishsnap.ui.ViewModelFactory


class SignInFragment : Fragment() {

    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!

    private val sharedPreferences by lazy {
        requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    }

    private val viewModel: SignInViewModel by viewModels {
        ViewModelFactory(ApiClient.apiService, sharedPreferences)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.window?.statusBarColor =
            ContextCompat.getColor(requireContext(), R.color.darkGreen)
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        WindowCompat.getInsetsController(
            requireActivity().window,
            requireActivity().window.decorView
        ).isAppearanceLightStatusBars = false
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
            resetStatusBarColor()
        }

        binding.btnSignIn.setOnClickListener {
            val email = binding.emailEditTextLayout.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()

            var isValid = true

            if (email.isEmpty() || password.isEmpty()) {
                binding.emailEditTextLayout.error = "Field tidak boleh kosong"
                binding.edtPassword.error = "Field tidak boleh kosong"
                binding.edtPassword.errorIconDrawable = null
                isValid = false
                Toast.makeText(requireContext(), "Semua field harus diisi", Toast.LENGTH_SHORT)
                    .show()
                showLoading(false)
            } else {
                showLoading(true)
                Handler(Looper.getMainLooper()).postDelayed({
                    viewModel.loginUser(email, password)
                }, 2000)
                Handler(Looper.getMainLooper()).postDelayed({
                    resetStatusBarColor()
                }, 5000)
            }
        }

        binding.tvForgotPassword.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_forgotPasswordFragment)
        }

        viewModel.loginResponse.observe(viewLifecycleOwner, Observer { response ->
            showLoading(false)
            if (response.isSuccessful) {
                Toast.makeText(requireContext(), "Login Berhasil!", Toast.LENGTH_LONG).show()
                findNavController().navigate(R.id.action_signInFragment_to_homeFragment)
            } else {
                Toast.makeText(
                    requireContext(),
                    "Login gagal : ${response.message()}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

        viewModel.errorMessage.observe(viewLifecycleOwner, Observer { message ->
            showLoading(false)
            Toast.makeText(requireContext(), "User belum terdaftar", Toast.LENGTH_SHORT).show()
        })
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.loadingButton.root.visibility = View.VISIBLE
        } else {
            binding.loadingButton.root.visibility = View.GONE
        }
    }

    private fun Fragment.resetStatusBarColor() {
        activity?.window?.statusBarColor = Color.WHITE
        WindowCompat.getInsetsController(
            requireActivity().window,
            requireActivity().window.decorView
        ).isAppearanceLightStatusBars = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
//        activity?.window?.statusBarColor = ContextCompat.getColor(requireContext(), android.R.color.white)
//        WindowCompat.getInsetsController(requireActivity().window, requireActivity().window.decorView).isAppearanceLightStatusBars = true
        _binding = null
    }
}