package com.abolfazloskooii.nikeshop.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.abolfazloskooii.nikeshop.Base.Base
import com.abolfazloskooii.nikeshop.Base.SingleNike
import com.abolfazloskooii.nikeshop.Base.threadNetWortRequest
import com.abolfazloskooii.nikeshop.Model.*
import com.abolfazloskooii.nikeshop.Model.repositories.Manager.CartRepositoryManager
import com.abolfazloskooii.nikeshop.R
import io.reactivex.rxjava3.core.Completable
import org.greenrobot.eventbus.EventBus

class CartViewModel(private val cartRepositoryManager: CartRepositoryManager) :
    Base.NikeViewModel() {

    val cartLiveData = MutableLiveData<List<CartItem>>()
    val purchaseLiveData = MutableLiveData<CartPurchase>()
    val emptyStateLiveData = MutableLiveData<EmptyState>()

    init {
        refresh()
    }

    private fun getCart() {
        if (!TokenContainer.token.isNullOrEmpty()) {
            progressLiveData.value = true
            emptyStateLiveData.value = EmptyState(false)
            cartRepositoryManager.getCart()
                .threadNetWortRequest()
                .doAfterSuccess { progressLiveData.value = false }
                .subscribe(object : SingleNike<CartResponse>(disposable) {
                    override fun onSuccess(it: CartResponse) {
                        if (it.cart_items.isNotEmpty()) {
                            cartLiveData.value = it.cart_items
                            purchaseLiveData.value =
                                CartPurchase(it.payable_price, it.shipping_cost, it.total_price)
                        }else
                            emptyStateLiveData.value = EmptyState(true, R.string.cartEmptyState,true)
                    }
                })
        }else
            emptyStateLiveData.value = EmptyState(true, R.string.cartEmptyStateLoginRequired,true)
    }

    fun removeItem(cartItem: CartItem): Completable =
        cartRepositoryManager.removeCart(cartItem.cart_item_id)
            .doAfterSuccess {
                applyOffPrice()
                if (cartLiveData.value!!.isEmpty())
                    emptyStateLiveData.postValue(EmptyState(true,R.string.cartEmptyState))
                eventChangeCount(cartItem,2)
            }
            .ignoreElement()


     fun addItemCart(cartItem: CartItem): Completable =
        cartRepositoryManager.changeCartCount(cartItem.cart_item_id, ++cartItem.count)
            .doAfterSuccess { applyOffPrice()
                eventChangeCount(cartItem,0)}
            .ignoreElement()

     fun subtractItemCart(cartItem: CartItem): Completable =
        cartRepositoryManager.changeCartCount(cartItem.cart_item_id, --cartItem.count)
            .doAfterSuccess { applyOffPrice()
                eventChangeCount(cartItem,1) }
            .ignoreElement()

    private fun applyOffPrice() {
        var totalPrice = 0
        var payablePrice = 0
        cartLiveData.value?.let { cartItem ->
            purchaseLiveData.value.let {
                cartItem.forEach { cartItem ->
                    totalPrice += cartItem.product.price!! * cartItem.count
                    payablePrice +=
                        (cartItem.product.price - cartItem.product.discount!!) * cartItem.count
                }
                it?.total_price = totalPrice
                it?.payable_price = payablePrice
                purchaseLiveData.postValue(it)
            }
        }
    }

    fun refresh() {
        getCart()
    }

    private fun eventChangeCount(cartItemCount: CartItem, sort : Int){
       val data = EventBus.getDefault().getStickyEvent(CartItemCount::class.java)
        data.let {
            when(sort){
                0 -> {
                    it.count += 1
                    EventBus.getDefault().postSticky(it)
                }
                1 -> {
                    it.count -= 1
                    EventBus.getDefault().postSticky(it)
                }
                2 -> {
                    it.count -= cartItemCount.count
                    EventBus.getDefault().postSticky(it)
                }
            }
        }
    }

    fun setProgress(): LiveData<Boolean> = progressLiveData

}