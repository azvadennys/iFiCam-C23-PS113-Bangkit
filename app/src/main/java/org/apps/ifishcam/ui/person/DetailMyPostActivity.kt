package org.apps.ifishcam.ui.person

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import org.apps.ifishcam.databinding.ActivityDetailMyPostBinding
import org.apps.ifishcam.model.StoryReq
import org.apps.ifishcam.model.User
import org.apps.ifishcam.model.UserPreference
import org.apps.ifishcam.model.story.StoriesItem
import org.apps.ifishcam.utils.formatDate

class DetailMyPostActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailMyPostBinding
    private val myPostViewModel by viewModels<MyPostViewModel>()
    private lateinit var userPref: UserPreference
    private lateinit var user: User
    private lateinit var auth: FirebaseAuth

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailMyPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        dataDetail()
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        userPref = UserPreference(this)
        user = userPref.getUser()

        binding.deleteButton.setOnClickListener {
            val uid = currentUser?.uid

            val dataStory = intent.getParcelableExtra<StoriesItem>("key_story")
            val storyId = dataStory?.storyId

            if (uid != null && storyId != null) {
                val dialogBuilder = AlertDialog.Builder(this)
                dialogBuilder.setMessage("Apakah Anda yakin ingin menghapus?")
                    .setCancelable(false)
                    .setPositiveButton("Ya") { _, _ ->
                        val story = StoryReq(storyId)
                        myPostViewModel.deleteStoriesUid(uid, story)
                        val intent = Intent(this, MyPostActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                        Toast.makeText(this, "Story Dihapus", Toast.LENGTH_SHORT).show()
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


        binding.backButton.setOnClickListener {
            val intent = Intent(this@DetailMyPostActivity, MyPostActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun dataDetail(){
        val dataStory = intent.getParcelableExtra<StoriesItem>("key_story")
        if (dataStory != null){
            binding.apply {
                Glide.with(this@DetailMyPostActivity)
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