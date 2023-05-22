package com.abolfazloskooii.nikeshop.Model

data class Comment(
	val date: String? = null,
	val author: Author? = null,
	val id: Int? = null,
	val title: String? = null,
	val content: String? = null
)

data class Author(
	val email: String? = null
)

const val EXTRA_KEY_COMMENT = "comment"

