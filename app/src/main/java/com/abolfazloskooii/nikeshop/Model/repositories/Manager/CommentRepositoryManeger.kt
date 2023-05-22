package com.abolfazloskooii.nikeshop.Model.repositories.Manager

import com.abolfazloskooii.nikeshop.Model.AddComment
import com.abolfazloskooii.nikeshop.Model.Comment
import io.reactivex.rxjava3.core.Single

interface CommentRepositoryManager {
    fun getComment(q : Int) : Single<List<Comment>>
    fun addComment(addComment: AddComment) : Single<AddComment>
}