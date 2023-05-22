package com.abolfazloskooii.nikeshop.Servies.imageLoading

import com.abolfazloskooii.nikeshop.Base.NikeImageView
import com.abolfazloskooii.nikeshop.Base.TAG
import com.facebook.drawee.view.SimpleDraweeView
import timber.log.Timber

class ImageLoader : ImageLoadingServies {
    override fun loadImage(uri: String, imageView: NikeImageView) {
        if (imageView is SimpleDraweeView)
            imageView.setImageURI(uri)

    }
}