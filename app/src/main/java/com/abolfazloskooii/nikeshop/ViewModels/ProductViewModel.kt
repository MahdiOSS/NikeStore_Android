package com.abolfazloskooii.nikeshop.ViewModels

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.abolfazloskooii.nikeshop.Base.Base
import com.abolfazloskooii.nikeshop.Base.EXTRA_KEY
import com.abolfazloskooii.nikeshop.Base.threadNetWortRequest
import com.abolfazloskooii.nikeshop.Model.Comment
import com.abolfazloskooii.nikeshop.Model.Product
import com.abolfazloskooii.nikeshop.Model.repositories.Manager.CartRepositoryManager
import com.abolfazloskooii.nikeshop.Model.repositories.Manager.CommentRepositoryManager
import com.abolfazloskooii.nikeshop.Model.repositories.Manager.ProductsRepositoryManager
import io.reactivex.rxjava3.core.Completable
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

class ProductViewModel(
    private val bundle: Bundle,
    private val repositoryManager: CommentRepositoryManager,
    private val cartRepositoryManager: CartRepositoryManager,
    private val productsRepositoryManager: ProductsRepositoryManager
) : Base.NikeViewModel() {


    private var productLiveData: MutableLiveData<Product> = MutableLiveData<Product>()
    private var commentLiveData: MutableLiveData<List<Comment>> = MutableLiveData()

    init {
        reqStart()
    }

    private fun reqStart() {
        viewModelScope.launch {
            progressLiveData.value = true
            delay(500)

            val value = bundle.getParcelable<Product>(EXTRA_KEY)
            productLiveData.value = value!!

            repositoryManager.getComment(value.id!!)
                .threadNetWortRequest()
                .doFinally { progressLiveData.value = false }
                .subscribe({
                    Timber.tag("SCROOl").i(it.toString())
                    commentLiveData.value = it
                }, {
                    Timber.tag("SCROOl").i(it.toString())
                })

        }
    }



    fun addToFavorite() : Boolean {

        val product = productLiveData.value!!
       return if (product.isFavorite) {
           productsRepositoryManager.deleteFav(product)
               .threadNetWortRequest()
               .subscribe { product.isFavorite = false }
           false
       }
        else {
           productsRepositoryManager.addFav(product)
               .threadNetWortRequest()
               .subscribe { product.isFavorite = true }
           true
       }

    }

    fun getComments(): LiveData<List<Comment>> = commentLiveData

    fun getProduct(): LiveData<Product> = productLiveData

    fun setProgress(): LiveData<Boolean> = progressLiveData

    fun addToCart(): Completable =
        cartRepositoryManager.addToCart(productLiveData.value?.id!!)
            .ignoreElement()


}