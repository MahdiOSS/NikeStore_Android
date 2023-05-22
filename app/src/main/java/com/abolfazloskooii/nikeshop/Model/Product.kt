package com.abolfazloskooii.nikeshop.Model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
@Entity(tableName = "products")
@kotlinx.parcelize.Parcelize
data class Product(
	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("price")
	val price: Int? = null,

	@field:SerializedName("discount")
	val discount: Int? = null,

	@PrimaryKey
	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("previous_price")
	val previousPrice: Int? = null,

	@field:SerializedName("status")
	val status: Int? = null
):Parcelable{
	var isFavorite = false
}

const val SORT_BY_LASTED = 0
const val SORT_BY_BEST_SELLER = 1
const val SORT_BY_DESC = 2
const val SORT_BY_ASC = 3
