package org.apps.ifishcam.ui.detect_fish

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import org.apps.ifishcam.ui.MainActivity
import org.apps.ifishcam.databinding.ActivityDetailDetectFishBinding
import org.apps.ifishcam.network.response.PredictResponse
import org.apps.ifishcam.network.response.RecipesItem

class DetailDetectFishActivity : AppCompatActivity() {

    companion object{
        const val KEY_DETECT = "key_detect"
    }

    private lateinit var binding: ActivityDetailDetectFishBinding
    private lateinit var recipesAdapter: RecipesAdapter
    private val detectFishViewModel by viewModels<DetectFishViewModel>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailDetectFishBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        dataDetail()

        detectFishViewModel.listRecipes.observe(this){
            if (it != null) {
                showListRecipes(it)
            }
        }

        binding.back.setOnClickListener {
            val intent = Intent(this@DetailDetectFishActivity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        detectFishViewModel.getRecipes()
    }

    private fun showListRecipes(listRecipes: List<RecipesItem>){
        binding.rvRecipes.layoutManager = LinearLayoutManager(this)
        recipesAdapter = RecipesAdapter(listRecipes)
        binding.rvRecipes.adapter = recipesAdapter
    }

    private fun dataDetail(){
        val dataDetect = intent.getParcelableExtra<PredictResponse>("key_detect")
        if (dataDetect != null){
            binding.apply {
                Glide.with(this@DetailDetectFishActivity)
                    .load(dataDetect.photoUrl)
                    .into(gambarIkan)
                namaIkan.text = dataDetect.fishName
                deskripsiIkan.text = dataDetect.description
                deskripsiNutrisi.text = dataDetect.nutrition?.joinToString("\n\n")
                tvAkurasi.text = dataDetect.predictionAccuracy
            }
        }
    }
}