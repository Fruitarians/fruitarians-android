package com.althaaf.fruitarians.core.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.althaaf.fruitarians.R
import com.althaaf.fruitarians.core.data.network.dashboard.fruitstore.DataItem
import com.althaaf.fruitarians.databinding.ItemFruitstoreBinding
import com.althaaf.fruitarians.ui.detailfruitstore.DetailFruitStoreActivity
import com.bumptech.glide.Glide

class FruitStoreListAdapter :
    PagingDataAdapter<DataItem, FruitStoreListAdapter.MyViewHolder>(DIFF_CALLBACK) {

    class MyViewHolder(private val binding: ItemFruitstoreBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DataItem) {
            binding.alamatToko.text = item.alamat.kota
            binding.namaToko.text = item.name

            if (item.gambarProfil == null) {
                binding.imgFruitStore.setImageDrawable(ContextCompat.getDrawable(binding.imgFruitStore.context, R.drawable.store_transparent))
            } else {
                Glide.with(itemView.context)
                    .load(item.gambarProfil)
                    .into(binding.imgFruitStore)
                binding.imgFruitStore.scaleType = ImageView.ScaleType.FIT_XY
            }

            itemView.setOnClickListener {

                val intent = Intent(itemView.context, DetailFruitStoreActivity::class.java)
                intent.putExtra(DetailFruitStoreActivity.EXTRA_DATA, item)
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            ItemFruitstoreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
    }


    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataItem>() {
            override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
                return oldItem.id == newItem.id
            }

        }
    }
}