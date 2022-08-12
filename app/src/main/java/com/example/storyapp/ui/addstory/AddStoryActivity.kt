package com.example.storyapp.ui.addstory

import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.example.storyapp.data.entity.AddStoryResponse
import com.example.storyapp.data.remote.ApiConfig
import com.example.storyapp.databinding.ActivityAddStoryBinding
import com.example.storyapp.helper.Const.TOKEN
import com.example.storyapp.helper.createCustomTempFile
import com.example.storyapp.helper.reduceFileImage
import com.example.storyapp.helper.uriToFile
import com.example.storyapp.ui.liststory.ListStoryActivity
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class AddStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddStoryBinding

    private var photo: File? = null

    private lateinit var photoPath: String

    private lateinit var text: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            btnCamera.setOnClickListener {
                startTakePhoto()
            }

            btnGallery.setOnClickListener {
                startGallery()
            }
            btnSubmit.setOnClickListener {
                text = edtDescription.text.toString()
                uploadImage()
            }
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(photoPath)
            photo = myFile

            val result = BitmapFactory.decodeFile(photo?.path)
            binding.imgPhoto.setImageBitmap(result)
        }
    }

    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        createCustomTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this,
                "com.example.storyapp",
                it
            )
            photoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            photo = uriToFile(selectedImg, this@AddStoryActivity)
            binding.imgPhoto.setImageURI(selectedImg)
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private fun uploadImage() {
        if (photo != null) {
            val file = reduceFileImage(photo as File)

            val description = text.toRequestBody("text/plain".toMediaType())
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile
            )

            val client = ApiConfig.getApiService().addStory(TOKEN ,imageMultipart, description)

            client.enqueue(object : Callback<AddStoryResponse> {
                override fun onResponse(
                    call: Call<AddStoryResponse>,
                    response: Response<AddStoryResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null && !responseBody.error!!) {
                            Toast.makeText(this@AddStoryActivity, responseBody.message, Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@AddStoryActivity, ListStoryActivity::class.java)
                            startActivity(intent)
                        }
                    } else {
                        makeToast(response.message())
                    }
                }

                override fun onFailure(call: Call<AddStoryResponse>, t: Throwable) {
                    makeToast("Gagal mengupload story")
                }
            })
        } else {
            makeToast("Silakan masukkan berkas gambar terlebih dahulu")
        }
    }

    private fun makeToast(message: String) {
        Toast.makeText(
            this@AddStoryActivity,
            message,
            Toast.LENGTH_SHORT
        ).show()
    }
}