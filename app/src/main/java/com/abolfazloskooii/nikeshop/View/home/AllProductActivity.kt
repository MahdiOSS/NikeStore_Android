package com.abolfazloskooii.nikeshop.View.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abolfazloskooii.nikeshop.Base.Base
import com.abolfazloskooii.nikeshop.Base.EXTRA_KEY
import com.abolfazloskooii.nikeshop.Base.NikeToolbar
import com.abolfazloskooii.nikeshop.Model.Product
import com.abolfazloskooii.nikeshop.R
import com.abolfazloskooii.nikeshop.Servies.imageLoading.ImageLoadingServies
import com.abolfazloskooii.nikeshop.View.adapters.RvMainProductAdapter
import com.abolfazloskooii.nikeshop.View.adapters.SORT_BY_LARGE
import com.abolfazloskooii.nikeshop.View.adapters.SORT_BY_SMALL
import com.abolfazloskooii.nikeshop.ViewModels.AllProductViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class AllProductActivity : Base.NikeActivity(), RvMainProductAdapter.OnClickListener {
    private val viewModel: AllProductViewModel by viewModel {
        parametersOf(
            intent.extras?.getInt(
                EXTRA_KEY
            )
        )
    }
    private lateinit var adapter: RvMainProductAdapter
    private lateinit var gridLayoutManager: GridLayoutManager
    private val imageLoader: ImageLoadingServies by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_product)

        gridLayoutManager = GridLayoutManager(this, 2)

        val selectedSortTitleTv = findViewById<TextView>(R.id.selectedSortTitleTv)
        val productsRv = findViewById<RecyclerView>(R.id.productsRv)
        val viewTypeChangerBtn = findViewById<ImageView>(R.id.viewTypeChangerBtn)
        val toolbarView = findViewById<NikeToolbar>(R.id.toolbarView)
        val sortBtn = findViewById<View>(R.id.sortBtn)

        viewModel.getProducts().observe(this) {
            productsRv.layoutManager = gridLayoutManager
            productsRv.setHasFixedSize(true)
            adapter = RvMainProductAdapter(SORT_BY_SMALL, it as MutableList<Product>, imageLoader, this)
            productsRv.adapter = adapter
        }

        viewModel.setProgress().observe(this) {
            progress(it)
        }

        viewModel.getSort().observe(this){
            selectedSortTitleTv.text = when (it) {
                0 -> " جدید ترین محصولات"
                1 -> "پر فروش ترین محصولات"
                2 -> "لوکس ترین محصولات"
                3 -> "به صرفه ترین محصولات"
                else -> "معمولی"
            }
        }



        viewTypeChangerBtn.setOnClickListener {

            when (adapter.viewType) {
                SORT_BY_LARGE -> {
                    adapter.viewType = SORT_BY_SMALL
                    gridLayoutManager.spanCount = 2
                    viewTypeChangerBtn.setImageResource(R.drawable.ic_grid)

                    adapter.notifyDataSetChanged()
                }
                SORT_BY_SMALL -> {
                    adapter.viewType = SORT_BY_LARGE
                    gridLayoutManager.spanCount = 1
                    viewTypeChangerBtn.setImageResource(R.drawable.ic_plus_square)

                    adapter.notifyDataSetChanged()
                }
            }
        }

       toolbarView.onBackListener = View.OnClickListener { finish() }

        sortBtn.setOnClickListener {
            val dialog = MaterialAlertDialogBuilder(this).apply {
                this.setTitle(R.string.sort)
                setItems(R.array.sortArray) { p0, it ->
                    viewModel.sort = it
                    viewModel.getProducts()
                    adapter.notifyDataSetChanged()
                }
            }
            dialog.show()
        }

    }

    override fun onClick(product: Product) {
        startActivity(Intent(this, ProductDetailActivity::class.java).putExtra(EXTRA_KEY, product))
    }

    override fun onFavoriteListener(product: Product) {
        viewModel.addToFavorite(product)
    }
}