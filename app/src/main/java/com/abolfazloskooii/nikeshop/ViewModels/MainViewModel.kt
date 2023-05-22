package com.abolfazloskooii.nikeshop.ViewModels

import android.media.session.MediaSession.Token
import com.abolfazloskooii.nikeshop.Base.Base
import com.abolfazloskooii.nikeshop.Base.SingleNike
import com.abolfazloskooii.nikeshop.Model.CartItemCount
import com.abolfazloskooii.nikeshop.Model.TokenContainer
import com.abolfazloskooii.nikeshop.Model.repositories.Manager.CartRepositoryManager
import io.reactivex.rxjava3.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus

class MainViewModel(private val cartRepositoryManager: CartRepositoryManager) : Base.NikeViewModel() {

    init {
       refresh()
    }

    fun refresh(){
        if(!TokenContainer.token.isNullOrEmpty()){
            cartRepositoryManager.getCartCount()
                .subscribeOn(Schedulers.io())
                .subscribe(object : SingleNike<CartItemCount>(disposable){
                    override fun onSuccess(t: CartItemCount) {
                        EventBus.getDefault().postSticky(t)
                    }
                })
        }
    }

}