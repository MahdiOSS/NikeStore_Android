package com.abolfazloskooii.nikeshop.Model.repositories

import com.abolfazloskooii.nikeshop.Model.Product
import com.abolfazloskooii.nikeshop.Model.repositories.Manager.ProductsRepositoryManager
import com.abolfazloskooii.nikeshop.Model.resource.DataSource.ProductsDataSource
import com.abolfazloskooii.nikeshop.Model.resource.ServerProductsDataSource
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

class ProductsRepository(
    private val serverProductsDataSource: ServerProductsDataSource,
    private val localProductsRepositoryManager: ProductsDataSource
) : ProductsRepositoryManager {

    override fun getProducts(sort: Int): Single<List<Product>> =
        localProductsRepositoryManager.getProductsFavorite().flatMap { list ->
            serverProductsDataSource.getProducts(sort).doAfterSuccess { productList ->
                val ids = list.map { it.id }
                productList.forEach { i ->
                    if (ids.contains(i.id))
                        i.isFavorite = true
                }
            }
        }

    override fun getProductsFavorite(): Single<List<Product>> =
        localProductsRepositoryManager.getProductsFavorite()

    override fun deleteFav(product: Product): Completable =
        localProductsRepositoryManager.deleteFav(product)


    override fun addFav(product: Product): Completable =
        localProductsRepositoryManager.addFav(product)

    override fun search(q: String): Single<List<Product>> = serverProductsDataSource.search(q)

}