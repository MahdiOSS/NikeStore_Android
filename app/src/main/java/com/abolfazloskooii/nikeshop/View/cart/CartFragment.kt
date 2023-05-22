package com.abolfazloskooii.nikeshop.View.cart

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.abolfazloskooii.nikeshop.Auth.Auth
import com.abolfazloskooii.nikeshop.Base.Base
import com.abolfazloskooii.nikeshop.Base.CompletableNike
import com.abolfazloskooii.nikeshop.Base.EXTRA_KEY
import com.abolfazloskooii.nikeshop.Base.NikeImageView
import com.abolfazloskooii.nikeshop.Base.threadNetWortRequest
import com.abolfazloskooii.nikeshop.Model.CartItem
import com.abolfazloskooii.nikeshop.Model.SORT_BY_LASTED
import com.abolfazloskooii.nikeshop.R
import com.abolfazloskooii.nikeshop.Servies.imageLoading.ImageLoadingServies
import com.abolfazloskooii.nikeshop.View.cart.shipping.ShippingActivity
import com.abolfazloskooii.nikeshop.View.adapters.CartAdapter
import com.abolfazloskooii.nikeshop.View.home.AllProductActivity
import com.abolfazloskooii.nikeshop.View.home.ProductDetailActivity
import com.abolfazloskooii.nikeshop.ViewModels.CartViewModel
import com.abolfazloskooii.nikeshop.databinding.CartFragmentBinding
import com.airbnb.lottie.LottieAnimationView
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber


class CartFragment : Base.NikeFragment(), CartAdapter.EventListener {

    private val viewModel: CartViewModel by viewModel()
    private val imageLoadingServies: ImageLoadingServies by inject()
    lateinit var adapter: CartAdapter
    val compositeDisposable = CompositeDisposable()
    lateinit var binding: CartFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CartFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cartItemsRv = binding.cartItemsRv

            viewModel.cartLiveData.observe(viewLifecycleOwner) {
                Timber.tag("ITEM_").i(it.toString())
                adapter = CartAdapter(it as MutableList<CartItem>, imageLoadingServies, this)
                cartItemsRv.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                cartItemsRv.setHasFixedSize(true)
                adapter.let { adapter ->
                    cartItemsRv.adapter = adapter
                }
            }

        viewModel.purchaseLiveData.observe(viewLifecycleOwner) {
            it.let {
                adapter.purchase = it
                adapter.notifyItemChanged(adapter.itemCount)
            }
        }

        viewModel.setProgress().observe(viewLifecycleOwner) {
            progress(it)
        }

        viewModel.emptyStateLiveData.observe(viewLifecycleOwner) {
            if (it.show) {
                val state = showEmptyState(R.layout.view_cart_empty_state)
                state.let { view ->
                    view?.findViewById<TextView>(R.id.emptyStateMessageTv)?.text = getString(it.message)
                    val emptyStateCtaBtn = view?.findViewById<Button>(R.id.emptyStateCtaBtn)!!
                    emptyStateCtaBtn.visibility = if (it.showBtn) View.VISIBLE else View.GONE
                    val animView = view.findViewById<LottieAnimationView>(R.id.animView)
                    val imageView = view.findViewById<ImageView>(R.id.imageView)
                    if (it.message == R.string.cartEmptyState) {
                        animView.visibility = View.VISIBLE
                        imageView.visibility = View.GONE
                        emptyStateCtaBtn.visibility = View.VISIBLE
                        emptyStateCtaBtn.text = "ثبت سفارش جدید"
                        emptyStateCtaBtn.setOnClickListener {
                            startActivity(
                                Intent(requireContext(), AllProductActivity::class.java).putExtra(
                                    EXTRA_KEY, SORT_BY_LASTED
                                )
                            )
                        }
                    } else if (it.message == R.string.cartEmptyStateLoginRequired) {
                        animView.visibility = View.GONE
                        imageView.visibility = View.VISIBLE
                        emptyStateCtaBtn.setOnClickListener {
                            startActivity(
                                Intent(
                                    requireContext(),
                                    Auth::class.java
                                )
                            )
                        }
                    }
                }
            } else
                view.findViewById<View>(R.id.emptyStateRootView)?.visibility = View.GONE
        }
        view.findViewById<Button>(R.id.payBtn)?.setOnClickListener {
            startActivity(
                Intent(requireContext(), ShippingActivity::class.java).putExtra(
                    EXTRA_KEY,
                    viewModel.purchaseLiveData.value
                )
            )
        }
    }

    override fun increaseItem(cartItem: CartItem) {
        Timber.i(cartItem.toString())
        viewModel.addItemCart(cartItem).threadNetWortRequest()
            .subscribe(object : CompletableNike(compositeDisposable) {
                override fun onComplete() {
                    adapter.increaseAndDecrease(cartItem)
                    adapter.notifyItemChanged(adapter.cartItem.size)
                }
            })
    }

    override fun decreaseItem(cartItem: CartItem) {
        viewModel.subtractItemCart(cartItem).threadNetWortRequest()
            .subscribe(object : CompletableNike(compositeDisposable) {
                override fun onComplete() {
                    adapter.increaseAndDecrease(cartItem)
                    adapter.notifyItemChanged(adapter.cartItem.size)
                }
            })
    }

    override fun removeFromCart(cartItem: CartItem) {
        viewModel.removeItem(cartItem).threadNetWortRequest()
            .subscribe(object : CompletableNike(compositeDisposable) {
                override fun onComplete() {
                    adapter.removeItem(cartItem)
                    adapter.notifyItemChanged(adapter.cartItem.size)
                    adapter.notifyDataSetChanged()
                }
            })
    }

    override fun imageClick(cartItem: CartItem) {
        startActivity(
            Intent(requireContext(), ProductDetailActivity()::class.java).putExtra(
                EXTRA_KEY, cartItem.product
            )
        )
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.clear()
    }

    override fun onStart() {
        super.onStart()
        viewModel.refresh()
    }
}