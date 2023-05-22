package com.abolfazloskooii.nikeshop.Servies.http

import com.abolfazloskooii.nikeshop.Model.*
import com.google.gson.JsonObject
import io.reactivex.rxjava3.core.Single
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiServies {

    @GET("product/list")
    fun getProducts(@Query("sort") sort : Int): Single<List<Product>>

    @GET("banner/slider")
    fun getBanner() : Single<List<Banner>>

    @GET("comment/list")
    fun getComment(@Query("product_id") query: Int) : Single<List<Comment>>

    @POST ("comment/add")
    fun addComment(@Body jsonObject: JsonObject) : Single<AddComment>

    @POST("cart/add")
    fun addToCart (@Body jsonObject: JsonObject) : Single<AddToCartResponse>

    @POST("cart/remove")
    fun removeItemFromCart(@Body jsonObject: JsonObject): Single<MessageResponse>

    @GET("cart/list")
    fun getCart(): Single<CartResponse>

    @POST("cart/changeCount")
    fun changeCount(@Body jsonObject: JsonObject): Single<AddToCartResponse>

    @GET("cart/count")
    fun getCartItemsCount(): Single<CartItemCount>

    @POST ("auth/token")
    fun login (@Body jsonObject: JsonObject) : Single<TokenResponse>

    @POST ("user/register")
    fun signUp (@Body jsonObject: JsonObject) : Single<MessageResponse>

    @POST ("auth/token")
    fun refreshToken(@Body jsonObject: JsonObject) : Call<TokenResponse>

    @POST ("order/submit")
    fun submitORDER(@Body jsonObject: JsonObject) : Single<SubmitOrderResult>

    @GET ("order/checkout")
    fun checkout(@Query("order_id") orderID: Int) : Single<Checkout>

    @GET ("order/list")
    fun orderHistory () : Single<List<OrderHistory>>

    @GET ("product/search")
    fun search(@Query("q") q : String) : Single<List<Product>>

}

fun createRetrofit(): ApiServies {

    val okHttpClient = OkHttpClient.Builder().apply {
        addInterceptor {
            val oldRequest = it.request()
            val newRequest = oldRequest.newBuilder()

            if (TokenContainer.token != null) {
                newRequest.addHeader("Authorization", "Bearer ${TokenContainer.token}")
                newRequest.addHeader("Accept", "application/json")
            }

            newRequest.method(oldRequest.method, oldRequest.body)

            return@addInterceptor it.proceed(newRequest.build())
        }
    }.addInterceptor(HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    })
        .build()

    return Retrofit.Builder()
        .baseUrl("http://expertdevelopers.ir/api/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .client(okHttpClient)
        .build()
        .create(ApiServies::class.java)
}