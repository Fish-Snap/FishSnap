package com.example.fishsnap

import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.fishsnap.data.dummy.CarouselItem
import com.example.fishsnap.data.dummy.ItemDetail
import com.example.fishsnap.databinding.FragmentDetailCarouselBinding

class DetailCarouselFragment : Fragment() {
    private var _binding: FragmentDetailCarouselBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailCarouselBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        arguments?.let {
            val item = DetailCarouselFragmentArgs.fromBundle(it).carouselItem
            displayItemDetails(item)
        }
    }

    private fun displayItemDetails(item: CarouselItem) {
        with(binding) {
            tvTitle.text = item.title
            tvDescription.text = item.description
            tvJudul.text = item.type
            tvSource.text = item.sumber

            Glide.with(this@DetailCarouselFragment)
                .load(item.imageUrl)
                .transform(CenterCrop(), RoundedCorners(22))
                .into(ivImage)

            when (item.itemType) {
                "manfaat" -> {
                    item.manfaat?.let { details ->
                        tvDeskripsi.text = createSpannableText(details)
                    }
                }

                "fakta" -> {
                    item.fakta?.let { details ->
                        tvDeskripsi.text = createSpannableText(details)
                    }
                }

                else -> {}
            }
        }
    }

    private fun createSpannableText(details: List<ItemDetail>): SpannableStringBuilder {
        val spannableBuilder = SpannableStringBuilder()
        details.forEach { detail ->
            val title = "${detail.judul}\n"
            val description = "${detail.deskripsi}\n\n"

            val start = spannableBuilder.length
            spannableBuilder.append(title)
            spannableBuilder.setSpan(
                StyleSpan(Typeface.BOLD),
                start,
                start + title.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            spannableBuilder.append(description)
        }
        return spannableBuilder
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}