package com.abolfazloskooii.nikeshop.View.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.abolfazloskooii.nikeshop.Base.Base
import com.abolfazloskooii.nikeshop.Base.EXTRA_KEY
import com.abolfazloskooii.nikeshop.Base.NikeToolbar
import com.abolfazloskooii.nikeshop.Base.SingleNike
import com.abolfazloskooii.nikeshop.Base.threadNetWortRequest
import com.abolfazloskooii.nikeshop.Model.AddComment
import com.abolfazloskooii.nikeshop.R
import com.abolfazloskooii.nikeshop.ViewModels.AddCommentViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddComment() : Base.NikeFragment() {
    private val viewModel: AddCommentViewModel by viewModel()
    var compositeDisposable = CompositeDisposable()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_comment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val titleComment = view.findViewById<TextView>(R.id.titleComment)
        val contentComment = view.findViewById<TextView>(R.id.contentComment)
        val SubmitCommentt = view.findViewById<Button>(R.id.SubmitCommentt)
        val addComment = view.findViewById<NikeToolbar>(R.id.addComment)

        SubmitCommentt.setOnClickListener {
            if (!contentComment.text.isNullOrEmpty() && !titleComment.text.isNullOrEmpty()) {

                viewModel.addComment(
                    AddComment(
                        arguments?.getInt(EXTRA_KEY),
                        titleComment.text.toString(),
                        contentComment.text.toString()
                    )
                )
                    .threadNetWortRequest()
                    .subscribe(object : SingleNike<AddComment>(compositeDisposable) {
                        override fun onSuccess(t: AddComment) {
                            Toast.makeText(
                                requireContext(),
                                " نظر با موفقیت ثبت گردید ! ",
                                Toast.LENGTH_SHORT
                            ).show()
                            activity?.supportFragmentManager?.beginTransaction()?.remove(this@AddComment)?.commit()
                        }
                    })

            }
        }
        addComment.onBackListener = View.OnClickListener { activity?.supportFragmentManager?.beginTransaction()?.remove(this@AddComment)?.commit() }


    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.clear()
    }
}