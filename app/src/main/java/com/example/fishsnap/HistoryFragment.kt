package com.example.fishsnap

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fishsnap.adapter.historyAdapter.HistoryAdapter
import com.example.fishsnap.data.dummy.DummyItemsHistory
import com.example.fishsnap.databinding.FragmentHistoryBinding

class HistoryFragment : Fragment() {
    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var historyAdapter: HistoryAdapter
    private lateinit var historyList: MutableList<DummyItemsHistory>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        historyList = getDummyHistoryItems()
        historyAdapter = HistoryAdapter(requireContext(), historyList)

        setupHistoryRecyclerview()
    }

    private fun setupHistoryRecyclerview() {
        val linearLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.recyclerHistory.layoutManager = linearLayoutManager
        binding.recyclerHistory.adapter = historyAdapter
    }

    private fun getDummyHistoryItems(): MutableList<DummyItemsHistory> {
return mutableListOf(
    DummyItemsHistory(
        title = "David Arrozaqi",
        description = "Description 1",
        image = "https://i.pinimg.com/564x/09/ba/92/09ba9297ec464820a46ea6af02a27077.jpg"
    ),
    DummyItemsHistory(
        title = "David Arrozaqi",
        description = "Description 1",
        image = "https://i.pinimg.com/564x/09/ba/92/09ba9297ec464820a46ea6af02a27077.jpg"
    ),
    DummyItemsHistory(
        title = "David Arrozaqi",
        description = "Description 1",
        image = "https://i.pinimg.com/564x/09/ba/92/09ba9297ec464820a46ea6af02a27077.jpg"
    ),
    DummyItemsHistory(
        title = "David Arrozaqi",
        description = "Description 1",
        image = "https://i.pinimg.com/564x/09/ba/92/09ba9297ec464820a46ea6af02a27077.jpg"
    ),
    DummyItemsHistory(
        title = "David Arrozaqi",
        description = "Description 1",
        image = "https://i.pinimg.com/564x/09/ba/92/09ba9297ec464820a46ea6af02a27077.jpg"
    ),
    DummyItemsHistory(
        title = "David Arrozaqi",
        description = "Description 1",
        image = "https://i.pinimg.com/564x/09/ba/92/09ba9297ec464820a46ea6af02a27077.jpg"
    ),
    DummyItemsHistory(
        title = "David Arrozaqi",
        description = "Description 1",
        image = "https://i.pinimg.com/564x/09/ba/92/09ba9297ec464820a46ea6af02a27077.jpg"
    ),
)
    }
}