package com.abolfazloskooii.nikeshop.Model.resource

import com.abolfazloskooii.nikeshop.Model.Checkout
import com.abolfazloskooii.nikeshop.Model.OrderHistory
import com.abolfazloskooii.nikeshop.Model.OrderItem
import com.abolfazloskooii.nikeshop.Model.SubmitOrderResult
import com.abolfazloskooii.nikeshop.Model.resource.DataSource.PaymentDataSource
import com.abolfazloskooii.nikeshop.Servies.http.ApiServies
import com.google.gson.JsonObject
import io.reactivex.rxjava3.core.Single

class ServerPaymentDataSource(private val apiServies: ApiServies) : PaymentDataSource {
    override fun submit(jsonObject: JsonObject): Single<SubmitOrderResult> =
        apiServies.submitORDER(jsonObject)

    override fun checkout(orderID: Int): Single<Checkout> = apiServies.checkout(orderID)

    override fun orderHistory(): Single<List<OrderHistory>> = apiServies.orderHistory()

}