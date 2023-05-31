package org.apps.ifishcam.ui.detect_fish

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.yalantis.ucrop.UCrop
import org.apps.ifishcam.MainActivity
import org.apps.ifishcam.R
import org.apps.ifishcam.databinding.ActivityDetectFishBinding
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetectFishBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        binding.backButton.setOnClickListener{
            val intent = Intent(this@DetectFishActivity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }

        binding.galeriButton.setOnClickListener { startGallery() }
        binding.cameraButton.setOnClickListener { startCamera() }
    }

    private fun startCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        createCustomTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this@DetectFishActivity,
                "org.apps.ifishcam.ui.detect_fish",
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
            .withAspectRatio(1f, 1f) // Sesuaikan rasio aspek sesuai kebutuhan Anda
            .start(this@DetectFishActivity)
        binding.previewImageView.setImageResource(0)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            val resultUri = UCrop.getOutput(data!!)
            // Tampilkan hasil crop pada ImageView atau lakukan operasi lain sesuai kebutuhan Anda
            binding.previewImageView.setImageURI(resultUri)
            Log.d("ImageURI", resultUri.toString())
        } else if (resultCode == UCrop.RESULT_ERROR) {
            val error = UCrop.getError(data!!)
            // Tangani error yang terjadi selama proses pemotongan gambar
        }else if (resultCode == RESULT_CANCELED && requestCode == UCrop.REQUEST_CROP) {
            // Set the default image to the previewImage when the crop operation is canceled
            binding.previewImageView.setImageResource(R.drawable.ic_insert_photo)
        }

    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

}