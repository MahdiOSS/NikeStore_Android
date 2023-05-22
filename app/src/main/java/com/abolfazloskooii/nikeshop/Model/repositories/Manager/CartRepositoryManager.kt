package com.abolfazloskooii.nikeshop.Model.repositories.Manager

import com.abolfazloskooii.nikeshop.Model.AddToCartResponse
import com.abolfazloskooii.nikeshop.Model.CartItemCount
import com.abolfazloskooii.nikeshop.Model.CartResponse
import com.abolfazloskooii.nikeshop.Model.MessageResponse
import io.reactivex.rxjava3.core.Single

interface CartRepositoryManager {

    fun addToCart (productId : Int) : Single<AddToCartResponse>
    fun getCart () : Single<CartResponse>
    fun removeCart (id : Int) : Single<MessageResponse>
    fun changeCartCount (cartId : Int , count: Int) : Single<AddToCartResponse>
    fun getCartCount () : Single<CartItemCount>

}