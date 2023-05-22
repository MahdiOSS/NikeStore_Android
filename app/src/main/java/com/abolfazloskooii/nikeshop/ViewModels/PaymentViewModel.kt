package com.abolfazloskooii.nikeshop.ViewModels

import com.abolfazloskooii.nikeshop.Base.Base
import com.abolfazloskooii.nikeshop.Model.SubmitOrderResult
import com.abolfazloskooii.nikeshop.Model.repositories.Manager.PaymentRepositoryManager
import com.google.gson.JsonObject
import io.reactivex.rxjava3.core.Single

class PaymentViewModel(private val paymentRepositoryManager: PaymentRepositoryManager) : Base.NikeViewModel() {

    fun submit(jsonObject: JsonObject) : Single<SubmitOrderResult> =
        paymentRepositoryManager.submit(jsonObject).doAfterSuccess { progressLiveData.postValue(false) }

    fun setProgress() = progressLiveData

}