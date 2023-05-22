package com.abolfazloskooii.nikeshop.Base

import com.abolfazloskooii.nikeshop.Servies.error.ConvertException
import io.reactivex.rxjava3.core.CompletableObserver
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import org.greenrobot.eventbus.EventBus
import timber.log.Timber

abstract class CompletableNike(private val compositeDisposable: CompositeDisposable) : CompletableObserver {
    override fun onError(e: Throwable) {
        EventBus.getDefault().post(ConvertException.errorNikeE.convert(e))
        Timber.tag("MAIN_ERROR").e(e)
    }
    override fun onSubscribe(d: Disposable) {
        compositeDisposable.add(d)
    }
}