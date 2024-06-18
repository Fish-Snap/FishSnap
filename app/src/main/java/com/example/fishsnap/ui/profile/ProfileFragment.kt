package com.example.fishsnap.ui.profile

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.navigation.fragment.findNavController
import com.example.fishsnap.R
import com.example.fishsnap.databinding.DialogLogoutBinding
import com.example.fishsnap.databinding.FragmentProfileBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.transition.MaterialFadeThrough
import com.google.android.material.transition.SlideDistanceProvider

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val fadeThrough = MaterialFadeThrough().apply {
            duration = 300L
        }
        enterTransition = fadeThrough
        exitTransition = fadeThrough
       updateStatusBarColor()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val userName = sharedPreferences.getString("USER_NAME", "User")
        binding.tvUsername.text = userName

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        binding.cvTerms.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_termsFragment)
        }

        binding.cvAboutUs.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_aboutFragment)
        }

        binding.cvChangePassword.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_changePasswordFragment)
        }

        binding.cvLogout.setOnClickListener {
            showLogoutDialog()
        }
    }

    private fun showLogoutDialog() {
        val binding = DialogLogoutBinding.inflate(layoutInflater)

        val dialog =  MaterialAlertDialogBuilder(requireContext(), R.style.DialogAnimation)
            .setView(binding.root)
            .setCancelable(true)
            .create()

        binding.btnNo.setOnClickListener {
            dialog.dismiss()
        }

        binding.btnYes.setOnClickListener {
            val sharedPreferences = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
            with(sharedPreferences.edit()) {
                clear()
                apply()
            }
            dialog.dismiss()
            findNavController().navigate(R.id.action_profileFragment_to_welcomeFragment)
        }

        dialog.show()
    }

    override fun onResume() {
        super.onResume()
        updateStatusBarColor()
    }

    private fun updateStatusBarColor() {
        activity?.window?.statusBarColor = ContextCompat.getColor(requireContext(), R.color.darkGreen)
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        WindowCompat.getInsetsController(requireActivity().window, requireActivity().window.decorView).isAppearanceLightStatusBars = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        activity?.window?.statusBarColor = Color.WHITE
        WindowCompat.getInsetsController(requireActivity().window, requireActivity().window.decorView).isAppearanceLightStatusBars = true
        _binding = null
    }
}