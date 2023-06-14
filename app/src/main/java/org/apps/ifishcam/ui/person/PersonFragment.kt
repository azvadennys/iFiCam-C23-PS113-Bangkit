package org.apps.ifishcam.ui.person

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import org.apps.ifishcam.R
import org.apps.ifishcam.databinding.FragmentPersonBinding
import org.apps.ifishcam.model.User
import org.apps.ifishcam.model.UserPreference
import org.apps.ifishcam.ui.LoginActivity

class PersonFragment : Fragment() {

    private var _binding: FragmentPersonBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var userPref: UserPreference
    private lateinit var user: User
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        (activity as AppCompatActivity).supportActionBar?.hide()
        // Inflate the layout for this fragment
        _binding = FragmentPersonBinding.inflate(inflater, container, false)

        // Configure Google Sign In
        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        updateUI(currentUser)

        userPref = UserPreference(requireContext())
        user = userPref.getUser()

        if (currentUser == null) {
            // Not signed in, launch the Login activity
            startActivity(Intent(requireActivity(), LoginActivity::class.java))
            requireActivity().finish()
        }

        binding.logoutButton.setOnClickListener {
            googleSignInClient.signOut()
            userPref.setLogout()
            auth.signOut()
            signOut()
            Toast.makeText(requireContext(),"Anda Keluar", Toast.LENGTH_SHORT).show()
        }

        binding.aboutButton.setOnClickListener {
            val intent = Intent(requireActivity(), AboutActivity::class.java)
            startActivity(intent)
        }

        binding.mypostButton.setOnClickListener {
            val intent = Intent(requireActivity(), MyPostActivity::class.java)
            startActivity(intent)
        }


        return binding.root
    }

    private fun updateUI(user: FirebaseUser?) = with(binding){
        Glide.with(this@PersonFragment).load(user?.photoUrl).into(profile)
        namaProfile.text = user?.displayName
    }

    private fun signOut() {
        startActivity(Intent(requireActivity(), LoginActivity::class.java))
        requireActivity().finish()
    }
}