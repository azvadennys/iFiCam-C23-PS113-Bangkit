package org.apps.ifishcam.ui.detect_fish

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.google.firebase.auth.FirebaseAuth
import com.yalantis.ucrop.UCrop
import org.apps.ifishcam.ui.MainActivity
import org.apps.ifishcam.R
import org.apps.ifishcam.databinding.ActivityDetectFishBinding
import org.apps.ifishcam.model.User
import org.apps.ifishcam.model.UserPreference
import org.apps.ifishcam.ui.ChooseActivity
import org.apps.ifishcam.utils.createCustomTempFile
import org.apps.ifishcam.utils.uriToFile
import java.io.File


class DetectFishActivity : AppCompatActivity() {

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(android.Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

    private lateinit var binding: ActivityDetectFishBinding
    private var getFile: File? = null
    private lateinit var currentPhotoPath: String
    private val detectFishViewModel by viewModels<DetectFishViewModel>()
    private lateinit var userPref: UserPreference
    private lateinit var user: User
    private lateinit var auth: FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetectFishBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        auth = FirebaseAuth.getInstance()
        userPref = UserPreference(this)
        user = userPref.getUser()

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        binding.backButton.setOnClickListener{
            val intent = Intent(this@DetectFishActivity, ChooseActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }

        binding.galeriButton.setOnClickListener { startGallery() }
        binding.cameraButton.setOnClickListener { startCamera() }
        binding.scanButton.setOnClickListener { predictFish() }

        detectFishViewModel.isLoading.observe(this){
            showLoading(it)
        }

        detectFishViewModel.isError.observe(this){
            showError(it)
        }

        detectFishViewModel.predictResponse.observe(this){ predictResponse ->
            if (predictResponse != null) {
                val intent = Intent(this@DetectFishActivity, DetailDetectFishActivity::class.java)
                intent.putExtra(DetailDetectFishActivity.KEY_DETECT, predictResponse)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
                Toast.makeText(this, "Berhasil detect", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Gagal detect", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun predictFish() {
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        val uid = currentUser?.uid
        detectFishViewModel.predictImage(uid!!, getFile!!)
    }


    private fun startCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        createCustomTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this@DetectFishActivity,
                "org.apps.ifishcam.ui.camera",
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)
            getFile = myFile

            val result = BitmapFactory.decodeFile(getFile?.path)
            if (result.isRecycled){
                binding.previewImageView.setImageBitmap(result)
            }

            startCrop(Uri.fromFile(myFile))
        }
    }


    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg = result.data?.data as Uri
            selectedImg.let { uri ->
                val myFile = uriToFile(uri, this@DetectFishActivity)
                getFile = myFile

                val resultBitmap = BitmapFactory.decodeFile(myFile?.path)
                if (resultBitmap.isRecycled){
                    binding.previewImageView.setImageBitmap(resultBitmap)
                }

                startCrop(uri)
            }
        }
    }

    private fun startCrop(uri: Uri) {
        val destinationUri = Uri.fromFile(File(cacheDir, "cropped_image.jpg"))

        UCrop.of(uri, destinationUri)
            .withAspectRatio(1f, 1f)
            .start(this@DetectFishActivity)
        binding.previewImageView.setImageResource(0)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            val resultUri = UCrop.getOutput(data!!)
            binding.previewImageView.setImageURI(resultUri)
            Log.d("ImageURI", resultUri.toString())
        } else if (resultCode == UCrop.RESULT_ERROR) {
            val error = UCrop.getError(data!!)
            Log.d("ImageURI", error.toString())
        }else if (resultCode == RESULT_CANCELED && requestCode == UCrop.REQUEST_CROP) {
            binding.previewImageView.setImageResource(R.drawable.ic_insert_photo)
        }

    }

    private fun showLoading(isLoading: Boolean){
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showError(isError: Boolean){
        if (isError){
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Server Bermasalah")
            builder.setMessage("Tidak dapat menampilkan Halaman Detail")

            builder.setPositiveButton("OK") { _, _ ->
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }

            val alertDialog = builder.create()
            alertDialog.show()

            Toast.makeText(this, "History ditambahkan", Toast.LENGTH_SHORT).show()
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

}