package com.example.fishsnap

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.navigation.fragment.findNavController
import com.example.fishsnap.components.ConfirmPasswordTextField
import com.example.fishsnap.components.PasswordTextField
import com.example.fishsnap.databinding.FragmentSignUpBinding

class SignUpFragment : Fragment() {
    private var _binding: FragmentSignUpBinding? = null
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

        // Initialize the custom views
        val passwordEditText = binding.passwordTextInputLayout.findViewById<PasswordTextField>(R.id.passwordEditText)
        val confirmPasswordEditText = binding.confirmPasswordTextInputLayout.findViewById<ConfirmPasswordTextField>(R.id.confirmPasswordEditText)

        // Set the PasswordTextField for ConfirmPasswordTextField
        confirmPasswordEditText.passwordTextField = passwordEditText
        
        // TO Do
        // add validate if button sign up pressed
    }

    private fun Fragment.resetStatusBarColor() {
        activity?.window?.statusBarColor = Color.TRANSPARENT
        WindowCompat.getInsetsController(requireActivity().window, requireActivity().window.decorView).isAppearanceLightStatusBars = true
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}