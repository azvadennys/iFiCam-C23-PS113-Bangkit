package org.apps.ifishcam.ui

import android.content.Intent
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.FirebaseAuth
import org.apps.ifishcam.R
import org.apps.ifishcam.databinding.ActivityMainBinding
import org.apps.ifishcam.model.User
import org.apps.ifishcam.model.UserPreference

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var user: User
    private lateinit var auth: FirebaseAuth
    private lateinit var userPref: UserPreference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        userPref = UserPreference(this)
        user = userPref.getUser()


        binding.navView.background = null
        binding.bottomBar.background = null
        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.navigation_home,
            R.id.navigation_history,
            R.id.navigation_explore,
            R.id.navigation_person))

        binding.detectGambar.setOnClickListener {
            startActivity(Intent(this@MainActivity, ChooseActivity::class.java))
        }

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
}