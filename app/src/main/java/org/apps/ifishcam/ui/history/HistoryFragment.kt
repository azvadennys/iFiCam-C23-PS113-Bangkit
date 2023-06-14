package org.apps.ifishcam.ui.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import org.apps.ifishcam.databinding.FragmentHistoryBinding
import org.apps.ifishcam.model.User
import org.apps.ifishcam.model.UserPreference
import org.apps.ifishcam.network.response.HistoriesItem

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val historyViewModel by viewModels<HistoryViewModel>()
    private lateinit var userPref: UserPreference
    private lateinit var user: User
    private lateinit var historyAdapter: HistoryAdapter
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.hide()
        // Inflate the layout for this fragment
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)

        historyViewModel.isEmpty.observe(viewLifecycleOwner){
            showEmptyData(it)
        }

        historyViewModel.isLoading.observe(viewLifecycleOwner){
            showLoading(it)
        }

        historyViewModel.listHistory.observe(viewLifecycleOwner){
            if (it != null) {
                showListArticle(it)
            }
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        userPref = UserPreference(requireContext())
        user = userPref.getUser()
        val uid = user.userId.toString()
        historyViewModel.getHistories(uid)
    }

    private fun showListArticle(listHistory: List<HistoriesItem>){
        binding.rvHistory.layoutManager = LinearLayoutManager(requireContext())
        historyAdapter = HistoryAdapter(listHistory)
        binding.rvHistory.adapter = historyAdapter
    }

    private fun showLoading(isLoading: Boolean){
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showEmptyData(isEmpty: Boolean){
        binding.emptyData.visibility = if (isEmpty) View.VISIBLE else View.GONE
    }
}