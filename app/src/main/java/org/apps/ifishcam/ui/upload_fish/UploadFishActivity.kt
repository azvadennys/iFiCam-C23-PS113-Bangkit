package org.apps.ifishcam.ui.upload_fish

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import org.apps.ifishcam.ui.MainActivity
import org.apps.ifishcam.databinding.ActivityUploadFishBinding
import org.apps.ifishcam.model.User
import org.apps.ifishcam.model.UserPreference
import org.apps.ifishcam.ui.ChooseActivity
import org.apps.ifishcam.utils.createCustomTempFile
import org.apps.ifishcam.utils.uriToFile
import java.io.File

class UploadFishActivity : AppCompatActivity() {

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(android.Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

    private lateinit var binding: ActivityUploadFishBinding
    private lateinit var currentPhotoPath: String
    private val uploadFishViewModel by viewModels<UploadFishViewModel>()
    private lateinit var user: User
    private lateinit var userPref: UserPreference
    private lateinit var auth: FirebaseAuth
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var latitude: String? = null
    private var longitude: String? = null
    private var getFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadFishBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        auth = FirebaseAuth.getInstance()
        userPref = UserPreference(this)
        user = userPref.getUser()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        binding.backButton.setOnClickListener{
            val intent = Intent(this@UploadFishActivity, ChooseActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }

        binding.cameraButton.setOnClickListener { requestCameraPermission() }
        binding.galeriButton.setOnClickListener { startGallery() }
        binding.postButton.setOnClickListener { uploadImage() }

        uploadFishViewModel.isLoading.observe(this) {
            showLoading(it)
        }

    }

    private fun startGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private fun uploadImage() {
        val nama = binding.edNama.text.toString()
        val description = binding.edDeskripsi.text.toString()
        val alamat = binding.edAlamat.text.toString()
        if (nama.isNotEmpty() && description.isNotEmpty() && alamat.isNotEmpty() && getFile != null) {
            getMyLastLocationAndUpload(nama, description, alamat)
            finish()
        } else {
            Toast.makeText(this, "Lengkapi Data Terlebih Dahulu!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getMyLastLocationAndUpload(nama: String, description: String, alamat: String) {
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    auth = FirebaseAuth.getInstance()
                    val currentUser = auth.currentUser
                    val uid = currentUser?.uid

                    val latitude = location.latitude.toString()
                    val longitude = location.longitude.toString()
                    uploadFishViewModel.uploadStory(uid!!, getFile!!, nama, description, alamat, latitude, longitude)
                    Toast.makeText(this, "Berhasil Upload", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@UploadFishActivity, MainActivity::class.java))
                } else{
                    Toast.makeText(this@UploadFishActivity, "Upload Gagal, Lokasi tidak tersedia", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            // Meminta izin akses lokasi
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
            Toast.makeText(this@UploadFishActivity, "Nyalahkan Lokasi Terlebih Dahulu!", Toast.LENGTH_SHORT).show()
        }
    }


    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false &&
                        permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    getMyLastLocationAndUpload(
                        binding.edNama.text.toString(),
                        binding.edDeskripsi.text.toString(),
                        binding.edAlamat.text.toString()
                    )
                }
                else -> {
                    Toast.makeText(this@UploadFishActivity, "Izin ditolak!", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }


    private fun requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), REQUEST_CODE_PERMISSIONS)
        } else {
            startCamera()
        }
    }

    private fun startCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        createCustomTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this@UploadFishActivity,
                "org.apps.ifishcam.ui.camera",
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)
            getFile = myFile

            val result = BitmapFactory.decodeFile(getFile?.path)
            binding.previewImageView.setImageBitmap(result)
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg = result.data?.data as Uri
            selectedImg.let { uri ->
                val myFile = uriToFile(uri, this@UploadFishActivity)
                getFile = myFile
                binding.previewImageView.setImageURI(uri)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    "Tidak mendapatkan permission",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}

