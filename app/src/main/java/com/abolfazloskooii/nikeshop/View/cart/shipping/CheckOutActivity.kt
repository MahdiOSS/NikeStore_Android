package com.abolfazloskooii.nikeshop.View.cart.shipping

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.abolfazloskooii.nikeshop.Base.Base
import com.abolfazloskooii.nikeshop.Base.EXTRA_KEY
import com.abolfazloskooii.nikeshop.Base.NikeImageView
import com.abolfazloskooii.nikeshop.Base.formatPrice
import com.abolfazloskooii.nikeshop.R
import com.abolfazloskooii.nikeshop.View.MainActivity
import com.abolfazloskooii.nikeshop.View.profile.OrderHistoryActivity
import com.abolfazloskooii.nikeshop.ViewModels.CheckOutViewModel
import com.abolfazloskooii.nikeshop.databinding.ActivityCheckOutBinding
import com.airbnb.lottie.LottieAnimationView
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import kotlin.random.Random

class CheckOutActivity : Base.NikeActivity() {
    lateinit var binding: ActivityCheckOutBinding

    val viewModel : CheckOutViewModel by viewModel {
        val uri = intent.data
        if (uri != null)
            parametersOf(uri.getQueryParameter("order_id")!!.toInt())
        else
            parametersOf(intent.getIntExtra(EXTRA_KEY,0))
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckOutBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_check_out)

        val orderPriceTv = findViewById<TextView>(R.id.orderPriceTv)
        val orderStatusTv = findViewById<TextView>(R.id.orderStatusTv)
        val purchaseStatusTv = findViewById<TextView>(R.id.purchaseStatusTv)
        val orderPrice2Tv = findViewById<TextView>(R.id.orderPrice2Tv)
        val returnHomeBtn = findViewById<Button>(R.id.returnHomeBtn)
        val orderHistoryBtn = findViewById<Button>(R.id.orderHistoryBtn)
        val animOrder = findViewById<LottieAnimationView>(R.id.animOrder)

        viewModel.setProgress().observe(this){
            progress(it)
        }

        viewModel.orderIdLiveData.observe(this){
            orderPriceTv.text = formatPrice(it.payable_price)
            orderStatusTv.text = it.payment_status
            purchaseStatusTv.text = when(it.purchase_success){
                true -> { "پرداخت با موفقیت انجام شد !" }
                false -> {
                    if (intent.hasExtra(EXTRA_KEY))
                        " سفارش تحویل در محل با موفقیت انجام شد !"
                    else
                        "خطا در پرداخت !"
                }
            }

            if (it.purchase_success) {
                animOrder.setAnimation(R.raw.animation)
                animOrder.playAnimation()
            }
            else if (!it.purchase_success && intent.hasExtra(EXTRA_KEY)) {
                animOrder.setAnimation(R.raw.delivery)
                animOrder.playAnimation()
            }else
            {
                animOrder.setAnimation(R.raw.cancel)
                animOrder.playAnimation()
            }
            val random = Random(1000000).nextInt()
            orderPrice2Tv.text = "DC-$random"
        }


        returnHomeBtn.setOnClickListener { startActivity(Intent(this,MainActivity::class.java)) }
        orderHistoryBtn.setOnClickListener { startActivity(Intent(this,OrderHistoryActivity::class.java)) }


    }
}