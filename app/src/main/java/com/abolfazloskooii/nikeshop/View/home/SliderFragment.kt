package com.abolfazloskooii.nikeshop.View.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.abolfazloskooii.nikeshop.Base.NikeImageView
import com.abolfazloskooii.nikeshop.Model.Banner
import com.abolfazloskooii.nikeshop.R
import com.abolfazloskooii.nikeshop.Servies.imageLoading.ImageLoadingServies
import com.facebook.drawee.view.SimpleDraweeView
import org.koin.android.ext.android.inject

class SliderFragment(private val banner: Banner) : Fragment() {
    private val imageLoader: ImageLoadingServies by inject()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val imageView =
            inflater.inflate(R.layout.fresco_load_image, container, false) as SimpleDraweeView
        banner.image?.let { imageLoader.loadImage(it, imageView as NikeImageView) }

        return imageView
    }

}