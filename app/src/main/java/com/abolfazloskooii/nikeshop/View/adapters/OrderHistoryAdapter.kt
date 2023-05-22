package com.abolfazloskooii.nikeshop.View.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.abolfazloskooii.nikeshop.Base.NikeImageView
import com.abolfazloskooii.nikeshop.Base.convertDpToPixel
import com.abolfazloskooii.nikeshop.Base.formatPrice
import com.abolfazloskooii.nikeshop.Model.OrderHistory
import com.abolfazloskooii.nikeshop.R
import kotlinx.android.extensions.LayoutContainer

class OrderHistoryAdapter(private val orderList: ArrayList<OrderHistory>, val context: Context) :
    RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder>() {

    inner class ViewHolder(override val containerView: View?) :
        RecyclerView.ViewHolder(containerView!!), LayoutContainer {

        val id = containerView?.findViewById<TextView>(R.id.orderId)
        val price = containerView?.findViewById<TextView>(R.id.price_order)
        val linearLayout = containerView?.findViewById<LinearLayout>(R.id.img_order_ll)

        val size = convertDpToPixel(100f, context).toInt()
        private val margin = convertDpToPixel(8f, context).toInt()
        val layoutParams = LinearLayout.LayoutParams(size, size)
            .apply {
                marginEnd = margin
                marginStart = margin
            }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_order_history, parent, false)
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = orderList[position]

        holder.id!!.text = current.id.toString()
        holder.price!!.text = formatPrice(current.payable)
        holder.linearLayout?.removeAllViews()

        current.order_items.forEach { it ->
            val imageView = NikeImageView(context)
            imageView.setImageURI(it.product.image)
            imageView.layoutParams = holder.layoutParams
            holder.linearLayout?.addView(imageView)

        }

    }

    override fun getItemCount(): Int = orderList.size

}