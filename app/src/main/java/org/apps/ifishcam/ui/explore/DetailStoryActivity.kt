package org.apps.ifishcam.ui.explore

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import org.apps.ifishcam.ui.MainActivity
import org.apps.ifishcam.databinding.ActivityDetailStoryBinding
import org.apps.ifishcam.model.story.StoriesItem
import org.apps.ifishcam.utils.formatDate

class DetailStoryActivity : AppCompatActivity() {

    companion object{
        const val KEY_STORY = "key_story"
    }

    private lateinit var binding: ActivityDetailStoryBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        dataDetail()

        binding.backButton.setOnClickListener {
            val intent = Intent(this@DetailStoryActivity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun dataDetail(){
        val dataStory = intent.getParcelableExtra<StoriesItem>("key_story")
        if (dataStory != null){
            binding.apply {
                Glide.with(this@DetailStoryActivity)
                    .load(dataStory.photoUrl)
                    .into(imgIkan)
                namaIkan.text = dataStory.name
                deskripsiIkan.text = dataStory.description
                alamatPenjual.text = dataStory.address
                tanggalUpload.text = dataStory.createdAt?.let { formatDate(it) }
            }
        }
    }
}