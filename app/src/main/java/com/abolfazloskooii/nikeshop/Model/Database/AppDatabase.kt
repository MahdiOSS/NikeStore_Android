package com.abolfazloskooii.nikeshop.Model.Database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.abolfazloskooii.nikeshop.Model.Product
import com.abolfazloskooii.nikeshop.Model.resource.LocalProductsDataSource
@Database(entities = [Product::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase(){
   abstract fun getDatabase() : LocalProductsDataSource
}