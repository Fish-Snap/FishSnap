package com.example.fishsnap.ui.history

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fishsnap.adapter.historyAdapter.HistoryAdapter
import com.example.fishsnap.auth.ApiClient
import com.example.fishsnap.databinding.FragmentHistoryBinding
import com.example.fishsnap.ui.ViewModelFactory
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialFadeThrough
import com.google.android.material.transition.SlideDistanceProvider

class HistoryFragment : Fragment() {
    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var historyAdapter: HistoryAdapter

    private val viewModel: HistoryViewModel by viewModels {
        val sharedPreferences = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        ViewModelFactory(ApiClient.apiService, sharedPreferences)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val fadeThrough = MaterialFadeThrough().apply {
            duration = 300L
        }
        enterTransition = fadeThrough
        exitTransition = fadeThrough
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
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

        historyAdapter = HistoryAdapter(emptyList()) { fishScanResponse ->
            val action = HistoryFragmentDirections.actionHistoryFragmentToDetailFishFragment(fishScanResponse)
            findNavController().navigate(action)
        }

        binding.recyclerHistory.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = historyAdapter
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.loadHistory()
        }

        viewModel.historyData.observe(viewLifecycleOwner) { historyList ->
            if (historyList.isEmpty()) {
                binding.handleImage.visibility = View.VISIBLE
                binding.handleText.visibility = View.VISIBLE
                binding.recyclerHistory.visibility = View.GONE
            } else {
                binding.handleImage.visibility = View.GONE
                binding.handleText.visibility = View.GONE
                binding.recyclerHistory.visibility = View.VISIBLE
            }
            historyAdapter = HistoryAdapter(historyList) { fishScanResponse ->
                val action = HistoryFragmentDirections.actionHistoryFragmentToDetailFishFragment(fishScanResponse)
                findNavController().navigate(action)
            }
            binding.recyclerHistory.adapter = historyAdapter
            binding.swipeRefreshLayout.isRefreshing = false
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
            binding.swipeRefreshLayout.isRefreshing = false
        }

        viewModel.loadHistory()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}