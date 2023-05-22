package com.abolfazloskooii.nikeshop.ViewModels

import androidx.lifecycle.MutableLiveData
import com.abolfazloskooii.nikeshop.Base.Base
import com.abolfazloskooii.nikeshop.Base.CompletableNike
import com.abolfazloskooii.nikeshop.Base.SingleNike
import com.abolfazloskooii.nikeshop.Base.threadNetWortRequest
import com.abolfazloskooii.nikeshop.Model.Product
import com.abolfazloskooii.nikeshop.Model.repositories.Manager.ProductsRepositoryManager
import com.abolfazloskooii.nikeshop.Servies.imageLoading.ImageLoadingServies

class FavoriteViewModel(
    private val productsRepositoryManager: ProductsRepositoryManager,
    val imageLoadingServies: ImageLoadingServies
) : Base.NikeViewModel() {

    val productsLiveData = MutableLiveData<List<Product>>()

    init {
        getProduct()
    }

    private fun getProduct() {
        productsRepositoryManager.getProductsFavorite().threadNetWortRequest()
            .subscribe(object : SingleNike<List<Product>>(disposable) {
                override fun onSuccess(t: List<Product>) {
                    productsLiveData.value = t
                }
            })
    }

    fun remove(product: Product) {
        return productsRepositoryManager.deleteFav(product)
            .threadNetWortRequest()
            .subscribe(object : CompletableNike(disposable) {
                override fun onComplete() {
                    if (productsLiveData.value!!.isEmpty())
                        getProduct()
                }
            })
    }

}