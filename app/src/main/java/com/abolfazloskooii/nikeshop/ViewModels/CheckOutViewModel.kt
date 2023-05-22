package com.abolfazloskooii.nikeshop.ViewModels

import androidx.lifecycle.MutableLiveData
import com.abolfazloskooii.nikeshop.Base.Base
import com.abolfazloskooii.nikeshop.Base.SingleNike
import com.abolfazloskooii.nikeshop.Base.threadNetWortRequest
import com.abolfazloskooii.nikeshop.Model.Checkout
import com.abolfazloskooii.nikeshop.Model.repositories.Manager.PaymentRepositoryManager

class CheckOutViewModel(orderId : Int,paymentRepositoryManager: PaymentRepositoryManager) : Base.NikeViewModel() {

    val orderIdLiveData = MutableLiveData<Checkout>()
    fun setProgress() = progressLiveData
    init {
        progressLiveData.value = true
        paymentRepositoryManager.checkout(orderId)
            .threadNetWortRequest()
            .doAfterSuccess { progressLiveData.value = false }
            .subscribe(object : SingleNike<Checkout>(disposable){
                override fun onSuccess(t: Checkout) {
                    orderIdLiveData.value = t
                }
            })
    }

}