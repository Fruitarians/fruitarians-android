package com.althaaf.fruitarians.core.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.althaaf.fruitarians.core.data.local.model.RecommendationModel
import com.althaaf.fruitarians.databinding.ItemRecommendationBinding
import com.althaaf.fruitarians.ui.fruitvariant.FruitVariantActivity
import com.bumptech.glide.Glide

class RecommendationAdapter(private val listRecommendation: List<RecommendationModel>): RecyclerView.Adapter<RecommendationAdapter.MyViewHolder>() {

    class MyViewHolder(private val binding: ItemRecommendationBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(data: RecommendationModel) {
            Glide.with(itemView.context)
                .load(data.image)
                .into(binding.imgRecommendation)

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, FruitVariantActivity::class.java)
                intent.putExtra(FruitVariantActivity.EXTRA_SEARCH, data.search)
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemRecommendationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount() = listRecommendation.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = listRecommendation[position]
        holder.bind(item)
    }
}