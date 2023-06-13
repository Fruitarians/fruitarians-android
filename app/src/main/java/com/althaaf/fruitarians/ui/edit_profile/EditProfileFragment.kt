package com.althaaf.fruitarians.ui.edit_profile

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.althaaf.fruitarians.R
import com.althaaf.fruitarians.core.data.network.dashboard.Data
import com.althaaf.fruitarians.core.data.network.retrofit.ApiResult
import com.althaaf.fruitarians.core.helper.ProfileViewModelFactory
import com.althaaf.fruitarians.core.helper.reduceFileImage
import com.althaaf.fruitarians.core.helper.uriToFile
import com.althaaf.fruitarians.databinding.FragmentEditProfileBinding
import com.althaaf.fruitarians.ui.profile.ProfileFragment
import com.bumptech.glide.Glide
import kotlinx.coroutines.NonDisposableHandle.parent
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File


class EditProfileFragment : Fragment() {

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var editProfileViewModel: EditProfileViewModel
    private var getFile: File? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val window: Window? = activity?.window
        window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window?.statusBarColor = ContextCompat.getColor(requireContext(), R.color.green)

        setupViewModel()
        setupData()
        setupAction()
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val selectedImg = result.data?.data as Uri

            selectedImg.let { uri ->
                val myFile = uriToFile(uri, requireActivity())
                getFile = myFile
                binding.imgProfile.setImageURI(uri)
                binding.imgProfile.elevation = 6F
            }
        }
    }

    private fun setupAction() {
        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.btnSaveProfile.setOnClickListener {
            val name = binding.edEditName.text.toString().trim()
            val telepon = binding.edEditTelepon.text.toString().trim()
            val address = binding.edEditAddress.text.toString().trim()
            val city = binding.edEditKota.text.toString().trim()
            val state = binding.autoCompleteTextState.text.toString().trim()
            val description = binding.edEditDescription.text.toString().trim()
            val startDay = binding.autoCompleteTextStartDay.text.toString().trim()
            val endDay = binding.autoCompleteTextEndDay.text.toString().trim()
            val startTime = binding.edEditTimeStart.text.toString().trim()
            val endTime = binding.edEditTimeEnd.text.toString().trim()

            Log.d(TAG, "name: $name")
            Log.d(TAG, "telepon: $telepon")
            Log.d(TAG, "address: $address")
            Log.d(TAG, "city: $city")
            Log.d(TAG, "state: $state")
            Log.d(TAG, "description: $description")
            Log.d(TAG, "startDay: $startDay")
            Log.d(TAG, "endDay: $endDay")
            Log.d(TAG, "startTime: $startTime")
            Log.d(TAG, "endTime: $endTime")
            Log.d(TAG, "endTime: $")

            if (getFile == null) {
                editProfile(
                    name.toRequestBody("text/plain".toMediaType()),
                    state.toRequestBody("text/plain".toMediaType()),
                    city.toRequestBody("text/plain".toMediaType()),
                    address.toRequestBody("text/plain".toMediaType()),
                    telepon.toRequestBody("text/plain".toMediaType()),
                    description.toRequestBody("text/plain".toMediaType()),
                    startTime.toRequestBody("text/plain".toMediaType()),
                    endTime.toRequestBody("text/plain".toMediaType()),
                    startDay.toRequestBody("text/plain".toMediaType()),
                    endDay.toRequestBody("text/plain".toMediaType()) ,
                    null,
                )
            } else {
                val file = reduceFileImage(getFile as File)
                val requestImageFile = file.asRequestBody("image/jpeg".toMediaType())
                val imageMultiPart: MultipartBody.Part = MultipartBody.Part.createFormData(
                    "file",
                    file.name,
                    requestImageFile
                )
                Log.d(TAG, "SEND IMAGE : $requestImageFile")

                editProfile(
                    name.toRequestBody("text/plain".toMediaType()),
                    state.toRequestBody("text/plain".toMediaType()),
                    city.toRequestBody("text/plain".toMediaType()),
                    address.toRequestBody("text/plain".toMediaType()),
                    telepon.toRequestBody("text/plain".toMediaType()),
                    description.toRequestBody("text/plain".toMediaType()),
                    startTime.toRequestBody("text/plain".toMediaType()),
                    endTime.toRequestBody("text/plain".toMediaType()),
                    startDay.toRequestBody("text/plain".toMediaType()),
                    endDay.toRequestBody("text/plain".toMediaType()) ,
                    imageMultiPart,
                )
            }
        }

        binding.btnChangeImg.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            val chooser = Intent.createChooser(intent, "Choose a Picture")
            launcherIntentGallery.launch(chooser)
        }
    }

    private fun editProfile(
        name: RequestBody,
        state: RequestBody,
        city: RequestBody,
        address: RequestBody,
        telepon: RequestBody,
        description: RequestBody,
        startTime: RequestBody,
        EndTime: RequestBody,
        StartDay: RequestBody,
        EndDay: RequestBody,
        imageMultiPart: MultipartBody.Part?
    ) {
        editProfileViewModel.editDataUser(
            name = name,
            negara = state,
            kota = city,
            deskripsi_alamat = address,
            telepon = telepon,
            deskripsi = description,
            jam_buka = startTime,
            jam_tutup = EndTime,
            hari_buka_awal = StartDay,
            hari_buka_akhir = EndDay,
            file = imageMultiPart
        ).observe(requireActivity()) {
            it?.let { response ->
                when(response) {
                    is ApiResult.Loading -> {
                        binding.lottieLoading.visibility = View.VISIBLE
                        binding.btnSaveProfile.visibility = View.GONE
                    }

                    is ApiResult.Success -> {
                        binding.lottieLoading.visibility = View.GONE
                        binding.btnSaveProfile.visibility = View.VISIBLE
                        requireActivity().onBackPressedDispatcher.onBackPressed()
                        Toast.makeText(requireActivity(), response.data.message, Toast.LENGTH_SHORT).show()
                    }

                    is ApiResult.Error -> {
                        binding.lottieLoading.visibility = View.GONE
                        binding.btnSaveProfile.visibility = View.VISIBLE
                        Toast.makeText(requireActivity(), response.error, Toast.LENGTH_SHORT).show()
                        Log.d(TAG, response.error)
                    }
                    else -> {
                        Toast.makeText(requireActivity(), "Failed, try again", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun setupData() {
        val dataDetailUser = EditProfileFragmentArgs.fromBundle(arguments as Bundle).detailUser

        dataDetailUser.let { user ->
            if (user.role == "user") {
                binding.apply {
                    titleEditOperational.visibility = View.GONE
                    titleEditDeskripsi.visibility = View.GONE
                    edEditDescription.visibility = View.GONE
                    edEditStartDay.visibility = View.GONE
                    edEditEndDay.visibility = View.GONE
                    edEditTimeStart.visibility = View.GONE
                    edEditTimeEnd.visibility = View.GONE
                    textView8.visibility = View.GONE
                    textView9.visibility = View.GONE
                    btnChangeImg.visibility = View.GONE

                    edEditName.setText(user.name)
                    edEditTelepon.setText(user.telepon)
                    edEditAddress.setText(user.alamat.deskripsiAlamat)
                    edEditKota.setText(user.alamat.kota)
                    autoCompleteTextState.setText(user.alamat.negara)
                    binding.imgProfile.setImageDrawable(
                        ContextCompat.getDrawable(
                            binding.imgProfile.context,
                            R.drawable.default_user_biasa
                        )
                    )
                    binding.imgProfile.elevation = 0F
                }
            } else {
                binding.apply {
                    edEditName.setText(user.name)
                    edEditTelepon.setText(user.telepon)
                    edEditAddress.setText(user.alamat.deskripsiAlamat)
                    edEditKota.setText(user.alamat.kota)
                    autoCompleteTextState.setText(user.alamat.negara)
                    if (user.deskripsi == null) edEditDescription.setText("") else edEditDescription.setText(
                        user.deskripsi
                    )
                    if (user.jamOperasional?.hariBukaAwal == null) autoCompleteTextStartDay.setText(
                        ""
                    ) else autoCompleteTextStartDay.setText(user.jamOperasional?.hariBukaAwal)
                    if (user.jamOperasional?.hariBukaAkhir == null) autoCompleteTextEndDay.setText("") else autoCompleteTextEndDay.setText(
                        user.jamOperasional?.hariBukaAkhir
                    )
                    if (user.jamOperasional?.jamBuka == null) edEditTimeStart.setText("") else edEditTimeStart.setText(
                        user.jamOperasional?.jamBuka
                    )
                    if (user.jamOperasional?.jamTutup == null) edEditTimeEnd.setText("") else edEditTimeEnd.setText(
                        user.jamOperasional?.jamTutup
                    )
                }

                if (user.gambarProfil != null) {
                    Glide.with(this)
                        .load(user.gambarProfil)
                        .into(binding.imgProfile)
                    binding.imgProfile.elevation = 6F

                } else {
                    binding.imgProfile.setImageDrawable(
                        ContextCompat.getDrawable(
                            binding.imgProfile.context,
                            R.drawable.default_img_toko
                        )
                    )
                    binding.imgProfile.elevation = 6F
                }
            }

        }
        val states = listOf("Indonesia", "Jepang")
        val adapterState = ArrayAdapter(requireActivity(), R.layout.list_select, states)
        (binding.autoCompleteTextState as? AutoCompleteTextView)?.setAdapter(adapterState)

        val operationalDay =
            listOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")
        val adapterOperationalDay = ArrayAdapter(requireActivity(), R.layout.list_select, operationalDay)
        (binding.autoCompleteTextStartDay as? AutoCompleteTextView)?.setAdapter(
            adapterOperationalDay
        )
        (binding.autoCompleteTextEndDay as? AutoCompleteTextView)?.setAdapter(adapterOperationalDay)
    }


    private fun setupViewModel() {
        editProfileViewModel = ViewModelProvider(
            requireActivity(),
            ProfileViewModelFactory.getInstance(requireActivity())
        )[EditProfileViewModel::class.java]
    }

    companion object {
        private const val TAG = "EditProfileFragment"
    }
}