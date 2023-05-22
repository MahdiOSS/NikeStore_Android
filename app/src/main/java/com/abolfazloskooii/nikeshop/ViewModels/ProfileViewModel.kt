package com.abolfazloskooii.nikeshop.ViewModels

import com.abolfazloskooii.nikeshop.Base.Base
import com.abolfazloskooii.nikeshop.Model.TokenContainer
import com.abolfazloskooii.nikeshop.Model.repositories.Manager.UserRepositoryManager

class ProfileViewModel(private val userRepositoryManager: UserRepositoryManager) : Base.NikeViewModel() {

    val username : String
    get() = userRepositoryManager.getUserName()

    val username2 : String
        get() = userRepositoryManager.getUserName()

    val signIn : Boolean
    get() = TokenContainer.token != null

    fun signOut () = userRepositoryManager.signOut()

    fun saveImage(uri : String) = userRepositoryManager.saveImage(uri)

    fun getImage() : String = userRepositoryManager.getImage()

}