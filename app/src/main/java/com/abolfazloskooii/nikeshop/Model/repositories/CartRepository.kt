package com.abolfazloskooii.nikeshop.Model.repositories

import com.abolfazloskooii.nikeshop.Model.AddToCartResponse
import com.abolfazloskooii.nikeshop.Model.CartItemCount
import com.abolfazloskooii.nikeshop.Model.CartResponse
import com.abolfazloskooii.nikeshop.Model.MessageResponse
import com.abolfazloskooii.nikeshop.Model.repositories.Manager.CartRepositoryManager
import com.abolfazloskooii.nikeshop.Model.resource.DataSource.CartDataSource
import com.abolfazloskooii.nikeshop.Model.resource.ServerCartDataSource
import com.google.gson.JsonObject
import io.reactivex.rxjava3.core.Single

class CartRepository(private val serverCartDataSource: CartDataSource) : CartRepositoryManager {

    override fun addToCart(productId: Int): Single<AddToCartResponse> {
        return serverCartDataSource.addToCart(productId)
    }

    override fun getCart(): Single<CartResponse> = serverCartDataSource.getCart()

    override fun removeCart(id: Int): Single<MessageResponse> =
        serverCartDataSource.removeCart(id)

    override fun changeCartCount(
        cartId: Int,
        count: Int
    ): Single<AddToCartResponse> = serverCartDataSource.changeCartCount(cartId,count)

    override fun getCartCount(): Single<CartItemCount> = serverCartDataSource.getCartCount()

}