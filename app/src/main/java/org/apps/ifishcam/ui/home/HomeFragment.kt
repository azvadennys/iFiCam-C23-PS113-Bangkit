package org.apps.ifishcam.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import org.apps.ifishcam.databinding.FragmentHomeBinding
import org.apps.ifishcam.model.User
import org.apps.ifishcam.model.UserPreference
import org.apps.ifishcam.model.artikel.ArtikelData
import org.apps.ifishcam.model.tenfish.TenFishData

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var newsAdapter: ArtikelAdapter
    private lateinit var tenFishAdapter: TenFishAdapter
    private val homeViewModel by viewModels<HomeViewModel>()
    private lateinit var userPref: UserPreference
    private lateinit var user: User
    private lateinit var auth: FirebaseAuth
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as AppCompatActivity).supportActionBar?.hide()
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        updateUI(currentUser)

        userPref = UserPreference(requireContext())
        user = userPref.getUser()

        showListArtikel()
        showListTenFish()

        return binding.root
    }

    private fun updateUI(user: FirebaseUser?) = with(binding){
        Glide.with(this@HomeFragment).load(user?.photoUrl).into(profile)
        namaProfile.text = user?.displayName
    }

    private fun showListArtikel() {
        binding.rvArtikel.layoutManager = LinearLayoutManager(requireContext())
        newsAdapter = ArtikelAdapter(ArtikelData.artikel)
        binding.rvArtikel.adapter = newsAdapter
    }

    private fun showListTenFish(){
        binding.rvTenfish.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        tenFishAdapter = TenFishAdapter(TenFishData.tenFish)
        binding.rvTenfish.adapter = tenFishAdapter
    }

    private fun showLoading(isLoading: Boolean){
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        homeViewModel.getNews()
        _binding = null
    }
}