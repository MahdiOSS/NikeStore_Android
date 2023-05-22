package com.abolfazloskooii.nikeshop.View.adapters

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.abolfazloskooii.nikeshop.Base.NikeImageView
import com.abolfazloskooii.nikeshop.Base.formatPrice
import com.abolfazloskooii.nikeshop.Base.implementSpringAnimationTrait
import com.abolfazloskooii.nikeshop.Model.Product
import com.abolfazloskooii.nikeshop.R
import com.abolfazloskooii.nikeshop.Servies.imageLoading.ImageLoadingServies

const val SORT_BY_SMALL = 0
const val SORT_BY_NORMAL = 1
const val SORT_BY_LARGE = 2

class RvMainProductAdapter(
    var viewType: Int = SORT_BY_NORMAL,
    private val data : MutableList<Product>,
    private val imageLoader: ImageLoadingServies,
    private val onClickListener: OnClickListener
    ) : RecyclerView.Adapter<RvMainProductAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.productTitleTv)
        val lastPrice : TextView = itemView.findViewById(R.id.previousPriceTv)
        val price: TextView = itemView.findViewById(R.id.currentPriceTv)
        val imgProduct : NikeImageView = itemView.findViewById(R.id.productIv)
        val favorite : ImageView = itemView.findViewById(R.id.favoriteBtn)

        init {
            itemView.implementSpringAnimationTrait()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = when(viewType){
            0 -> R.layout.item_product_small
            1 -> R.layout.item_product
            2 -> R.layout.item_product_large
            else -> throw java.lang.IllegalStateException("Invalid res !")
        }
        return ViewHolder(LayoutInflater.from(parent.context).inflate(layoutInflater,parent,false))
    }




    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = data[position]

        holder.title.text = current.title
        holder.price.text = formatPrice(current.price!!)
        current.image?.let { imageLoader.loadImage(it,holder.imgProduct) }
        holder.lastPrice.text = current.previousPrice?.let { formatPrice(it) }
        holder.lastPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG

        holder.itemView.setOnClickListener {
            onClickListener.onClick(current)
        }
        holder.favorite.setOnClickListener{
            onClickListener.onFavoriteListener(current)
            current.isFavorite = !current.isFavorite
            notifyItemChanged(position)
        }

        if (current.isFavorite)
            holder.favorite.setImageResource(R.drawable.ic_baseline_favorite_24)
        else
            holder.favorite.setImageResource(R.drawable.ic_favorites)




    }

    override fun getItemCount(): Int = data.size

    interface OnClickListener{
        fun onClick(product: Product)
        fun onFavoriteListener(product: Product)
    }

    override fun getItemViewType(position: Int): Int {
        return viewType
    }

}