package org.apps.ifishcam.ui.history

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.apps.ifishcam.databinding.ItemHistoryBinding
import org.apps.ifishcam.network.response.HistoriesItem

class HistoryAdapter (private val listHistory: List<HistoriesItem>): RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {
    class ViewHolder (val binding: ItemHistoryBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(history: HistoriesItem){
            binding.apply {
                tvItemTitle.text = history.name
                tvAkurasi.text = history.predictionAccuracy
                Glide.with(itemView.context)
                    .load(history.photoUrl)
                    .into(imgPoster)
                itemView.setOnClickListener {
                    val intentDetail = Intent(itemView.context, DetailHistoryActivity::class.java)
                    intentDetail.putExtra(DetailHistoryActivity.KEY_HISTORY, history)
                    itemView.context.startActivity(intentDetail)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val history = listHistory[position]
        holder.bind(history)
    }

    override fun getItemCount(): Int = listHistory.size
}