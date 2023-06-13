package com.althaaf.fruitarians.ui.scan

import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.althaaf.fruitarians.R
import com.althaaf.fruitarians.databinding.FragmentScanBinding

class ScanFragment : Fragment() {

    private lateinit var _binding: FragmentScanBinding
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentScanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val window: Window? = activity?.window
        window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window?.statusBarColor = ContextCompat.getColor(requireContext(), R.color.orange)
        setupAction()
    }

    private fun setupAction() {
        binding.btnScan.setOnClickListener {
            val toFruitScanActivity = ScanFragmentDirections.actionNavigationScanToFruitScanActivity()
            it.findNavController().navigate(toFruitScanActivity)
        }
    }

}