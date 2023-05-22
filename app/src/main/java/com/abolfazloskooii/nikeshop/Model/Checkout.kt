package com.abolfazloskooii.nikeshop.Model

data class Checkout(
    val payable_price: Int,
    val payment_status: String,
    val purchase_success: Boolean
)