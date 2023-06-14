package org.apps.ifishcam.ui.person

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import org.apps.ifishcam.ui.MainActivity
import org.apps.ifishcam.databinding.ActivityMyPostBinding
import org.apps.ifishcam.model.User
import org.apps.ifishcam.model.UserPreference
import org.apps.ifishcam.model.story.StoriesItem

class MyPostActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyPostBinding
    private val myPostViewModel by viewModels<MyPostViewModel>()
    private lateinit var myPostAdapter: MyPostAdapter
    private lateinit var userPref: UserPreference
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        myPostViewModel.listStory.observe(this){
            if (it != null) {
                showListStories(it)
            }
        }

        myPostViewModel.isLoading.observe(this){
            showLoading(it)
        }

        myPostViewModel.isEmpty.observe(this){
            showEmptyData(it)
        }

        binding.backButton.setOnClickListener {
            val intent = Intent(this@MyPostActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    override fun onResume() {
        super.onResume()
        userPref = UserPreference(this)
        user = userPref.getUser()
        val uid = user.userId.toString()
        myPostViewModel.getStoriesUid(uid)
    }

    private fun showListStories(listStory: List<StoriesItem>){
        binding.rvStory.layoutManager = LinearLayoutManager(this)
        myPostAdapter = MyPostAdapter(listStory)
        binding.rvStory.adapter = myPostAdapter
    }

    private fun showEmptyData(isEmpty: Boolean){
        binding.emptyData.visibility = if (isEmpty) View.VISIBLE else View.GONE
    }

    private fun showLoading(isLoading: Boolean){
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

}