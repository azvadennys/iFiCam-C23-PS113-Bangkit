package org.apps.ifishcam.ui.explore

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.apps.ifishcam.databinding.ItemStoryBinding
import org.apps.ifishcam.model.story.StoriesItem
import org.apps.ifishcam.utils.formatDate
import org.apps.ifishcam.utils.removeSymbols

class ExploreAdapter(private val listStory: List<StoriesItem>) : RecyclerView.Adapter<ExploreAdapter.ViewHolder>() {

    class ViewHolder (val binding: ItemStoryBinding): RecyclerView.ViewHolder(binding.root){
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(stories: StoriesItem){
            binding.apply {
                namaProfile.text = stories.userName?.let { removeSymbols(it) }
                tvItemName.text = stories.name
                descriptionAlamat.text = stories.address
                dateTextView.text = stories.createdAt?.let { formatDate(it) }
                Glide.with(itemView.context)
                    .load(stories.photoUrl)
                    .into(ivItemPhoto)
                itemView.setOnClickListener {
                    val intentDetail = Intent(itemView.context, DetailStoryActivity::class.java)
                    intentDetail.putExtra(DetailStoryActivity.KEY_STORY, stories)

                    val optionsCompat: ActivityOptionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                            itemView.context as Activity,
                            Pair(ivItemPhoto, "profile"),
                            Pair(tvItemName, "nama"),
                            Pair(descriptionAlamat, "deskripsi"),
                            Pair(dateTextView, "upload"),
                        )
                    itemView.context.startActivity(intentDetail, optionsCompat.toBundle())
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val stories = listStory[position]
        holder.bind(stories)
    }

    override fun getItemCount(): Int = listStory.size
}