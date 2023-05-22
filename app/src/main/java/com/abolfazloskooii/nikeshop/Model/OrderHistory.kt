package com.abolfazloskooii.nikeshop.Model

data class OrderHistory(
    val id : Int,
    val payable:Int,
    val order_items : List<OrderItem>
)
