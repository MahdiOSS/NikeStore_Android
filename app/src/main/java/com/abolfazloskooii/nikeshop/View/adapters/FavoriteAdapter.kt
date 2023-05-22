package com.abolfazloskooii.nikeshop.View.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.abolfazloskooii.nikeshop.Base.NikeImageView
import com.abolfazloskooii.nikeshop.Model.Product
import com.abolfazloskooii.nikeshop.R
import com.abolfazloskooii.nikeshop.Servies.imageLoading.ImageLoadingServies

class FavoriteAdapter(private val product: ArrayList<Product>,val imageLoadingServies: ImageLoadingServies,private val eventListener: EventListener) : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>(){

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val title = itemView.findViewById<TextView>(R.id.productTitleTv)
        val img = itemView.findViewById<NikeImageView>(R.id.nikeImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_favorite_product,parent,false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
      val current = product[position]

        holder.title.text = current.title
        imageLoadingServies.loadImage(current.image.toString(),holder.img)

        holder.title.setOnClickListener { eventListener.onClick(current) }
        holder.title.setOnLongClickListener {
            eventListener.onLongClick(current,position)
            false
        }

    }

    override fun getItemCount(): Int = product.size

    fun remove(product: Product,position: Int){
        this.product.remove(product)
        notifyItemRemoved(position)
    }

    interface EventListener{
        fun onClick(product: Product)
        fun onLongClick(product: Product,position: Int)
    }

}