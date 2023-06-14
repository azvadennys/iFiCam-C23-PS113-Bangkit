package org.apps.ifishcam.ui.detect_fish

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.apps.ifishcam.databinding.ItemResepBinding
import org.apps.ifishcam.network.response.RecipesItem

class RecipesAdapter (private val listRecipes: List<RecipesItem>) : RecyclerView.Adapter<RecipesAdapter.ViewHolder>() {
    class ViewHolder (val binding: ItemResepBinding): RecyclerView.ViewHolder(binding.root){
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(recipes: RecipesItem){
            binding.apply {
                tvItemTitle.text = recipes.title
                tvTingkatKesulitan.text = recipes.difficulty
                tvDurasi.text = recipes.duration
                Glide.with(itemView.context)
                    .load(recipes.photoUrl)
                    .into(imgPoster)
                itemView.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(recipes.linkUrl)
                    itemView.context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemResepBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recipes = listRecipes[position]
        holder.bind(recipes)
    }

    override fun getItemCount(): Int = listRecipes.size
}