package com.abolfazloskooii.nikeshop.Model.resource

import androidx.lifecycle.LiveData
import com.abolfazloskooii.nikeshop.Model.Product
import com.abolfazloskooii.nikeshop.Servies.http.ApiServies
import com.abolfazloskooii.nikeshop.Model.resource.DataSource.ProductsDataSource
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

class ServerProductsDataSource(private val apiServies: ApiServies) : ProductsDataSource {

    override fun getProducts(sort : Int): Single<List<Product>> = apiServies.getProducts(sort)

    override fun getProductsFavorite(): Single<List<Product>> {
        TODO("Not yet implemented")
    }

    override fun deleteFav(product: Product): Completable {
        TODO("Not yet implemented")
    }

    override fun addFav(product: Product): Completable {
        TODO("Not yet implemented")
    }

    override fun search(q: String): Single<List<Product>> = apiServies.search(q)

}