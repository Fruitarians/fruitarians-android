package com.althaaf.fruitarians.ui.cart

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.althaaf.fruitarians.R
import com.althaaf.fruitarians.core.adapter.CartListAdapter
import com.althaaf.fruitarians.core.data.network.cart.CartsItem
import com.althaaf.fruitarians.core.data.network.retrofit.ApiResult
import com.althaaf.fruitarians.core.helper.CartViewModelFactory
import com.althaaf.fruitarians.databinding.FragmentCartBinding

class CartFragment : Fragment() {

    private lateinit var _binding: FragmentCartBinding
    private val binding get() = _binding

    private lateinit var cartViewModel: CartViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val window: Window? = activity?.window
        window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window?.statusBarColor = ContextCompat.getColor(requireContext(), R.color.green)

        setupViewModel()
        setupListToko()
    }

    private fun setupListToko() {
        cartViewModel.getUserCart().observe(requireActivity()) {
            it?.let { response ->
                when (response) {
                    is ApiResult.Loading -> {
                        binding.lottieLoading.visibility = View.VISIBLE
                    }

                    is ApiResult.Success -> {
                        binding.lottieLoading.visibility = View.GONE
                        val adapter = CartListAdapter(requireActivity(), response.data.data.carts, requireActivity())
                        binding.rvCartToko.adapter = adapter
                        binding.rvCartToko.layoutManager = LinearLayoutManager(requireActivity())
                        binding.rvCartToko.setHasFixedSize(true)

                        if (response.data.data.totalData == 0) {
                            binding.rvCartToko.visibility = View.GONE
                            binding.stateEmpty.visibility = View.VISIBLE
                        } else{
                            binding.rvCartToko.visibility = View.VISIBLE
                            binding.stateEmpty.visibility = View.GONE
                        }

                        adapter.setOnItemClickCallback(object : CartListAdapter.OnItemClickCallback {
                            override fun onItemClicked(data: CartsItem) {
                                showAlertDialog(data)
                            }
                        })
                    }

                    is ApiResult.Error -> {
                        binding.lottieLoading.visibility = View.GONE
                        Toast.makeText(requireActivity(), response.error, Toast.LENGTH_SHORT).show()
                        Log.d(TAG, "Error: ${response.error}")
                    }
                }
            }
        }
    }

    private fun showAlertDialog(data: CartsItem) {
        val message = "Are you sure you want to delete this cart ?"
        val title = "Delete Cart"

        val alertDialogBuilder = AlertDialog.Builder(requireActivity())
        with(alertDialogBuilder) {
            setTitle(title)
            setMessage(message)
            setCancelable(false)
            setPositiveButton("Yes") { _, _ ->
                deleteCart(data.idCart)
            }
            setNegativeButton("No") { dialog, _ -> dialog.cancel()}
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun deleteCart(idCart: String) {
        cartViewModel.deleteStoreCart(idCart = idCart).observe(this){
            it?.let { response ->
                when (response) {
                    is ApiResult.Loading -> {
                    }

                    is ApiResult.Success -> {
                        Toast.makeText(requireActivity(), response.data.message, Toast.LENGTH_SHORT).show()
                        setupListToko()
                    }

                    is ApiResult.Error -> {
                        Toast.makeText(requireActivity(), response.error, Toast.LENGTH_SHORT).show()
                        Log.d(TAG, "Error: ${response.error}")
                    }
                }
            }
        }
    }


    private fun setupViewModel() {
        cartViewModel = ViewModelProvider(requireActivity(), CartViewModelFactory.getInstance(requireActivity()))[CartViewModel::class.java]
    }

    companion object {
        private const val TAG = "CartFragment"
    }
}