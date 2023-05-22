package com.abolfazloskooii.nikeshop.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.abolfazloskooii.nikeshop.Base.Base
import com.abolfazloskooii.nikeshop.Base.SingleNike
import com.abolfazloskooii.nikeshop.Base.threadNetWortRequest
import com.abolfazloskooii.nikeshop.Model.Product
import com.abolfazloskooii.nikeshop.Model.repositories.Manager.ProductsRepositoryManager

class AllProductViewModel(
    var sort: Int,
    private val productsRepositoryManager: ProductsRepositoryManager
) : Base.NikeViewModel() {

    private val productsLiveData = MutableLiveData<List<Product>>()
    private var sortLiveData = MutableLiveData<Int>()

    init {
        getProducts()
    }

    fun getProducts(): LiveData<List<Product>> {
        sortLiveData.value = sort
        progressLiveData.value = true
        productsRepositoryManager.getProducts(sortLiveData.value!!)
            .threadNetWortRequest()
            .doFinally { progressLiveData.value = false }
            .subscribe(object : SingleNike<List<Product>>(disposable) {
                override fun onSuccess(t: List<Product>) {
                    productsLiveData.value = t
                }
            })
        return productsLiveData
    }

    fun setProgress() = progressLiveData
    fun getSort() = sortLiveData

    fun addToFavorite(product: Product) {
        if (product.isFavorite)
            productsRepositoryManager.deleteFav(product)
                .threadNetWortRequest()
                .subscribe { product.isFavorite = false}
        else
            productsRepositoryManager.addFav(product)
                .threadNetWortRequest()
                .subscribe { product.isFavorite = true }
    }

}