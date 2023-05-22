package com.abolfazloskooii.nikeshop.Model.repositories.Manager

import com.abolfazloskooii.nikeshop.Model.Banner
import io.reactivex.rxjava3.core.Single

interface BannerRepositoryManager {
    fun getBanner () : Single<List<Banner>>
}