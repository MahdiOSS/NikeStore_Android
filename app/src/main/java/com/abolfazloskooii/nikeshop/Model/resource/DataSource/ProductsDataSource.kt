package com.abolfazloskooii.nikeshop.Model.resource.DataSource

import com.abolfazloskooii.nikeshop.Model.Product
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

interface ProductsDataSource {
    fun getProducts(sort :Int) : Single<List<Product>>

    fun getProductsFavorite() : Single<List<Product>>

    fun deleteFav(product: Product) : Completable

    fun addFav(product: Product) : Completable

    fun search(q : String) : Single<List<Product>>
}