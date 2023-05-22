package com.abolfazloskooii.nikeshop.Model.repositories

import com.abolfazloskooii.nikeshop.Model.Banner
import com.abolfazloskooii.nikeshop.Model.repositories.Manager.BannerRepositoryManager
import com.abolfazloskooii.nikeshop.Model.resource.DataSource.BannerDataSource
import io.reactivex.rxjava3.core.Single

class BannerRepository(private val serverBannerDataSource: BannerDataSource) :
    BannerRepositoryManager {

    override fun getBanner(): Single<List<Banner>> = serverBannerDataSource.getBanners()

}