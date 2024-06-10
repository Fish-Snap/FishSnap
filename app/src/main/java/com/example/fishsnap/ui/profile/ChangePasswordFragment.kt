package com.example.fishsnap.ui.profile

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.fishsnap.R
import com.example.fishsnap.databinding.FragmentChangePasswordBinding
import com.google.android.material.snackbar.Snackbar


class ChangePasswordFragment : Fragment() {
    private var _binding: FragmentChangePasswordBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChangePasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val userName = sharedPreferences.getString("USER_USERNAME", "Username")
        val email = sharedPreferences.getString("USER_EMAIL", "Email")
        
        binding.usernameTextInputLayout.setText(userName)
        binding.emailEditTextLayout.setText(email)

        binding.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnSignUp.setOnClickListener {
            val newPassword = binding.passwordEditText.text.toString()
            val confirmPassword = binding.confirmPasswordEditText.text.toString()

            if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                Snackbar.make(binding.root, "Fields cannot be empty", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (newPassword != confirmPassword) {
                binding.confirmPasswordTextInputLayout.error = "Passwords Harus Sama!!!"
                binding.confirmPasswordTextInputLayout.endIconMode = com.google.android.material.textfield.TextInputLayout.END_ICON_PASSWORD_TOGGLE
                return@setOnClickListener
            }
            binding.confirmPasswordTextInputLayout.error = null
            binding.confirmPasswordTextInputLayout.endIconMode = com.google.android.material.textfield.TextInputLayout.END_ICON_PASSWORD_TOGGLE
            Snackbar.make(binding.root, "Password changed successfully", Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}