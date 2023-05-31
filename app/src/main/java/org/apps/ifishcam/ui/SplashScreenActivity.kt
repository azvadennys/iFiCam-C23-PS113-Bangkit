package org.apps.ifishcam.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import org.apps.ifishcam.MainActivity
import org.apps.ifishcam.R
import org.apps.ifishcam.databinding.ActivitySplashScreenBinding
import org.apps.ifishcam.model.User
import org.apps.ifishcam.model.UserPreference
import org.apps.ifishcam.ui.home.HomeFragment

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding
    private lateinit var userPref: UserPreference
    private lateinit var user: User
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        auth = FirebaseAuth.getInstance()
        userPref = UserPreference(this)
        user = userPref.getUser()
        sessionUser()
    }

    private fun navigate(intent: Intent){
        val delayMillis = 3000L
        val handler = Handler()
        handler.postDelayed({
            startActivity(intent)
            finish()
        }, delayMillis)
    }

    private fun sessionUser() {
        if (user.userId != null && user.name != null && user.userId != null) {
            navigate(Intent(this, MainActivity::class.java))
        } else {
            navigate(Intent(this, LoginActivity::class.java))
        }
    }
}