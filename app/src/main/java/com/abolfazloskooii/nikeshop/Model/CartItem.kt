package com.abolfazloskooii.nikeshop.Model

import com.abolfazloskooii.nikeshop.Model.Product

data class CartItem(
    val cart_item_id: Int,
    var count: Int,
    val product: Product,
    var showProgress : Boolean
)