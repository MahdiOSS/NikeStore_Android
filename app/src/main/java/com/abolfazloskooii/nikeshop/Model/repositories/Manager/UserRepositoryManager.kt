package com.abolfazloskooii.nikeshop.Model.repositories.Manager

import io.reactivex.rxjava3.core.Completable

interface UserRepositoryManager {

    fun login (password : String , username : String) : Completable

    fun signUp (password : String , username : String) : Completable

    fun runToken ()

    fun getUserName() : String

    fun signOut ()

    fun saveImage(uri : String)

    fun getImage() : String
}