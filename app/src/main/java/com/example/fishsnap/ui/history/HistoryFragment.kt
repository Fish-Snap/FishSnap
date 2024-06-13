package com.example.fishsnap.ui.history

import android.content.Context
import android.os.Bundle
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
import com.example.fishsnap.data.dummy.DummyItemsHistory
import com.example.fishsnap.databinding.FragmentHistoryBinding
import com.example.fishsnap.ui.ViewModelFactory

class HistoryFragment : Fragment() {
    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var historyAdapter: HistoryAdapter

    private val viewModel: HistoryViewModel by viewModels {
        val sharedPreferences = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        ViewModelFactory(ApiClient.apiService, sharedPreferences)
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

        viewModel.historyData.observe(viewLifecycleOwner) { historyList ->
            historyAdapter = HistoryAdapter(historyList) { fishScanResponse ->
                val action = HistoryFragmentDirections.actionHistoryFragmentToDetailFishFragment(fishScanResponse)
                findNavController().navigate(action)
            }
            binding.recyclerHistory.adapter = historyAdapter
        }

        viewModel.loadHistory()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

