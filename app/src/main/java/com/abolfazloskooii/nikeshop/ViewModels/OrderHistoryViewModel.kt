package com.abolfazloskooii.nikeshop.ViewModels

import androidx.lifecycle.MutableLiveData
import com.abolfazloskooii.nikeshop.Base.Base
import com.abolfazloskooii.nikeshop.Base.SingleNike
import com.abolfazloskooii.nikeshop.Base.threadNetWortRequest
import com.abolfazloskooii.nikeshop.Model.OrderHistory
import com.abolfazloskooii.nikeshop.Model.TokenContainer
import com.abolfazloskooii.nikeshop.Model.repositories.Manager.PaymentRepositoryManager

class OrderHistoryViewModel(paymentRepositoryManager: PaymentRepositoryManager) :
    Base.NikeViewModel() {

    val orderLiveData = MutableLiveData<List<OrderHistory>>()

    init {
        if (TokenContainer.token != null)
            paymentRepositoryManager.orderHistory().threadNetWortRequest()
                .map {
                    val list = it.reversed()
                    list
                }
                .subscribe(object : SingleNike<List<OrderHistory>>(disposable) {
                    override fun onSuccess(t: List<OrderHistory>) {
                        orderLiveData.value = t
                    }
                })
    }

}