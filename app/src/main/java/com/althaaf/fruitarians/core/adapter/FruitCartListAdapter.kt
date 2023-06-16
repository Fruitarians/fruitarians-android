package com.althaaf.fruitarians.core.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.althaaf.fruitarians.R
import com.althaaf.fruitarians.core.data.network.cart.CartItem
import com.althaaf.fruitarians.core.helper.formatToFormattedString
import com.althaaf.fruitarians.databinding.ItemCartBuahBinding
import com.bumptech.glide.Glide

class FruitCartListAdapter(private val context: Context, private val listFruitCart: List<CartItem>): RecyclerView.Adapter<FruitCartListAdapter.MyViewHolder>() {

    class MyViewHolder(private val context: Context, var binding: ItemCartBuahBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CartItem) {
            val formatPrice = item.harga.formatToFormattedString()

            binding.nameCartBuah.text = item.name
            binding.priceCartBuah.text = context.getString(R.string.priceFormat, formatPrice)
            binding.satuanCartBuah.text = context.getString(R.string.unitFormat, item.satuan)
            Glide.with(itemView.context)
                .load(item.gambar)
                .into(binding.imageCartBuah)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemCartBuahBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(context, binding)
    }

    override fun getItemCount(): Int = listFruitCart.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = listFruitCart[position]
        holder.bind(item)
    }
}