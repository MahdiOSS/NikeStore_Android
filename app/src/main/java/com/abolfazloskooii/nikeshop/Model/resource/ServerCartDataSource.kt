package com.abolfazloskooii.nikeshop.Model.resource

import com.abolfazloskooii.nikeshop.Model.*
import com.abolfazloskooii.nikeshop.Model.resource.DataSource.CartDataSource
import com.abolfazloskooii.nikeshop.Servies.http.ApiServies
import com.google.gson.JsonObject
import io.reactivex.rxjava3.core.Single

class ServerCartDataSource(private val apiServies: ApiServies) : CartDataSource {

    override fun addToCart(productId: Int): Single<AddToCartResponse> {
        return apiServies.addToCart(JsonObject().apply { addProperty("product_id", productId) })
    }

    override fun getCart(): Single<CartResponse> = apiServies.getCart()

    override fun removeCart(id: Int): Single<MessageResponse> =
        apiServies.removeItemFromCart(JsonObject().apply {
            addProperty("cart_item_id", id)
        })

    override fun changeCartCount(
        cartId: Int,
        count: Int
    ): Single<AddToCartResponse> = apiServies.changeCount(JsonObject().apply {
        addProperty("cart_item_id", cartId)
        addProperty("count", count)
    })


    override fun getCartCount(): Single<CartItemCount> = apiServies.getCartItemsCount()

}