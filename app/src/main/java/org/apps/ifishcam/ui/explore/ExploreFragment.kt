package org.apps.ifishcam.ui.explore

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import org.apps.ifishcam.databinding.FragmentExploreBinding
import org.apps.ifishcam.model.story.StoriesItem
import org.apps.ifishcam.ui.map.MapsActivity

class ExploreFragment : Fragment() {

    private var _binding: FragmentExploreBinding? = null
    private val exploreViewModel by viewModels<ExploreViewModel>()
    private lateinit var exploreAdapter: ExploreAdapter


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        (activity as AppCompatActivity).supportActionBar?.hide()

        _binding = FragmentExploreBinding.inflate(inflater, container, false)

        exploreViewModel.listStories.observe(viewLifecycleOwner){
            if (it != null) {
                showListStories(it)
            }
        }

        exploreViewModel.isLoading.observe(viewLifecycleOwner){
            showLoading(it)
        }

        exploreViewModel.emptyData.observe(viewLifecycleOwner){
            showEmptyData(it)
        }

        binding.mapButton.setOnClickListener {
            startActivity(Intent(requireActivity(), MapsActivity::class.java))
        }

        return binding.root
    }

    private fun showListStories(listStory: List<StoriesItem>){
        binding.rvStory.layoutManager = LinearLayoutManager(requireContext())
        exploreAdapter = ExploreAdapter(listStory)
        binding.rvStory.adapter = exploreAdapter
    }

    private fun showLoading(isLoading: Boolean){
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showEmptyData(isEmpty: Boolean){
        binding.emptyData.visibility = if (isEmpty) View.VISIBLE else View.GONE
    }

    override fun onResume() {
        super.onResume()
        exploreViewModel.getStory()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}