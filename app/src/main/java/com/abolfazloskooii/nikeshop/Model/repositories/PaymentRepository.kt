package com.abolfazloskooii.nikeshop.Model.repositories

import com.abolfazloskooii.nikeshop.Model.Checkout
import com.abolfazloskooii.nikeshop.Model.OrderHistory
import com.abolfazloskooii.nikeshop.Model.OrderItem
import com.abolfazloskooii.nikeshop.Model.SubmitOrderResult
import com.abolfazloskooii.nikeshop.Model.repositories.Manager.PaymentRepositoryManager
import com.abolfazloskooii.nikeshop.Model.resource.DataSource.PaymentDataSource
import com.abolfazloskooii.nikeshop.Model.resource.ServerPaymentDataSource
import com.google.gson.JsonObject
import io.reactivex.rxjava3.core.Single

class PaymentRepository(private val serverPaymentDataSource: PaymentDataSource) : PaymentRepositoryManager {
    override fun submit(jsonObject: JsonObject): Single<SubmitOrderResult> = serverPaymentDataSource.submit(jsonObject)

    override fun checkout(orderID: Int): Single<Checkout> = serverPaymentDataSource.checkout(orderID)

    override fun orderHistory(): Single<List<OrderHistory>> = serverPaymentDataSource.orderHistory()


}