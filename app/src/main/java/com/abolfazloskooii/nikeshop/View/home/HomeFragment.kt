package com.abolfazloskooii.nikeshop.View.home

import android.annotation.SuppressLint
import android.app.usage.UsageEvents.Event
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.abolfazloskooii.nikeshop.Base.Base
import com.abolfazloskooii.nikeshop.Base.EXTRA_KEY
import com.abolfazloskooii.nikeshop.Base.TAG
import com.abolfazloskooii.nikeshop.Base.convertDpToPixel
import com.abolfazloskooii.nikeshop.Model.Banner
import com.abolfazloskooii.nikeshop.Model.Product
import com.abolfazloskooii.nikeshop.Model.SORT_BY_BEST_SELLER
import com.abolfazloskooii.nikeshop.Model.SORT_BY_LASTED
import com.abolfazloskooii.nikeshop.R
import com.abolfazloskooii.nikeshop.Servies.imageLoading.ImageLoadingServies
import com.abolfazloskooii.nikeshop.View.adapters.RvMainProductAdapter
import com.abolfazloskooii.nikeshop.View.adapters.SORT_BY_NORMAL
import com.abolfazloskooii.nikeshop.View.adapters.SliderViewPagerAdapter2
import com.abolfazloskooii.nikeshop.ViewModels.HomeViewModel
import com.abolfazloskooii.nikeshop.databinding.FragmentMainBinding
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.internal.notifyAll
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import kotlin.math.abs


class HomeFragment : Base.NikeFragment(), RvMainProductAdapter.OnClickListener {

    private val homeViewModel: HomeViewModel by viewModel()
    private val imageLoader: ImageLoadingServies by inject()

    lateinit var binding: FragmentMainBinding
    private lateinit var viewPager2: ViewPager2
    private lateinit var indicator: DotsIndicator
    private lateinit var rvMainProductAdapter: RvMainProductAdapter
    private lateinit var rvBestSellerAdapter: RvMainProductAdapter
    private lateinit var recyclerViewX: RecyclerView
    private lateinit var recyclerviewBestseller: RecyclerView
    private lateinit var sliderViewPagerAdapter2: SliderViewPagerAdapter2
    private val handler = Handler()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(layoutInflater)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        viewPager2 = view.findViewById(R.id.viewPager)
        indicator = view.findViewById(R.id.sliderIndicator)
        recyclerViewX = view.findViewById(R.id.rv_news_main)
        recyclerviewBestseller = view.findViewById(R.id.rv_best_seller_main)
        val developer = view.findViewById<LinearLayout>(R.id.developer)
        val btn_seeAll_lasted = view.findViewById<Button>(R.id.btn_seeAll_lasted)
        val btn_seeAll_bestSeller = view.findViewById<Button>(R.id.btn_seeAll_bestSeller)

        binding.searchHome.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.linRoot, SearchFragment())
                    .setTransition(TRANSIT_FRAGMENT_OPEN)
                    .addToBackStack(null)
                    .commit()
            }
            true
        }



        recycler1()

        homeViewModel.setProgress().observe(viewLifecycleOwner) { progress(it) }

        homeViewModel.getProducts().observe(viewLifecycleOwner) {
            rvMainProductAdapter = RvMainProductAdapter(
                SORT_BY_NORMAL,
                it.reversed() as MutableList<Product>, imageLoader, this
            )
            recyclerViewX.adapter = rvMainProductAdapter
            rvMainProductAdapter.notifyDataSetChanged()
            developer.visibility = View.VISIBLE
        }

        btn_seeAll_lasted.setOnClickListener {
            startActivity(Intent(this.context, AllProductActivity::class.java).apply {
                putExtra(
                    EXTRA_KEY,
                    SORT_BY_LASTED
                )
            })
        }

        btn_seeAll_bestSeller.setOnClickListener {
            startActivity(Intent(this.context, AllProductActivity::class.java).apply {
                putExtra(
                    EXTRA_KEY,
                    SORT_BY_BEST_SELLER
                )
            })
        }


        homeViewModel.getBanners().observe(viewLifecycleOwner) {

            sliderViewPagerAdapter2 =
                SliderViewPagerAdapter2(
                    viewPager2,
                    it as ArrayList<Banner>,
                    imageLoader,
                    getDisplayMetric().toInt()
                )

            val layoutParams = viewPager2.layoutParams
            layoutParams?.height = getDisplayMetric().toInt()
            viewPager2.layoutParams = layoutParams
            indicator.setFadingEdgeLength(3)
            viewPager2.adapter = sliderViewPagerAdapter2
            viewPager2.offscreenPageLimit = 3
            viewPager2.clipToPadding = false
            viewPager2.clipChildren = false
            viewPager2.let { it1 -> indicator.attachTo(it1) }
            viewPager2.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            viewPager2.setOnTouchListener { _, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        handler.removeCallbacks(runnable)
                        true
                    }

                    MotionEvent.ACTION_UP -> {
                        handler.postDelayed(runnable, 5000)
                        true
                    }

                    else -> false
                }
            }
            val compositePageTransformer = CompositePageTransformer()
            compositePageTransformer.addTransformer(MarginPageTransformer(40))
            compositePageTransformer.addTransformer { page, position ->
                val r = 1 - abs(position)
                page.scaleY = 0.85f + r * 0.15f
            }
            viewPager2.setPageTransformer(compositePageTransformer)

            viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)

                    handler.removeCallbacks(runnable)
                    handler.postDelayed(runnable, 5000)

                    viewPager2.isUserInputEnabled =
                        position == sliderViewPagerAdapter2.itemCount - 1

                }
            })

        }

        homeViewModel.getProductsBestSeller().observe(viewLifecycleOwner) {
            Timber.tag(TAG).i(it.toString())
            rvBestSellerAdapter = RvMainProductAdapter(
                SORT_BY_BEST_SELLER,
                it.reversed() as MutableList<Product>, imageLoader, this
            )
            recyclerviewBestseller.adapter = rvBestSellerAdapter
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            indicator.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                Timber.i("scroll x -> $scrollX && scroll y -> $scrollY && oldScrollX -> $oldScrollX && oldScroll y -> $oldScrollY")
            }
            indicator.setFadingEdgeLength(3)
        }
    }


    override fun onClick(product: Product) {
        startActivity(Intent(requireContext(), ProductDetailActivity::class.java).apply {
            putExtra(
                EXTRA_KEY,
                product
            )
        })
    }

    override fun onFavoriteListener(product: Product) {
        homeViewModel.addToFavorite(product)
    }

    private fun recycler1() {
        val layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        recyclerViewX.layoutManager = layoutManager
        recyclerviewBestseller.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        recyclerViewX.setHasFixedSize(true)
        recyclerviewBestseller.setHasFixedSize(true)
    }

    private val runnable = object : Runnable {
        override fun run() {

            try {
                if (viewPager2.currentItem == sliderViewPagerAdapter2.itemCount) {
                    viewPager2.setCurrentItem(0, false)
                } else
                    viewPager2.currentItem =
                        (viewPager2.currentItem + 1) % sliderViewPagerAdapter2.itemCount

                handler.postDelayed(this, 5000)
            }catch (t:Throwable){
                Timber.e(t)
            }

        }
    }

    private fun getDisplayMetric(): Float {
        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
        val width = displayMetrics.widthPixels - convertDpToPixel(32f, this.requireContext())

        //Use Opration for height size !
        return width * 178 / 328
    }

    override fun onResume() {
        super.onResume()
        handler.removeCallbacks(runnable)
        viewPager2.postDelayed(runnable, 5000)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable)
    }

}