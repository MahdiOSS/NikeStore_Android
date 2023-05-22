package com.abolfazloskooii.nikeshop.ViewModels

import com.abolfazloskooii.nikeshop.Base.Base
import com.abolfazloskooii.nikeshop.Base.threadNetWortRequest
import com.abolfazloskooii.nikeshop.Model.AddComment
import com.abolfazloskooii.nikeshop.Model.repositories.CommentRepository
import com.abolfazloskooii.nikeshop.Model.repositories.Manager.CommentRepositoryManager
import io.reactivex.rxjava3.core.Single

class AddCommentViewModel(private val commentRepository: CommentRepositoryManager) : Base.NikeViewModel() {

    fun addComment(addComment: AddComment) : Single<AddComment> =
        commentRepository.addComment(addComment)


}