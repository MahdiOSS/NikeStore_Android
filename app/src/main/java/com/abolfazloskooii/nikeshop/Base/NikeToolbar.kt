package com.abolfazloskooii.nikeshop.Base

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.abolfazloskooii.nikeshop.R

class NikeToolbar(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {


    var onBackListener: View.OnClickListener? = null
        set(value) {
            field = value
            findViewById<ImageView>(R.id.back_comment).setOnClickListener(onBackListener)
        }

    init {
        inflate(context, R.layout.nike_toolbar_cus, this)

        if (attrs != null) {
            val attr = context.obtainStyledAttributes(attrs, R.styleable.NikeToolbar)
            val title = attr.getString(R.styleable.NikeToolbar_nike_toolbar_cus)
            if (title != null) {
                if (title.isNotEmpty())
                    findViewById<TextView>(R.id.cus_toolbar_title).text = title
            }
            attr.recycle()
        }
    }

}