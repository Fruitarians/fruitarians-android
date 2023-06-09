package com.althaaf.fruitarians.ui.fruitscan

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Camera
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.althaaf.fruitarians.R
import com.althaaf.fruitarians.core.data.network.retrofit.ApiResult
import com.althaaf.fruitarians.core.helper.ScanViewModelFactory
import com.althaaf.fruitarians.core.helper.reduceFileImage
import com.althaaf.fruitarians.core.helper.rotateFile
import com.althaaf.fruitarians.core.helper.uriToFile
import com.althaaf.fruitarians.databinding.ActivityFruitScanBinding
import com.althaaf.fruitarians.ui.product.ProductAddUpdateActivity
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class FruitScanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFruitScanBinding
    private lateinit var fruitScanViewModel: FruitScanViewModel
    private var getFile: File? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFruitScanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        setupViewModel()
        setupAction()
    }

    private fun setupViewModel() {
        fruitScanViewModel = ViewModelProvider(
            this,
            ScanViewModelFactory.getInstance()
        )[FruitScanViewModel::class.java]
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(this, "Didn't get a permissions", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private val launcherIntentCameraX =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == CAMERA_X_RESULT) {
                val myFile = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    it.data?.getSerializableExtra("picture", File::class.java)
                } else {
                    @Suppress("DEPRECATION")
                    it.data?.getSerializableExtra("picture")
                } as? File
                val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean
                myFile?.let { file ->
                    rotateFile(file, isBackCamera)
                    getFile = myFile
                    binding.imagePreview.setImageBitmap(BitmapFactory.decodeFile(file.path))
                }
            }
        }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg = result.data?.data as Uri

            selectedImg.let { uri ->
                val myFile = uriToFile(uri, this)
                getFile = myFile
                binding.imagePreview.setImageURI(uri)
            }
        }
    }

    private fun setupAction() {
        binding.btnUploadCamera.setOnClickListener {
            val intent = Intent(this@FruitScanActivity, CameraActivity::class.java)
            launcherIntentCameraX.launch(intent)
        }

        binding.btnUploadFromGallery.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            val chooser = Intent.createChooser(intent, "Choose a Picture")
            launcherIntentGallery.launch(chooser)
        }

        binding.btnUploadScan.setOnClickListener {
            requestScan()
        }

        binding.btnBack.setOnClickListener {
            this.onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun requestScan() {
        if (getFile == null) {
            Toast.makeText(this, "Please insert an image file first", Toast.LENGTH_SHORT).show()
        } else {
            val file = reduceFileImage(getFile as File)
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "image_data",
                file.name,
                requestImageFile
            )

            uploadScan(imageMultipart)
        }
    }

    private fun uploadScan(imageMultipart: MultipartBody.Part) {
        fruitScanViewModel.fruitScanDetection(imageMultipart).observe(this) {
            it?.let { response ->
                when (response) {
                    is ApiResult.Loading -> {
                        binding.lottieLoading.visibility = View.VISIBLE
                        binding.btnUploadScan.visibility = View.GONE
                        binding.resultScan.visibility = View.GONE
                    }

                    is ApiResult.Success -> {
                        binding.lottieLoading.visibility = View.GONE
                        binding.btnUploadScan.visibility = View.VISIBLE
                        binding.resultScan.visibility = View.VISIBLE
                        binding.resultScan.text = getString(R.string.scan_result, response.data.modelPrediction, response.data.modelPredictionConfidenceScore)
                    }

                    is ApiResult.Error -> {
                        binding.lottieLoading.visibility = View.GONE
                        binding.btnUploadScan.visibility = View.VISIBLE
                        Toast.makeText(this, response.error, Toast.LENGTH_SHORT).show()
                        binding.resultScan.visibility = View.VISIBLE
                        binding.resultScan.text = response.error
                        Log.d(TAG, response.error)
                    }
                    else -> {
                        Toast.makeText(this, "Failed, try again", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        private const val TAG = "FruitScanActivity"
        const val CAMERA_X_RESULT = 200

        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}