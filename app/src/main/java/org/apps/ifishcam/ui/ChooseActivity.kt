package org.apps.ifishcam.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.apps.ifishcam.databinding.ActivityChooseBinding
import org.apps.ifishcam.ui.detect_fish.DetectFishActivity
import org.apps.ifishcam.ui.upload_fish.UploadFishActivity

class ChooseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChooseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChooseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        navigate()
    }

    private fun navigate() {
        binding.backButton.setOnClickListener{
            val intent = Intent(this@ChooseActivity, MainActivity::class.java)
            startActivity(intent)
        }

        binding.deteksiButton.setOnClickListener {
            val intent = Intent(this@ChooseActivity, DetectFishActivity::class.java)
            startActivity(intent)
        }

        binding.postingButton.setOnClickListener {
            val intent = Intent(this@ChooseActivity, UploadFishActivity::class.java)
            startActivity(intent)
        }
    }


}