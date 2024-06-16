package com.example.fishsnap.ui.profile

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.fishsnap.R
import com.example.fishsnap.components.ConfirmPasswordTextField
import com.example.fishsnap.components.PasswordTextField
import com.example.fishsnap.databinding.FragmentChangePasswordBinding
import com.google.android.material.snackbar.Snackbar


class ChangePasswordFragment : Fragment() {
    private var _binding: FragmentChangePasswordBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ChangePasswordViewModel by viewModels()

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
        val token = sharedPreferences.getString("TOKEN", "")

        if (token.isNullOrEmpty()) {
            Snackbar.make(binding.root, "Authorization token is missing. Please log in again.", Snackbar.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_changePasswordFragment_to_homeFragment)
            return
        }

        binding.usernameTextInputLayout.setText(userName)
        binding.emailEditTextLayout.setText(email)

        binding.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }

        val passwordEditText = binding.passwordTextInputLayout.findViewById<PasswordTextField>(R.id.passwordEditText)
        val confirmPasswordEditText = binding.confirmPasswordTextInputLayout.findViewById<ConfirmPasswordTextField>(
            R.id.confirmPasswordEditText
        )

        confirmPasswordEditText.passwordTextField = passwordEditText

        binding.btnSignUp.setOnClickListener {
            val oldPassword = binding.oldpasswordEditText.text.toString()
            val newPassword = binding.passwordEditText.text.toString()
            val confirmPassword = binding.confirmPasswordEditText.text.toString()

            var isValid = true

            // Check if fields are empty and set error messages accordingly
            if (oldPassword.isEmpty()) {
                binding.oldpasswordTextInputLayout.error = "Field tidak boleh kosong"
                binding.oldpasswordTextInputLayout.errorIconDrawable = null
                isValid = false
            } else {
                binding.oldpasswordTextInputLayout.error = null
            }

            if (newPassword.isEmpty()) {
                binding.passwordTextInputLayout.error = "Field tidak boleh kosong"
                binding.passwordTextInputLayout.errorIconDrawable = null
                isValid = false
            } else {
                binding.passwordTextInputLayout.error = null
            }

            if (confirmPassword.isEmpty()) {
                binding.confirmPasswordTextInputLayout.error = "Field tidak boleh kosong"
                binding.confirmPasswordTextInputLayout.errorIconDrawable = null
                isValid = false
            } else {
                binding.confirmPasswordTextInputLayout.error = null
            }

            if (!isValid) {
                Snackbar.make(binding.root, "Fields cannot be empty", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Check if new password length is less than 8 characters
            if (newPassword.length < 8) {
                binding.passwordTextInputLayout.error = "Password harus lebih dari 8 karakter"
                binding.passwordTextInputLayout.errorIconDrawable = null
                binding.passwordTextInputLayout.endIconMode = com.google.android.material.textfield.TextInputLayout.END_ICON_PASSWORD_TOGGLE
                return@setOnClickListener
            } else {
                binding.passwordTextInputLayout.error = null
            }

            // Check if new password and confirm password do not match
            if (newPassword != confirmPassword) {
                binding.confirmPasswordTextInputLayout.error = "Passwords Harus Sama!!!"
                binding.confirmPasswordTextInputLayout.errorIconDrawable = null
                binding.confirmPasswordTextInputLayout.endIconMode = com.google.android.material.textfield.TextInputLayout.END_ICON_PASSWORD_TOGGLE
                return@setOnClickListener
            } else {
                binding.confirmPasswordTextInputLayout.error = null
            }

            // Reset errors and end icon modes if all checks pass
            binding.passwordTextInputLayout.error = null
            binding.confirmPasswordTextInputLayout.error = null
            binding.passwordTextInputLayout.endIconMode = com.google.android.material.textfield.TextInputLayout.END_ICON_PASSWORD_TOGGLE
            binding.confirmPasswordTextInputLayout.endIconMode = com.google.android.material.textfield.TextInputLayout.END_ICON_PASSWORD_TOGGLE

            // Call ViewModel method to change password
            viewModel.changePassword(token, oldPassword, newPassword)
        }

        viewModel.changePasswordResult.observe(viewLifecycleOwner) { success ->
            if (success) {
                showSuccessDialog()
            } else {
                Snackbar.make(
                    binding.root,
                    viewModel.errorMessage.value ?: "Password change failed",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun showSuccessDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Password Changed")
            .setMessage("Password anda berhasil diganti, silahkan login ulang.")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
                logoutAndNavigateToWelcome()
            }
            .show()
    }

    private fun logoutAndNavigateToWelcome() {
        val sharedPreferences = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()
        findNavController().navigate(R.id.action_changePasswordFragment_to_welcomeFragment)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}