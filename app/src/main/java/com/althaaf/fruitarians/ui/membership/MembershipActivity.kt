package com.althaaf.fruitarians.ui.membership

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.althaaf.fruitarians.R
import com.althaaf.fruitarians.core.adapter.FruitStoreListAdapter
import com.althaaf.fruitarians.core.adapter.LoadingStateAdapter
import com.althaaf.fruitarians.core.adapter.MembershipListAdapter
import com.althaaf.fruitarians.core.adapter.TokoBuahListAdapter
import com.althaaf.fruitarians.core.helper.HomeViewModelFactory
import com.althaaf.fruitarians.databinding.ActivityMembershipBinding

class MembershipActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMembershipBinding
    private lateinit var membershipViewModel: MembershipViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMembershipBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupListMembership()
        setupAction()
    }

    override fun onResume() {
        super.onResume()
        setupListMembership()
    }

    private fun setupAction() {
        binding.btnBack.setOnClickListener {
            this.onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupListMembership() {
        val adapter = MembershipListAdapter()
        binding.rvMembership.layoutManager = LinearLayoutManager(this)
        binding.rvMembership.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter{
                adapter.retry()
            }
        )

        membershipViewModel.getUserMembership().observe(this) { data ->
            adapter.submitData(lifecycle,data)
        }

        adapter.addLoadStateListener { loadState ->
            if (loadState.source.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached && adapter.itemCount < 1) {
                binding.rvMembership.visibility = View.GONE
                binding.membershipNotFound.visibility = View.VISIBLE
            } else {
                binding.rvMembership.visibility = View.VISIBLE
                binding.membershipNotFound.visibility = View.GONE
            }
        }
    }

    private fun setupViewModel() {
        membershipViewModel = ViewModelProvider(this, HomeViewModelFactory.getInstance(this))[MembershipViewModel::class.java]
    }
}