package com.abolfazloskooii.nikeshop.View.home

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_MATCH_ACTIVITY_CLOSE
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abolfazloskooii.nikeshop.Base.Base
import com.abolfazloskooii.nikeshop.Base.EXTRA_KEY
import com.abolfazloskooii.nikeshop.Base.NikeToolbar
import com.abolfazloskooii.nikeshop.Model.EXTRA_KEY_COMMENT
import com.abolfazloskooii.nikeshop.R
import com.abolfazloskooii.nikeshop.View.adapters.RvCommentAdapter
import com.abolfazloskooii.nikeshop.ViewModels.CommentViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class CommentAllActivity : Base.NikeActivity() {
    var check = false
    private val viewModel: CommentViewModel by viewModel {
        parametersOf(
            intent.extras?.getInt(
                EXTRA_KEY_COMMENT
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment_all)

        val commentsRv = findViewById<RecyclerView>(R.id.commentsRv)
        val commentListToolbar = findViewById<NikeToolbar>(R.id.commentListToolbar)
        val sort_by_lasted = findViewById<ImageView>(R.id.sort_by_lasted)
        val insertCommentBtn = findViewById<ImageView>(R.id.insertCommentBtn)

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        commentsRv.layoutManager = layoutManager
        commentsRv.setHasFixedSize(true)

        viewModel.getCommentAll().observe(this) {
            commentsRv.adapter = RvCommentAdapter(it, true)
        }

        commentListToolbar.onBackListener = View.OnClickListener { finish() }

        viewModel.setProgress().observe(this) {
            progress(it)
        }

        sort_by_lasted.setOnClickListener {

            check = !check

            lifecycleScope.launch {
                progress(true)
                delay(200)
                layoutManager.stackFromEnd = check
                layoutManager.reverseLayout = check
                progress(false)

            }
        }

        insertCommentBtn.setOnClickListener {
            supportFragmentManager.beginTransaction().setTransition(TRANSIT_FRAGMENT_MATCH_ACTIVITY_CLOSE)
                .add(
                R.id.commentAllRootMain, AddComment::class.java,Bundle().apply { putInt(EXTRA_KEY,intent.extras!!.getInt(EXTRA_KEY_COMMENT)) }
            )
                .commit()
        }


    }

    override fun onResume() {
        super.onResume()
        viewModel.getCommentAll()
    }
}