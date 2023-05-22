package com.abolfazloskooii.nikeshop.View.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.abolfazloskooii.nikeshop.Model.Comment
import com.abolfazloskooii.nikeshop.R

class RvCommentAdapter(private val data: List<Comment>, private val showAll: Boolean) :
    RecyclerView.Adapter<RvCommentAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val commentTitleTv: TextView = itemView.findViewById(R.id.commentTitleTv)
        val commentContentTv: TextView = itemView.findViewById(R.id.commentContentTv)
        val commentDateTv: TextView = itemView.findViewById(R.id.commentDateTv)
        val commentAuthor: TextView = itemView.findViewById(R.id.commentAuthor)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false)
        )


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = data[position]

        holder.commentDateTv.text = current.date
        holder.commentAuthor.text = current.author?.email
        holder.commentContentTv.text = current.content
        holder.commentTitleTv.text = current.title

    }

    override fun getItemCount(): Int = if (data.size >= 5 && showAll) data.size else 5

}