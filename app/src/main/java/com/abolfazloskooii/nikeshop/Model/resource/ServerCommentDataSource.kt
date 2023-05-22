package com.abolfazloskooii.nikeshop.Model.resource

import com.abolfazloskooii.nikeshop.Model.AddComment
import com.abolfazloskooii.nikeshop.Model.Comment
import com.abolfazloskooii.nikeshop.Model.resource.DataSource.CommentDataSource
import com.abolfazloskooii.nikeshop.Servies.http.ApiServies
import com.google.gson.JsonObject
import io.reactivex.rxjava3.core.Single

class ServerCommentDataSource(private val apiServies: ApiServies) : CommentDataSource {
    override fun getComment(q: Int): Single<List<Comment>> {
        return apiServies.getComment(q)
    }

    override fun addComment(addComment: AddComment): Single<AddComment> = apiServies.addComment(JsonObject().apply {
        addProperty("title",addComment.title)
        addProperty("product_id",addComment.productId)
        addProperty("content",addComment.content)
    })
}