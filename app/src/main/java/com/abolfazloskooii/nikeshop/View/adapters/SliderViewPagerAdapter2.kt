package com.abolfazloskooii.nikeshop.View.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.abolfazloskooii.nikeshop.Base.NikeImageView
import com.abolfazloskooii.nikeshop.Model.Banner
import com.abolfazloskooii.nikeshop.R
import com.abolfazloskooii.nikeshop.Servies.imageLoading.ImageLoadingServies

class SliderViewPagerAdapter2(
    private val viewPager2: ViewPager2,
    private var list: ArrayList<Banner>,
    private val imageLoadingServies: ImageLoadingServies,
    private val height: Int
) :
    RecyclerView.Adapter<SliderViewPagerAdapter2.ViewHolder>() {


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val img: NikeImageView = itemView.findViewById(R.id.imgBanner)

        init {
            val params = img.layoutParams
            params.height = height
            img.layoutParams = params
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.image_container, parent, false)
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val current = list[position]
//Picture be shouid PNG
        imageLoadingServies.loadImage(current.image.toString(), holder.img)

        if (position == list.size) {
//            viewPager2.post(runnable)
        }


    }

    override fun getItemCount(): Int = list.size

    private val runnable = Runnable() {
        list.addAll(list)
        notifyDataSetChanged()
    }

}