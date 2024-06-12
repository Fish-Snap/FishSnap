package com.example.fishsnap.ui.detection

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.fishsnap.R
import com.example.fishsnap.auth.ApiClient
import com.example.fishsnap.auth.repository.FishRepository
import com.yalantis.ucrop.UCrop
import com.example.fishsnap.databinding.FragmentDetectionBinding
import com.example.fishsnap.ui.ViewModelFactory
import com.example.fishsnap.utils.getImageUri
import com.example.fishsnap.utils.getPathFromUri
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.util.Date

class DetectionFragment : Fragment() {
    private var _binding: FragmentDetectionBinding? = null
    private val binding get() = _binding!!

    private val sharedPreferences by lazy {
        requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    }

    private val viewModel: DetectionViewModel by viewModels {
        ViewModelFactory(ApiClient.apiService, sharedPreferences)
    }

    private var selectedImageUri: Uri? = null
    private var currentUri: Uri? = null

    private val launchGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentUri = uri
            withUCrop(currentUri!!)
        }
    }

    private val launchCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            withUCrop(currentUri!!)
        }
    }

    private val launchCrop =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val resultUri = UCrop.getOutput(result.data!!)
                if (resultUri != null) {
                    selectedImageUri = resultUri
                    setPreview()
                }
            }
        }

    private fun withUCrop(uri: Uri) {
        val timeStamp = Date().time
        val cachedImage = File(requireActivity().cacheDir, "cropped_image_${timeStamp}.jpg")
        val destinationUri = Uri.fromFile(cachedImage)
        val uCrop = UCrop.of(uri, destinationUri).withAspectRatio(1f, 1f)

        uCrop.getIntent(requireContext()).apply {
            launchCrop.launch(this)
        }
    }

    private fun setPreview() {
        Glide.with(requireContext())
            .load(selectedImageUri)
            .centerCrop()
            .transform(RoundedCorners(32))
            .into(binding.previewImageView)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().finish()
            }
        })

        binding.btnGallery.setOnClickListener {
            launchGallery.launch(
                PickVisualMediaRequest(
                    ActivityResultContracts.PickVisualMedia.ImageOnly
                )
            )
        }

        binding.btnCamera.setOnClickListener {
            currentUri = getImageUri(requireActivity())
            launchCamera.launch(currentUri)
        }

        binding.btnAnalyze.setOnClickListener {
            selectedImageUri?.let { uri ->
                analyzeImage(uri)
            } ?: run {
                Toast.makeText(requireContext(), "No image selected", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.scanResponse.observe(viewLifecycleOwner, Observer { response ->
            if (response.isSuccessful) {
                val fishData = response.body()?.data
                fishData?.let {
                    val action = DetectionFragmentDirections.actionDetectionFragmentToDetailFishFragment(it)
                    findNavController().navigate(action)
                }
            } else {
                Toast.makeText(requireContext(), "Error: ${response.message()}", Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.errorMessage.observe(viewLifecycleOwner, Observer { message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        })
    }

    private fun analyzeImage(uri: Uri) {
        val token = sharedPreferences.getString("TOKEN", "") ?: return
        val path = getPathFromUri(requireContext(), uri)
        val file = File(path)
        if (!file.exists()) {
            Toast.makeText(requireContext(), "File does not exist", Toast.LENGTH_SHORT).show()
            return
        }
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
        viewModel.scanFish(body, "Bearer $token")
    }

    private fun getPathFromUri(context: Context, uri: Uri): String {
        var filePath: String? = null
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        if (cursor != null) {
            cursor.moveToFirst()
            val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            if (idx != -1) {
                filePath = cursor.getString(idx)
            }
            cursor.close()
        }
        return filePath ?: uri.path!!
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val REQUEST_GALLERY = 1001
        private const val REQUEST_CAMERA = 1002
    }
}

