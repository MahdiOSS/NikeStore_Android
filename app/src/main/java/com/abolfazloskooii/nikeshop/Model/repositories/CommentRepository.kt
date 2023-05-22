package com.abolfazloskooii.nikeshop.Model.repositories

import com.abolfazloskooii.nikeshop.Model.AddComment
import com.abolfazloskooii.nikeshop.Model.Comment
import com.abolfazloskooii.nikeshop.Model.repositories.Manager.CommentRepositoryManager
import com.abolfazloskooii.nikeshop.Model.resource.ServerCommentDataSource
import io.reactivex.rxjava3.core.Single

class CommentRepository(private val serverCommentDataSource: ServerCommentDataSource) : CommentRepositoryManager{
    override fun getComment(q: Int): Single<List<Comment>> {
        return serverCommentDataSource.getComment(q)
    }

    override fun addComment(addComment: AddComment): Single<AddComment> =  serverCommentDataSource.addComment(addComment)

}