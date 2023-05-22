package com.abolfazloskooii.nikeshop.ViewModels

import androidx.lifecycle.MutableLiveData
import com.abolfazloskooii.nikeshop.Base.Base
import com.abolfazloskooii.nikeshop.Base.SingleNike
import com.abolfazloskooii.nikeshop.Base.threadNetWortRequest
import com.abolfazloskooii.nikeshop.Model.Product
import com.abolfazloskooii.nikeshop.Model.repositories.Manager.ProductsRepositoryManager
import io.reactivex.rxjava3.core.Single

class SearchViewModel(private val productsRepositoryManager: ProductsRepositoryManager) : Base.NikeViewModel() {

    val productList = MutableLiveData<List<Product>>()

    fun search(q: String) {

        productsRepositoryManager.search(q)
            .threadNetWortRequest()
            .subscribe(object : SingleNike<List<Product>>(disposable){
                override fun onSuccess(t: List<Product>) {
                    productList.value = t
                }
            })

    }



}