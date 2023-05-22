package com.abolfazloskooii.nikeshop.Servies.imageLoading

import android.widget.ImageView
import com.abolfazloskooii.nikeshop.Base.NikeImageView
import com.facebook.drawee.view.SimpleDraweeView

interface ImageLoadingServies {
    fun loadImage (uri : String,imageView: NikeImageView)
}