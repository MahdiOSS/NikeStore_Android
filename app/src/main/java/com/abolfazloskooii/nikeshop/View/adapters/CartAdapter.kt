package com.abolfazloskooii.nikeshop.View.adapters

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.abolfazloskooii.nikeshop.Base.NikeImageView
import com.abolfazloskooii.nikeshop.Base.formatPrice
import com.abolfazloskooii.nikeshop.Model.CartItem
import com.abolfazloskooii.nikeshop.Model.CartPurchase
import com.abolfazloskooii.nikeshop.R
import com.abolfazloskooii.nikeshop.Servies.imageLoading.ImageLoadingServies
import kotlinx.android.extensions.LayoutContainer

const val PURCHASE = 1
const val CART = 0

class CartAdapter(
    val cartItem: MutableList<CartItem>,
    private val imageLoadingServies: ImageLoadingServies,
    private val eventListener: EventListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var purchase: CartPurchase? = null

    inner class CartItemViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {
        val imageItem: NikeImageView = containerView.findViewById(R.id.productIv)
        val productTitleTv: TextView = containerView.findViewById(R.id.productTitleTv)
        val previousPriceTv: TextView = containerView.findViewById(R.id.previousPriceTv)
        val priceTv: TextView = containerView.findViewById(R.id.priceTv)
        val cartItemCountTv: TextView = containerView.findViewById(R.id.cartItemCountTv)
        val increaseBtn: ImageView = containerView.findViewById(R.id.increaseBtn)
        val decreaseBtn: ImageView = containerView.findViewById(R.id.decreaseBtn)
        val changeCountProgressBar: ProgressBar = containerView.findViewById(R.id.changeCountProgressBar)
        val removeFromCartBtn: TextView = containerView.findViewById(R.id.removeFromCartBtn)


    }

    class PurchaseViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {
        //        var totalPriceTv: TextView? = containerView?.totalPriceTv
//        var shippingCostTv: TextView? = containerView?.shippingCostTv
//        var payablePriceTv: TextView? = containerView?.payablePriceTv
        fun bind(totalPrice: Int, shippingCost: Int, payablePrice: Int) {
            containerView.findViewById<TextView>(R.id.totalPriceTv).apply {
                paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            }.text = formatPrice(totalPrice)
            containerView.findViewById<TextView>(R.id.shippingCostTv).text = formatPrice(shippingCost)
            containerView.findViewById<TextView>(R.id.payablePriceTv).text = formatPrice(payablePrice)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {

            PURCHASE -> {
                PurchaseViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_purchase_details, parent, false)
                )
            }
            CART -> {
                CartItemViewHolder(
                    LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
                )
            }

            else -> {
                "" as RecyclerView.ViewHolder
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (holder) {
            is PurchaseViewHolder -> {
                purchase.let {
                    if (it != null) {
                        holder.bind(it.total_price, it.shipping_cost, it.payable_price)
                    }
                }
            }
            is CartItemViewHolder -> {

                val item = cartItem[position]

                holder.changeCountProgressBar.visibility =
                    if (item.showProgress) View.VISIBLE else View.GONE

                holder.cartItemCountTv.visibility =
                    if (item.showProgress) View.INVISIBLE else View.VISIBLE

                holder.cartItemCountTv.text = item.count.toString()
                holder.previousPriceTv.text = item.product.previousPrice?.let { formatPrice(it) }
                holder.priceTv.text = formatPrice(item.product.price!!)
                holder.productTitleTv.text = item.product.title.toString()
                imageLoadingServies.loadImage(
                    item.product.image.toString(),
                    holder.imageItem
                )

                holder.increaseBtn.setOnClickListener {
                    if (item.count < 5) {
                        eventListener.increaseItem(item)
                        item.showProgress = true
                        holder.changeCountProgressBar.visibility = View.VISIBLE
                        holder.cartItemCountTv.visibility = View.INVISIBLE
                    }
                }
                holder.decreaseBtn.setOnClickListener {
                    if (item.count > 1) {
                        eventListener.decreaseItem(item)
                        item.showProgress = true
                        holder.changeCountProgressBar.visibility = View.VISIBLE
                        holder.cartItemCountTv.visibility = View.INVISIBLE
                    }
                }
                holder.removeFromCartBtn.setOnClickListener {
                    eventListener.removeFromCart(item)
                    item.showProgress = true
                    holder.changeCountProgressBar.visibility = View.VISIBLE
                    holder.cartItemCountTv.visibility = View.INVISIBLE
                }

                if (cartItem[position].showProgress)
                    holder.changeCountProgressBar.visibility = View.VISIBLE
                else holder.changeCountProgressBar.visibility = View.GONE

                holder.imageItem.setOnClickListener {
                    eventListener.imageClick(item)
                }

            }
        }
    }


    override fun getItemCount(): Int = cartItem.size + 1


    interface EventListener {
        fun increaseItem(cartItem: CartItem)
        fun decreaseItem(cartItem: CartItem)
        fun removeFromCart(cartItem: CartItem)
        fun imageClick(cartItem: CartItem)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == cartItem.size)
            PURCHASE
        else
            CART
    }

    fun removeItem(cartItem: CartItem) {
        val i = this.cartItem.indexOf(cartItem)
        if (i > -1) {
            this.cartItem[i].showProgress = false
            this.cartItem.removeAt(i)
            notifyItemRemoved(i)
        }
    }

    fun increaseAndDecrease(cartItem: CartItem) {
        val i = this.cartItem.indexOf(cartItem)
        if (i > -1) {
            this.cartItem[i].showProgress = false
            notifyItemChanged(i)
        }
    }
}