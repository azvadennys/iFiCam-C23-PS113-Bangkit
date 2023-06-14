package org.apps.ifishcam.ui.history

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import org.apps.ifishcam.ui.MainActivity
import org.apps.ifishcam.databinding.ActivityDetailHistoryBinding
import org.apps.ifishcam.model.HistoryReq
import org.apps.ifishcam.model.User
import org.apps.ifishcam.model.UserPreference
import org.apps.ifishcam.network.response.DetailedHistory
import org.apps.ifishcam.network.response.HistoriesItem
import org.apps.ifishcam.network.response.RecipesItem
import org.apps.ifishcam.ui.detect_fish.RecipesAdapter

class DetailHistoryActivity : AppCompatActivity() {

    companion object{
        const val KEY_HISTORY = "key_histories"
    }

    private lateinit var binding: ActivityDetailHistoryBinding
    private val historyViewModel by viewModels<HistoryViewModel>()
    private lateinit var recipesAdapter: RecipesAdapter
    private lateinit var userPref: UserPreference
    private lateinit var user: User
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        userPref = UserPreference(this)
        user = userPref.getUser()

        binding.deleteButton.setOnClickListener {
            val uid = currentUser?.uid
            val dataHistory = intent.getParcelableExtra<HistoriesItem>("key_histories")
            val historyId = dataHistory?.historyId

            if (uid != null && historyId != null) {
                val dialogBuilder = AlertDialog.Builder(this)
                dialogBuilder.setMessage("Apakah Anda yakin ingin menghapus?")
                    .setCancelable(false)
                    .setPositiveButton("Ya") { _, _ ->
                        val history = HistoryReq(historyId)
                        historyViewModel.deleteHistoryUid(uid, history)
                        val intent = Intent(this, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                        Toast.makeText(this, "History Dihapus", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    .setNegativeButton("Tidak") { dialog, _ ->
                        dialog.cancel()
                    }

                val alert = dialogBuilder.create()
                alert.setTitle("Konfirmasi Hapus")
                alert.show()
            }
        }

        val uid = currentUser?.uid
        val dataHistory = intent.getParcelableExtra<HistoriesItem>("key_histories")
        val historyId = dataHistory?.historyId
        historyViewModel.getDetailHistory(uid!!,historyId!!)
        dataDetail()

        historyViewModel.detailHistory.observe(this){
            detail(it)
        }

        historyViewModel.listRecipes.observe(this){
            if (it != null) {
                showListRecipes(it)
            }
        }

        binding.back.setOnClickListener {
            val intent = Intent(this@DetailHistoryActivity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        historyViewModel.getRecipes()
    }

    private fun detail(detail: DetailedHistory){
        binding.deskripsiIkan.text = detail.description
        binding.deskripsiNutrisi.text = detail.nutrition?.joinToString("\n\n")
    }

    private fun showListRecipes(listRecipes: List<RecipesItem>){
        binding.rvRecipes.layoutManager = LinearLayoutManager(this)
        recipesAdapter = RecipesAdapter(listRecipes)
        binding.rvRecipes.adapter = recipesAdapter
    }

    private fun dataDetail() {
        val dataHistory = intent.getParcelableExtra<HistoriesItem>("key_histories")
        binding.apply {
            Glide.with(this@DetailHistoryActivity)
                .load(dataHistory!!.photoUrl)
                .into(gambarIkan)
            namaIkan.text = dataHistory.name
            tvAkurasi.text = dataHistory.predictionAccuracy
        }
    }
}