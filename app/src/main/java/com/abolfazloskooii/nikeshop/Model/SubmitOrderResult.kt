package com.abolfazloskooii.nikeshop.Model

data class SubmitOrderResult(
    val bank_gateway_url: String,
    val order_id: Int
)