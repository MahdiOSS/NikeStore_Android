package com.abolfazloskooii.nikeshop.Model.resource.DataSource

import com.abolfazloskooii.nikeshop.Model.*
import io.reactivex.rxjava3.core.Single

interface CartDataSource {

    fun addToCart (productId : Int) : Single<AddToCartResponse>
    fun getCart () : Single<CartResponse>
    fun removeCart (id : Int) : Single<MessageResponse>
    fun changeCartCount (cartId : Int , count: Int) : Single<AddToCartResponse>
    fun getCartCount () : Single<CartItemCount>

}