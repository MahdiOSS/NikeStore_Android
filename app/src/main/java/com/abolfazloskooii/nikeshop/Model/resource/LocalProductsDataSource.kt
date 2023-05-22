package com.abolfazloskooii.nikeshop.Model.resource

import androidx.room.*
import com.abolfazloskooii.nikeshop.Model.Product
import com.abolfazloskooii.nikeshop.Model.resource.DataSource.ProductsDataSource
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single


@Dao
interface LocalProductsDataSource : ProductsDataSource {

    override fun getProducts(sort: Int): Single<List<Product>> {
        TODO("Not yet implemented")
    }

    @Query("SELECT * FROM products")
    override fun getProductsFavorite(): Single<List<Product>>

    @Delete
    override fun deleteFav(product: Product): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override fun addFav(product: Product): Completable

    override fun search(q: String): Single<List<Product>> {
        TODO("Not yet implemented")
    }

}