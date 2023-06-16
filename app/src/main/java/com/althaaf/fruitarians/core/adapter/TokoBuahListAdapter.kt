package com.althaaf.fruitarians.core.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.althaaf.fruitarians.R
import com.althaaf.fruitarians.core.data.network.profile.mystore.BuahItem
import com.althaaf.fruitarians.core.helper.formatToFormattedString
import com.althaaf.fruitarians.databinding.ItemProductmystoreBinding
import com.althaaf.fruitarians.ui.product.ProductAddUpdateActivity
import com.bumptech.glide.Glide

class TokoBuahListAdapter(private val context: Context): PagingDataAdapter<BuahItem, TokoBuahListAdapter.MyViewHolder>(DIFF_CALLBACK) {

    class MyViewHolder(private val context: Context, private val binding: ItemProductmystoreBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: BuahItem) {
            val formatPrice = item.harga.formatToFormattedString()

            binding.nameProductMystore.text = item.name
            binding.priceProductMystore.text = context.getString(R.string.priceFormat, formatPrice)
            binding.stockProductMystore.text = context.getString(R.string.stock, item.stok)

            Glide.with(itemView.context)
                .load(item.gambar)
                .into(binding.imageProductMystore)

            itemView.setOnClickListener {
                val detailBuah = BuahItem(
                    harga = item.harga,
                    satuan = item.satuan,
                    name = item.name,
                    id = item.id,
                    deskripsi =item.deskripsi,
                    stok = item.stok,
                    gambar = item.gambar
                )

                val intent = Intent(itemView.context, ProductAddUpdateActivity::class.java)
                intent.putExtra(ProductAddUpdateActivity.EXTRA_DETAIL, detailBuah)
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemProductmystoreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(context,binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
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