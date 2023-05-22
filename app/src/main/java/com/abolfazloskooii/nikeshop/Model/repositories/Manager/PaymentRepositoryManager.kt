package com.abolfazloskooii.nikeshop.Model.repositories.Manager

import com.abolfazloskooii.nikeshop.Model.Checkout
import com.abolfazloskooii.nikeshop.Model.OrderHistory
import com.abolfazloskooii.nikeshop.Model.OrderItem
import com.abolfazloskooii.nikeshop.Model.SubmitOrderResult
import com.google.gson.JsonObject
import io.reactivex.rxjava3.core.Single

interface PaymentRepositoryManager {

    fun submit (jsonObject: JsonObject) : Single<SubmitOrderResult>

    fun checkout (orderID: Int) : Single<Checkout>

    fun orderHistory () : Single<List<OrderHistory>>

}