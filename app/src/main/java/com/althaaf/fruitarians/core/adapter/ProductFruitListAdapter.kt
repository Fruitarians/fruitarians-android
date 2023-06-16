package com.althaaf.fruitarians.core.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.althaaf.fruitarians.R
import com.althaaf.fruitarians.core.data.network.dashboard.fruitstore.detail.BuahItem
import com.althaaf.fruitarians.core.helper.formatToFormattedString
import com.althaaf.fruitarians.databinding.ItemListBuahBinding
import com.althaaf.fruitarians.ui.detailfruitstore.DetailSpecificFruitActivity
import com.bumptech.glide.Glide

class ProductFruitListAdapter(private val context: Context, private val idToko: String): PagingDataAdapter<BuahItem, ProductFruitListAdapter.MyViewHolder>(DIFF_CALLBACK) {

    class MyViewHolder(private val binding: ItemListBuahBinding, private val context: Context, private val idToko: String): RecyclerView.ViewHolder(binding.root) {
        fun bind(data: BuahItem) {
            val formatPrice = data.harga.formatToFormattedString()
            binding.priceProduct.text = context.getString(R.string.priceFormat, formatPrice)
            binding.titleListProduct.text = data.name
            Glide.with(itemView.context)
                .load(data.gambar)
                .into(binding.fotoListProduct)

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailSpecificFruitActivity::class.java)
                intent.putExtra(DetailSpecificFruitActivity.EXTRA_ID_TOKO, idToko)
                intent.putExtra(DetailSpecificFruitActivity.EXTRA_ID_BUAH, data.id)
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemListBuahBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding, context, idToko)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<BuahItem>() {
            override fun areItemsTheSame(oldItem: BuahItem, newItem: BuahItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: BuahItem, newItem: BuahItem): Boolean {
                return oldItem.id == newItem.id
            }

        }
    }


}