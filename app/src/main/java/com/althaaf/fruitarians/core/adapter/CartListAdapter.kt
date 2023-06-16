package com.althaaf.fruitarians.core.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.althaaf.fruitarians.R
import com.althaaf.fruitarians.core.data.network.cart.CartItem
import com.althaaf.fruitarians.core.data.network.cart.CartsItem
import com.althaaf.fruitarians.databinding.ItemCartTokoBinding
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.althaaf.fruitarians.core.helper.formatToFormattedString
import com.althaaf.fruitarians.ui.cart.CartViewModel

class CartListAdapter(
    private val context: Context,
    private val listCart: List<CartsItem>,
    private val viewModelStoreOwner: ViewModelStoreOwner
) :
    RecyclerView.Adapter<CartListAdapter.MyViewHolder>() {

    private lateinit var cartViewModel: CartViewModel

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class MyViewHolder(private val context: Context, val binding: ItemCartTokoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: CartsItem) {
            binding.nameStore.text = data.toko.name
            binding.totalPrice.text = totalPrice(data.cart)
            binding.btnCheckout.setOnClickListener {
                val note = binding.edtNote.text.toString().trim()
                val message = messageTemplate(data, note)
                val url = "https://api.whatsapp.com/send?phone=+62${data.toko.telepon}&text=$message"
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                context.startActivity(intent)
            }
        }

        private fun messageTemplate(data: CartsItem, note: String): String {
            val listCartBuah = listFruit(data.cart)
            val totalPrice = totalPrice(data.cart)
            return "Halo, apakah benar ini dari ${data.toko.name}?, saya ingin memesan $listCartBuah dengan catatan $note dan total belanja sebesar $totalPrice"
        }

        private fun listFruit(cart: List<CartItem>): String {
            var list = ""

            if (cart.isEmpty()) {
                list = ""
            } else {
                cart.forEach { item ->
                    list += " ${item.name},"
                }
            }

            return list
        }

        private fun totalPrice(cart: List<CartItem>): CharSequence {
            var totalPrice = 0

            if (cart.isEmpty()) {
                totalPrice = 0
            } else {
                cart.forEach { item ->
                    totalPrice += item.harga
                }
            }

            val formatPrice = totalPrice.formatToFormattedString()
            return context.getString(R.string.priceFormat, formatPrice)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            ItemCartTokoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        cartViewModel = ViewModelProvider(viewModelStoreOwner).get(CartViewModel::class.java)
        return MyViewHolder(context, binding)
    }

    override fun getItemCount(): Int = listCart.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val items = listCart[position]
        holder.bind(items)

        holder.binding.btnDeleteCartStore.setOnClickListener { onItemClickCallback.onItemClicked(listCart[holder.absoluteAdapterPosition]) }


        val adapter = FruitCartListAdapter(context, items.cart)
        holder.binding.rvCartBuah.adapter = adapter
        holder.binding.rvCartBuah.layoutManager = LinearLayoutManager(context)
        holder.binding.rvCartBuah.setHasFixedSize(true)
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: CartsItem)
    }
}