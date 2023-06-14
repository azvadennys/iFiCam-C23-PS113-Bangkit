package org.apps.ifishcam.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.apps.ifishcam.databinding.ItemTenfishBinding
import org.apps.ifishcam.model.tenfish.TenFish

class TenFishAdapter(private val tenFishList: List<TenFish>) :
    RecyclerView.Adapter<TenFishAdapter.TenFishViewHolder>() {

    class TenFishViewHolder(val binding: ItemTenfishBinding) : RecyclerView.ViewHolder(binding.root) {
       fun bind(tenFish: TenFish){
           binding.apply {
               tvItemTenfish.text = tenFish.nama
               Glide.with(itemView.context)
                   .load(tenFish.photoUrl)
                   .into(imgTenfish)
           }
       }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TenFishViewHolder {
        val binding = ItemTenfishBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TenFishViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TenFishViewHolder, position: Int) {
        val currentTenFish = tenFishList[position]
        holder.bind(currentTenFish)
    }

    override fun getItemCount() = tenFishList.size


}
