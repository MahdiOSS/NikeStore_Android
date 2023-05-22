package com.abolfazloskooii.nikeshop.Model.resource

import com.abolfazloskooii.nikeshop.Model.Banner
import com.abolfazloskooii.nikeshop.Servies.http.ApiServies
import com.abolfazloskooii.nikeshop.Model.resource.DataSource.BannerDataSource
import io.reactivex.rxjava3.core.Single

class ServerBannerDataSource(private val apiServies: ApiServies) : BannerDataSource {

    override fun getBanners(): Single<List<Banner>> = apiServies.getBanner()

}