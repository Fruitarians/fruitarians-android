package com.althaaf.fruitarians.core.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.althaaf.fruitarians.core.data.network.profile.mystore.BuahItem
import com.althaaf.fruitarians.core.data.network.profile.myvendor.VendorSubsItem
import com.althaaf.fruitarians.databinding.ItemPartnerBinding
import com.althaaf.fruitarians.ui.myvendor.DetailMyVendorActivity
import com.althaaf.fruitarians.ui.myvendor.SubsAddUpdateActivity
import com.althaaf.fruitarians.ui.product.ProductAddUpdateActivity
import com.bumptech.glide.Glide

class PartnerVendorListAdapter: PagingDataAdapter<VendorSubsItem, PartnerVendorListAdapter.MyViewHolder>(DIFF_CALLBACK) {

    class MyViewHolder(private val binding: ItemPartnerBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: VendorSubsItem) {
            binding.namaPartner.text = item.name
            binding.alamatPartner.text = item.alamat
            binding.datePartner.text = item.schedule

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailMyVendorActivity::class.java)
                intent.putExtra(DetailMyVendorActivity.EXTRA_DETAIL, item)
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemPartnerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
    }


    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<VendorSubsItem>() {
            override fun areItemsTheSame(oldItem: VendorSubsItem, newItem: VendorSubsItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: VendorSubsItem, newItem: VendorSubsItem): Boolean {
                return oldItem.id == newItem.id
            }

        }
    }
}