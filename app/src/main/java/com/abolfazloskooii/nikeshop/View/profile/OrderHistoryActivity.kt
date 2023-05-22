package com.abolfazloskooii.nikeshop.View.profile

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abolfazloskooii.nikeshop.Base.Base
import com.abolfazloskooii.nikeshop.Base.NikeToolbar
import com.abolfazloskooii.nikeshop.Model.OrderHistory
import com.abolfazloskooii.nikeshop.R
import com.abolfazloskooii.nikeshop.View.adapters.OrderHistoryAdapter
import com.abolfazloskooii.nikeshop.ViewModels.OrderHistoryViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class OrderHistoryActivity : Base.NikeActivity() {
    val viewModel: OrderHistoryViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_history)

        val Rv_order_history = findViewById<RecyclerView>(R.id.Rv_order_history)

        Rv_order_history.layoutManager = LinearLayoutManager(this)

        viewModel.orderLiveData.observe(this) {
            Rv_order_history.adapter = OrderHistoryAdapter(it as ArrayList<OrderHistory>,this)
        }

        findViewById<NikeToolbar>(R.id.toolbarOrderHistory).onBackListener = View.OnClickListener { finish() }

    }
}