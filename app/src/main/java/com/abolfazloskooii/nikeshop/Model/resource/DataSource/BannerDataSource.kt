package com.abolfazloskooii.nikeshop.Model.resource.DataSource

import com.abolfazloskooii.nikeshop.Model.Banner
import io.reactivex.rxjava3.core.Single

interface BannerDataSource {
    fun getBanners() : Single<List<Banner>>
}