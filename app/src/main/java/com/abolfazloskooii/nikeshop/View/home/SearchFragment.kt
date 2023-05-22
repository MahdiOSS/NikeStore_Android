package com.abolfazloskooii.nikeshop.View.home

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.getSystemService
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.abolfazloskooii.nikeshop.Model.Product
import com.abolfazloskooii.nikeshop.R
import com.abolfazloskooii.nikeshop.Servies.imageLoading.ImageLoadingServies
import com.abolfazloskooii.nikeshop.View.adapters.RvMainProductAdapter
import com.abolfazloskooii.nikeshop.ViewModels.SearchViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class SearchFragment : Fragment(), RvMainProductAdapter.OnClickListener {
    val viewModel: SearchViewModel by viewModel()
    private val imageLoadingServies: ImageLoadingServies by inject()
    private lateinit var recycler: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val et_search = view.findViewById<TextView>(R.id.et_search)

        recycler = view.findViewById(R.id.rv_search)
        recycler.layoutManager = GridLayoutManager(requireContext(), 2)
        recycler.setHasFixedSize(true)

//        (requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).showSoftInput(et_search,InputMethodManager.SHOW_IMPLICIT)

        viewModel.productList.observe(viewLifecycleOwner) {
            recycler.adapter = RvMainProductAdapter(
                data = it as MutableList<Product>,
                imageLoader = imageLoadingServies,
                onClickListener = this
            )
        }



        et_search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                viewModel.search(s.toString())
            }
        })


    }

    override fun onClick(product: Product) {

    }

    override fun onFavoriteListener(product: Product) {

    }


}