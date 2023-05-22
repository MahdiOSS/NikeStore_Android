package com.abolfazloskooii.nikeshop.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.abolfazloskooii.nikeshop.Base.Base
import com.abolfazloskooii.nikeshop.Base.CompletableNike
import com.abolfazloskooii.nikeshop.Base.SingleNike
import com.abolfazloskooii.nikeshop.Base.threadNetWortRequest
import com.abolfazloskooii.nikeshop.Model.Banner
import com.abolfazloskooii.nikeshop.Model.Product
import com.abolfazloskooii.nikeshop.Model.SORT_BY_BEST_SELLER
import com.abolfazloskooii.nikeshop.Model.SORT_BY_LASTED
import com.abolfazloskooii.nikeshop.Model.repositories.Manager.BannerRepositoryManager
import com.abolfazloskooii.nikeshop.Model.repositories.Manager.ProductsRepositoryManager


class HomeViewModel(
    private val productsRepositoryManager: ProductsRepositoryManager,
    private val bannerRepositoryManager: BannerRepositoryManager,
) : Base.NikeViewModel() {

    private var productsLiveData = MutableLiveData<List<Product>>()
    private var productsBestSellerLiveData = MutableLiveData<List<Product>>()
    private var bannersLiveData = MutableLiveData<List<Banner>>()

    init {
        progressLiveData.value = true
        refresh()
    }


    private fun requestProducts(sort: Int) {
        progressLiveData.value = true
       productsRepositoryManager.getProducts(sort)
            .threadNetWortRequest()
            .doAfterSuccess { progressLiveData.value = false }
            .subscribe(object : SingleNike<List<Product>>(disposable){
                override fun onSuccess(t: List<Product>) {
                    setDataProducts(t,sort)
                }
            })
    }

    private fun requestBanners() {
     bannerRepositoryManager.getBanner()
            .threadNetWortRequest()
            .subscribe(object : SingleNike<List<Banner>>(disposable){
                override fun onSuccess(t: List<Banner>) {
                    bannersLiveData.value = t
                }
            })
    }

    fun addToFavorite(product: Product) {
        if (product.isFavorite)
            productsRepositoryManager.deleteFav(product)
                .threadNetWortRequest()
                .subscribe(object : CompletableNike(disposable){
                    override fun onComplete() {
                        product.isFavorite = false
                    }
                })
        else
            productsRepositoryManager.addFav(product)
                .threadNetWortRequest()
                .subscribe(object : CompletableNike(disposable){
                    override fun onComplete() {
                        product.isFavorite = true
                    }
                })
    }

    fun getProducts(): LiveData<List<Product>> = productsLiveData

    fun getProductsBestSeller(): LiveData<List<Product>> = productsBestSellerLiveData

    fun setProgress(): LiveData<Boolean> = progressLiveData

    fun getBanners(): LiveData<List<Banner>> = bannersLiveData

    private fun setDataProducts(list: List<Product>, sort: Int) {
        when (sort) {
            SORT_BY_LASTED -> productsLiveData.value = list
            SORT_BY_BEST_SELLER -> productsBestSellerLiveData.value = list
        }
    }
    fun refresh (){
        progressLiveData.value = true
        requestBanners()
        requestProducts(SORT_BY_LASTED)
        requestProducts(SORT_BY_BEST_SELLER)
    }
}