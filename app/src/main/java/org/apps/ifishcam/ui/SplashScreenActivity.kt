package org.apps.ifishcam.ui

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import org.apps.ifishcam.databinding.ActivitySplashScreenBinding
import org.apps.ifishcam.model.User
import org.apps.ifishcam.model.UserPreference

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding
    private lateinit var userPref: UserPreference
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        userPref = UserPreference(this)
        val user = userPref.getUser()
        sessionUser(user.userId)
    }

    private fun sessionUser(userId: String?){
        if (userId != null){
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("userId", userId)
            navigate(intent)
        } else{
            val intent = Intent(this, LoginActivity::class.java)
            navigate(intent)
        }
    }

    private fun navigate(intent: Intent){
        val delayMillis = 3000L
        val handler = Handler()
        handler.postDelayed({
            startActivity(intent)
            finish()
        }, delayMillis)
    }

}