package com.althaaf.fruitarians.ui.profile

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.althaaf.fruitarians.R
import com.althaaf.fruitarians.core.data.network.dashboard.Alamat
import com.althaaf.fruitarians.core.data.network.dashboard.Data
import com.althaaf.fruitarians.core.data.network.retrofit.ApiResult
import com.althaaf.fruitarians.core.helper.ProfileViewModelFactory
import com.althaaf.fruitarians.databinding.FragmentProfileBinding
import com.althaaf.fruitarians.ui.authentication.login.LoginActivity
import com.bumptech.glide.Glide

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var userDataProfile: Data
    private var userImg: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val window: Window? = activity?.window
        window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window?.statusBarColor = ContextCompat.getColor(requireContext(), R.color.green)

        setupViewModel()
        setupGetDataUser()
        setupAction()
    }

    override fun onResume() {
        super.onResume()
        setupGetDataUser()
    }

    private fun setupGetDataUser() {
        profileViewModel.getAllDataUser().observe(requireActivity()) {
            it?.let { response ->
                when(response) {
                    is ApiResult.Loading -> {
                        binding.lottieLoading.visibility = View.VISIBLE
                    }

                    is ApiResult.Success -> {
                        val roleUser = response.data.data.role

                        if (roleUser == "user") {
                            userDataProfile = Data(
                                bergabung = response.data.data.bergabung,
                                role = response.data.data.role,
                                jamOperasional = response.data.data.jamOperasional,
                                name = response.data.data.name,
                                telepon = response.data.data.telepon,
                                gambarProfil = null,
                                id = response.data.data.id,
                                deskripsi = "",
                                email = response.data.data.email,
                                alamat = Alamat(
                                    kota = response.data.data.alamat.kota,
                                    negara = response.data.data.alamat.negara,
                                    deskripsiAlamat = response.data.data.alamat.deskripsiAlamat
                                )
                            )

                            userImg = response.data.data.gambarProfil

                            binding.btnMystore.visibility = View.GONE
                            binding.lottieLoading.visibility = View.GONE
                            binding.nameProfile.text = response.data.data.name
                            binding.roleProfile.text = getString(R.string.User)
                            binding.imgProfile.elevation = 0F
                            binding.imgProfile.setImageDrawable(ContextCompat.getDrawable(binding.imgProfile.context, R.drawable.default_user_biasa))
                        } else if (roleUser == "toko") {
                            userDataProfile = Data(
                                bergabung = response.data.data.bergabung,
                                role = response.data.data.role,
                                jamOperasional = response.data.data.jamOperasional,
                                name = response.data.data.name,
                                telepon = response.data.data.telepon,
                                gambarProfil = response.data.data.gambarProfil,
                                id = response.data.data.id,
                                deskripsi = response.data.data.deskripsi,
                                email = response.data.data.email,
                                alamat = Alamat(
                                    kota = response.data.data.alamat.kota,
                                    negara = response.data.data.alamat.negara,
                                    deskripsiAlamat = response.data.data.alamat.deskripsiAlamat
                                )
                            )

                            userImg = response.data.data.gambarProfil

                            binding.lottieLoading.visibility = View.GONE
                            binding.nameProfile.text = response.data.data.name
                            binding.roleProfile.text = getString(R.string.seller)
                            binding.btnMystore.visibility = View.VISIBLE
                            binding.btnMyvendor.visibility = View.GONE

                            if (response.data.data.gambarProfil != null) {
                                Glide.with(requireActivity())
                                    .load(response.data.data.gambarProfil)
                                    .into(binding.imgProfile)
                                binding.imgProfile.elevation = 6F

                            } else {
                                binding.imgProfile.setImageDrawable(ContextCompat.getDrawable(binding.imgProfile.context, R.drawable.default_img_toko))
                                binding.imgProfile.elevation = 6F
                            }
                        } else if (roleUser == "vendor") {
                            userDataProfile = Data(
                                bergabung = response.data.data.bergabung,
                                role = response.data.data.role,
                                jamOperasional = response.data.data.jamOperasional,
                                name = response.data.data.name,
                                telepon = response.data.data.telepon,
                                gambarProfil = response.data.data.gambarProfil,
                                id = response.data.data.id,
                                deskripsi = response.data.data.deskripsi,
                                email = response.data.data.email,
                                alamat = Alamat(
                                    kota = response.data.data.alamat.kota,
                                    negara = response.data.data.alamat.negara,
                                    deskripsiAlamat = response.data.data.alamat.deskripsiAlamat
                                )
                            )

                            userImg = response.data.data.gambarProfil

                            binding.lottieLoading.visibility = View.GONE
                            binding.nameProfile.text = response.data.data.name
                            binding.roleProfile.text = getString(R.string.vendor)
                            binding.btnMystore.visibility = View.GONE
                            binding.btnMyvendor.visibility = View.VISIBLE

                            if (response.data.data.gambarProfil != null) {
                                Glide.with(requireActivity())
                                    .load(response.data.data.gambarProfil)
                                    .into(binding.imgProfile)
                                binding.imgProfile.elevation = 6F

                            } else {
                                binding.imgProfile.setImageDrawable(ContextCompat.getDrawable(binding.imgProfile.context, R.drawable.default_img_toko))
                                binding.imgProfile.elevation = 6F
                            }
                        }
                    }

                    is ApiResult.Error -> {
                        binding.lottieLoading.visibility = View.GONE
                        Toast.makeText(requireActivity(), response.error, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun setupViewModel() {
        profileViewModel = ViewModelProvider(
            requireActivity(),
            ProfileViewModelFactory.getInstance(requireActivity())
        )[ProfileViewModel::class.java]
    }

    private fun setupAction() {
        binding.buttonLogout.setOnClickListener {
            profileViewModel.logout().observe(requireActivity()) {
                it?.let { response ->
                    when (response) {
                        is ApiResult.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                            binding.buttonLogout.text = ""
                        }

                        is ApiResult.Success -> {
                            Toast.makeText(
                                requireActivity(),
                                response.data.message,
                                Toast.LENGTH_SHORT
                            ).show()
                            profileViewModel.clearUserSession()
                            moveActivity()
                        }

                        is ApiResult.Error -> {
                            Toast.makeText(requireActivity(), response.error, Toast.LENGTH_SHORT)
                                .show()
                            binding.progressBar.visibility = View.GONE
                        }
                    }
                }
            }
        }

        binding.btnMystore.setOnClickListener { view->
            val toMyStoreActivity = ProfileFragmentDirections.actionNavigationProfileToMyStoreActivity()
            view.findNavController().navigate(toMyStoreActivity)
        }

        binding.btnMyvendor.setOnClickListener { view->
            val toMyVendorActivity = ProfileFragmentDirections.actionNavigationProfileToMyVendorActivity()
            view.findNavController().navigate(toMyVendorActivity)
        }

        binding.btnLanguage.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }

        binding.btnChangePassword.setOnClickListener { view ->
            val toChangePasswordFragment = ProfileFragmentDirections.actionNavigationProfileToChangePasswordFragment(userImg)
            view.findNavController().navigate(toChangePasswordFragment)
        }

        binding.btnEditProfile.setOnClickListener { view ->
            val toEditProfileFragment = ProfileFragmentDirections.actionNavigationProfileToEditProfileFragment(userDataProfile)
            toEditProfileFragment.detailUser = userDataProfile
            view.findNavController().navigate(toEditProfileFragment)
        }
    }

    private fun moveActivity() {
        val intent = Intent(requireActivity(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

}