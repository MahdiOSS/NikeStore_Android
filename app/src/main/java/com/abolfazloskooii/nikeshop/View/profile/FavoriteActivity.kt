package com.abolfazloskooii.nikeshop.View.profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abolfazloskooii.nikeshop.Base.Base
import com.abolfazloskooii.nikeshop.Base.EXTRA_KEY
import com.abolfazloskooii.nikeshop.Base.NikeToolbar
import com.abolfazloskooii.nikeshop.Model.Product
import com.abolfazloskooii.nikeshop.R
import com.abolfazloskooii.nikeshop.View.adapters.FavoriteAdapter
import com.abolfazloskooii.nikeshop.View.home.ProductDetailActivity
import com.abolfazloskooii.nikeshop.ViewModels.FavoriteViewModel
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteActivity : Base.NikeActivity(), FavoriteAdapter.EventListener {

    val viewModel: FavoriteViewModel by viewModel()

    //    val imageLoadingServies : ImageLoadingServies by inject()
    lateinit var adapter: FavoriteAdapter
    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite_products)

        recyclerView = findViewById(R.id.favoriteProductsRv)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        viewModel.productsLiveData.observe(this) {
            if (it.isNotEmpty()) {
                adapter = FavoriteAdapter(it as ArrayList<Product>, get(), this)
                recyclerView.adapter = adapter
            } else {
                showEmptyState(R.layout.view_default_empty_state)
                findViewById<TextView>(R.id.emptyStateMessageTv).text = getString(R.string.favorites_empty_state_message)
            }
        }

        findViewById<ImageView>(R.id.helpBtn).setOnClickListener{
            showSnackBar(getString(R.string.favorites_help_message))
        }

       findViewById<NikeToolbar>(R.id.toolbarFav).onBackListener = View.OnClickListener {
           finish()
       }

    }

    override fun onClick(product: Product) {
        startActivity(Intent(this, ProductDetailActivity::class.java).putExtra(EXTRA_KEY, product))
    }

    override fun onLongClick(product: Product, position: Int) {
        viewModel.remove(product)
        adapter.remove(product, position)
    }
}