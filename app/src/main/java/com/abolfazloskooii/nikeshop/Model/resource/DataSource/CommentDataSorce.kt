package com.abolfazloskooii.nikeshop.Model.resource.DataSource

import com.abolfazloskooii.nikeshop.Model.AddComment
import com.abolfazloskooii.nikeshop.Model.Comment
import io.reactivex.rxjava3.core.Single

interface CommentDataSource {

    fun getComment(q : Int) : Single<List<Comment>>

    fun addComment(addComment: AddComment) : Single<AddComment>

}