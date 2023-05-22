package com.abolfazloskooii.nikeshop.Base

import com.abolfazloskooii.nikeshop.Servies.error.ConvertException
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import org.greenrobot.eventbus.EventBus

abstract class SingleNike<T : Any>(private val compositeDisposable: CompositeDisposable) : SingleObserver<T> {
    override fun onError(e: Throwable) {
        EventBus.getDefault().post(ConvertException.errorNikeE.convert(e))
    }

    override fun onSubscribe(d: Disposable) {
        compositeDisposable.add(compositeDisposable)
    }
}