package com.example.fishsnap.ui.detection

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.fishsnap.R
import com.yalantis.ucrop.UCrop
import com.example.fishsnap.databinding.FragmentDetectionBinding
import com.example.fishsnap.utils.getImageUri
import java.io.File
import java.util.Date

class DetectionFragment : Fragment() {
    private var _binding: FragmentDetectionBinding? = null
    private val binding get() = _binding!!
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
    ) { isSucces ->
        if (isSucces){
            withUCrop(currentUri!!)
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

    private val launchCrop =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val resultUri = UCrop.getOutput(result.data!!)
                if (resultUri != null) {
                    currentUri = resultUri
                    setPreview()
                }
            }
        }

    private fun setPreview() {
        Glide.with(requireContext())
            .load(currentUri)
            .centerCrop()
            .transform(RoundedCorners(32))
            .into(binding.previewImageView)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
    }

}