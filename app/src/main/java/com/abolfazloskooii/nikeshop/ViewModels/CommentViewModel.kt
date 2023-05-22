package com.abolfazloskooii.nikeshop.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.abolfazloskooii.nikeshop.Base.Base
import com.abolfazloskooii.nikeshop.Base.SingleNike
import com.abolfazloskooii.nikeshop.Base.threadNetWortRequest
import com.abolfazloskooii.nikeshop.Model.AddComment
import com.abolfazloskooii.nikeshop.Model.Comment
import com.abolfazloskooii.nikeshop.Model.repositories.Manager.CommentRepositoryManager

class CommentViewModel(private val postId: Int, private val commentRepositoryManager: CommentRepositoryManager) :
    Base.NikeViewModel() {
    private val commentLiveData = MutableLiveData<List<Comment>>()

    init {
       reqComment()
    }

    private fun reqComment(){
        progressLiveData.value = true
        commentRepositoryManager.getComment(postId).threadNetWortRequest()
            .doFinally { progressLiveData.value = false }
            .subscribe(object : SingleNike<List<Comment>>(disposable){
                override fun onSuccess(t: List<Comment>) {
                    commentLiveData.value = t
                }
            })
    }


    fun getCommentAll(): LiveData<List<Comment>> = commentLiveData

    fun setProgress(): LiveData<Boolean> = progressLiveData

}