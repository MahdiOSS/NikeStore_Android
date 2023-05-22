package com.abolfazloskooii.nikeshop.View.cart.shipping

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.abolfazloskooii.nikeshop.Base.*
import com.abolfazloskooii.nikeshop.Model.CartPurchase
import com.abolfazloskooii.nikeshop.Model.SubmitOrderResult
import com.abolfazloskooii.nikeshop.R
import com.abolfazloskooii.nikeshop.View.adapters.CartAdapter
import com.abolfazloskooii.nikeshop.ViewModels.PaymentViewModel
import com.google.gson.JsonObject
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.koin.androidx.viewmodel.ext.android.viewModel

class ShippingActivity : Base.NikeActivity() {
    val viewModel: PaymentViewModel by viewModel()
    val compositeDisposable = CompositeDisposable()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shipping)

        val purchaseDetail = intent.getParcelableExtra<CartPurchase>(EXTRA_KEY)
            ?: throw IllegalStateException("purchase detail cannot be null")


        viewModel.setProgress().observe(this){
            progress(it)
        }

        val purchaseDetailView = findViewById<View>(R.id.purchaseDetailView)
        val firstNameEt = findViewById<TextView>(R.id.firstNameEt)
        val lastNameEt = findViewById<TextView>(R.id.lastNameEt)
        val postalCodeEt = findViewById<TextView>(R.id.postalCodeEt)
        val phoneNumberEt = findViewById<TextView>(R.id.phoneNumberEt)
        val addressEt = findViewById<TextView>(R.id.addressEt)
        val onlinePaymentBtn = findViewById<Button>(R.id.onlinePaymentBtn)
        val codBtn = findViewById<Button>(R.id.codBtn)

        val viewHolder = CartAdapter.PurchaseViewHolder(purchaseDetailView)
        viewHolder.bind(
            purchaseDetail.total_price,
            purchaseDetail.shipping_cost,
            purchaseDetail.payable_price
        )

        val listener = View.OnClickListener {
            if (!firstNameEt.text.isNullOrEmpty()
                && !lastNameEt.text.isNullOrEmpty()
                && !postalCodeEt.text.isNullOrEmpty()
                && !phoneNumberEt.text.isNullOrEmpty()
                && !addressEt.text.isNullOrEmpty()) {
                progress(true)
                viewModel.submit(JsonObject().apply {
                    addProperty("first_name", firstNameEt.text.toString())
                    addProperty("last_name", lastNameEt.text.toString())
                    addProperty("postal_code", postalCodeEt.text.toString())
                    addProperty("mobile", phoneNumberEt.text.toString())
                    addProperty("address", addressEt.text.toString())
                    addProperty(
                        "payment_method",
                        if (it.id == R.id.onlinePaymentBtn) "online" else "cash_on_delivery"
                    )
                }).threadNetWortRequest()
                    .subscribe(object : SingleNike<SubmitOrderResult>(compositeDisposable) {
                        override fun onSuccess(t: SubmitOrderResult) {

                            if (t.bank_gateway_url.isNotEmpty()) {
                                openUrlInCustomTab(this@ShippingActivity, t.bank_gateway_url)
                            } else
                                startActivity(
                                    Intent(
                                        this@ShippingActivity,
                                        CheckOutActivity::class.java
                                    ).putExtra(EXTRA_KEY,t.order_id)
                                )

                            finish()
                        }
                    })
            }else
                showSnackBar("لطفا تمامی فیلد ها را پر کنید !")
        }

        onlinePaymentBtn.setOnClickListener(listener)
        codBtn.setOnClickListener(listener)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
}