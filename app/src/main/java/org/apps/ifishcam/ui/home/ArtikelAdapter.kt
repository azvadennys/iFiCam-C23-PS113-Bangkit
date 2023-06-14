package org.apps.ifishcam.ui.home

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.apps.ifishcam.databinding.ItemArtikelBinding
import org.apps.ifishcam.model.artikel.ArticlesItem

class ArtikelAdapter(private val listArtikel: List<ArticlesItem>): RecyclerView.Adapter<ArtikelAdapter.ViewHolder>() {
    class ViewHolder (val binding: ItemArtikelBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(artikel: ArticlesItem){
            binding.apply {
                tvItemTitle.text = artikel.title
                tvItemPublishedDate.text = artikel.date
                Glide.with(itemView.context)
                    .load(artikel.photoUrl)
                    .into(imgPoster)
                itemView.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(artikel.linkUrl)
                    itemView.context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemArtikelBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val artikel = listArtikel[position]
        holder.bind(artikel)
    }

    override fun getItemCount(): Int = listArtikel.size
}