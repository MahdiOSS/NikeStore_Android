package com.abolfazloskooii.nikeshop.View.home

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abolfazloskooii.nikeshop.Base.Base
import com.abolfazloskooii.nikeshop.Base.CompletableNike
import com.abolfazloskooii.nikeshop.Base.EXTRA_KEY
import com.abolfazloskooii.nikeshop.Base.formatPrice
import com.abolfazloskooii.nikeshop.Base.scroll.ObservableScrollViewCallbacks
import com.abolfazloskooii.nikeshop.Base.scroll.ScrollState
import com.abolfazloskooii.nikeshop.Model.EXTRA_KEY_COMMENT
import com.abolfazloskooii.nikeshop.Model.Product
import com.abolfazloskooii.nikeshop.R
import com.abolfazloskooii.nikeshop.Servies.imageLoading.ImageLoadingServies
import com.abolfazloskooii.nikeshop.View.adapters.RvCommentAdapter
import com.abolfazloskooii.nikeshop.ViewModels.HomeViewModel
import com.abolfazloskooii.nikeshop.ViewModels.ProductViewModel
import com.abolfazloskooii.nikeshop.databinding.ActivityProductDetailBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import timber.log.Timber

class ProductDetailActivity : Base.NikeActivity() {
    private val imageLoader: ImageLoadingServies by inject()
    val compositeDisposable = CompositeDisposable()
    lateinit var binding: ActivityProductDetailBinding
    private val viewModel by viewModel<ProductViewModel> { parametersOf(intent.extras) }
    private val homeViewModel: HomeViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val backBtn = findViewById<ImageView>(R.id.backBtn)
        val commentsRv = findViewById<RecyclerView>(R.id.commentsRv)

        backBtn.setOnClickListener { onBackPressed() }

        val data = intent.extras?.getParcelable<Product>(EXTRA_KEY)

        commentsRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        commentsRv.setHasFixedSize(true)


        viewModel.setProgress().observe(this) {
            progress(it)
        }

        viewModel.getProduct().observe(this) {
            imageLoader.loadImage(it.image.toString(), binding.productIv)
            binding.currentPriceTv.text = formatPrice(it.price!!)
            binding.previousPriceTv.text = it.previousPrice?.let { it1 -> formatPrice(it1) }
            binding.previousPriceTv.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            binding.toolbarTitleTv.text = it.title
            binding.titleTv.text = it.title
        }

        binding.productIv.post {
            val picture = binding.productIv
            binding.observableScrollView.addScrollViewCallbacks(object : ObservableScrollViewCallbacks {
                override fun onScrollChanged(
                    scrollY: Int,
                    firstScroll: Boolean,
                    dragging: Boolean
                ) {
                    binding.toolbarView.alpha = scrollY.toFloat() / binding.productIv.height.toFloat()
                    binding.titleTv.alpha = (720 - scrollY.toFloat()) / 100
                    Timber.tag("SCROOl").i(scrollY.toFloat().toString())
                    picture.translationY = scrollY.toFloat() / 2
                }

                override fun onDownMotionEvent() {}
                override fun onUpOrCancelMotionEvent(scrollState: ScrollState?) {}
            })
        }


        viewModel.getComments().observe(this) {
            binding.addToCart.visibility = View.VISIBLE
            Timber.tag("SCROOl").i(it.toString())
            commentsRv.adapter = RvCommentAdapter(it, false)
            binding.viewAllCommentsBtn.visibility = View.VISIBLE
        }

        binding.viewAllCommentsBtn.setOnClickListener {
            data?.id?.let { it1 -> startActivity(it1) }
        }

        binding.addToCart.setOnClickListener {
            viewModel.addToCart()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : CompletableNike(compositeDisposable) {
                    override fun onComplete() {
                        showSnackBar(getString(R.string.successAddToCart))
                    }

                })
        }


        binding.favoriteBtn.setOnClickListener {
           if (viewModel.addToFavorite())
               binding.favoriteBtn.setImageResource(R.drawable.ic_baseline_favorite_24)
               else
               binding.favoriteBtn.setImageResource(R.drawable.ic_favorites)
        }

        binding.insertCommentBtn.setOnClickListener {
            data?.id?.let { it1 -> startActivity(it1) }
        }

    }

    fun startActivity(id : Int){
        startActivity(
            Intent(
                this,
                CommentAllActivity::class.java
            ).apply { putExtra(EXTRA_KEY_COMMENT,id) })
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
}