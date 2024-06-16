package com.example.fishsnap.ui.signup

import android.content.Context
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
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.fishsnap.R
import com.example.fishsnap.auth.ApiClient
import com.example.fishsnap.auth.repository.AuthRepository
import com.example.fishsnap.components.ConfirmPasswordTextField
import com.example.fishsnap.components.PasswordTextField
import com.example.fishsnap.databinding.FragmentSignUpBinding
import com.example.fishsnap.ui.ViewModelFactory

class SignUpFragment : Fragment() {
    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SignUpViewModel by viewModels {
        ViewModelFactory(ApiClient.apiService, requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.window?.statusBarColor =  ContextCompat.getColor(requireContext(),
            R.color.darkGreen
        )
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        WindowCompat.getInsetsController(requireActivity().window, requireActivity().window.decorView).isAppearanceLightStatusBars = false
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
      _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().popBackStack()
           resetStatusBarColor()
        }

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
            resetStatusBarColor()
        }

        binding.btnSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
        }

        val passwordEditText = binding.passwordTextInputLayout.findViewById<PasswordTextField>(R.id.passwordEditText)
        val confirmPasswordEditText = binding.confirmPasswordTextInputLayout.findViewById<ConfirmPasswordTextField>(
            R.id.confirmPasswordEditText
        )

        confirmPasswordEditText.passwordTextField = passwordEditText

        binding.btnSignUp.setOnClickListener {
            showLoading(true)
                val name = binding.nameTextInputLayout.text.toString().trim()
                val username = binding.usernameTextInputLayout.text.toString().trim()
                val email = binding.emailEditTextLayout.text.toString().trim()
                val password = binding.passwordEditText.text.toString().trim()
                val confirmPassword = binding.confirmPasswordEditText.text.toString().trim()

                if (password == confirmPassword) {
                    viewModel.registerUser(name, username, email, password)
                } else {
                    showLoading(false)
                    Toast.makeText(requireContext(), "Passwords do not match", Toast.LENGTH_SHORT).show()
                }
        }

        // TO DO
        //bad logic pengecekan field kosong
        //bad logic user telah terdaftar

        Handler(Looper.getMainLooper()).postDelayed({
        viewModel.registerResponse.observe(viewLifecycleOwner, Observer { response ->
            showLoading(false)
            if (response.isSuccessful) {
                Toast.makeText(requireContext(), "Registrasi Berhasil! Silahkan lakukan verifikasi diemail anda", Toast.LENGTH_LONG).show()
                findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
            } else {
                Toast.makeText(requireContext(), "Registration failed: ${response.message()}", Toast.LENGTH_SHORT).show()
            }
        })
        }, 2000)


        viewModel.errorMessage.observe(viewLifecycleOwner, Observer { message ->
            showLoading(false)
            Toast.makeText(requireContext(), "User Telah Terdaftar", Toast.LENGTH_SHORT).show()
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
        WindowCompat.getInsetsController(requireActivity().window, requireActivity().window.decorView).isAppearanceLightStatusBars = true
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}