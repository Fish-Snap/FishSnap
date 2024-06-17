package com.example.fishsnap.ui.detection

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.fishsnap.auth.ApiClient
import com.example.fishsnap.databinding.FragmentDetectionBinding
import com.example.fishsnap.ui.ViewModelFactory
import com.example.fishsnap.utils.FishDetectionModel
import com.example.fishsnap.utils.getImageUri
import com.yalantis.ucrop.UCrop
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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

        viewModel.clearErrorMessage()

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

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
            showLoading(true)
            selectedImageUri?.let { uri ->
                analyzeImage(uri)
            } ?: run {
                showLoading(false)
                Toast.makeText(requireContext(), "No image selected", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.scanResponse.observe(viewLifecycleOwner) { response ->
            showLoading(false)
            if (response.isSuccessful) {
                val fishData = response.body()?.data
                fishData?.let {
                    val annotatedImagePath =
                        saveBitmap(binding.previewImageView.drawable.toBitmap())
                    it.annotatedImagePath = annotatedImagePath
                    val action =
                        DetectionFragmentDirections.actionDetectionFragmentToDetailFishFragment(it)
                    findNavController().navigate(action)
                }
            } else {
                Toast.makeText(requireContext(), "Error: ${response.message()}", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            showLoading(false)
            message?.let {
                if (it.isNotEmpty()) {
                    Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun analyzeImage(uri: Uri) {
        val path = getPathFromUri(requireContext(), uri)
        val file = File(path)
        if (!file.exists()) {
            Toast.makeText(requireContext(), "File does not exist", Toast.LENGTH_SHORT).show()
            return
        }
        val imageBitmap = BitmapFactory.decodeFile(path)
        val fishDetectionModel = FishDetectionModel(requireContext())
        val (_, confidence) = fishDetectionModel.detectFish(imageBitmap)
        val bitmapWithBoundingBox = fishDetectionModel.drawBoundingBox(imageBitmap, confidence)

        // Display the annotated image if fish is detected
        Handler(Looper.getMainLooper()).postDelayed({
        if (confidence > 0.5) { // assuming 0.5 as threshold for detection
            binding.previewImageView.setImageBitmap(bitmapWithBoundingBox)
            // Save the annotated image and update urlImg with its path
            val annotatedImagePath = saveBitmap(bitmapWithBoundingBox)
            updateUrlImg(file, annotatedImagePath)
        } else {
            showLoading(false)
            Toast.makeText(requireContext(), "Ikan tidak terdeteksi", Toast.LENGTH_SHORT).show()
        }
        }, 2000)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.loadingButton.root.visibility = View.VISIBLE
        } else {
            binding.loadingButton.root.visibility = View.GONE
        }
    }

    private fun saveBitmap(bitmap: Bitmap): String {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val fileName = "IMG_$timeStamp.jpg"
        val file = File(requireContext().filesDir, fileName)
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
        }
        return file.absolutePath
    }

    private fun updateUrlImg(originalFile: File, annotatedImagePath: String) {
        val token = sharedPreferences.getString("TOKEN", "") ?: return
        val requestFile = originalFile.asRequestBody("image/*".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("file", originalFile.name, requestFile)
        viewModel.scanFish(body, "Bearer $token", annotatedImagePath)
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
}


