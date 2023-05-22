package com.abolfazloskooii.nikeshop.ViewModels

import com.abolfazloskooii.nikeshop.Base.Base
import com.abolfazloskooii.nikeshop.Model.repositories.Manager.UserRepositoryManager
import io.reactivex.rxjava3.core.Completable

class AuthViewModel(private val userRepositoryManager: UserRepositoryManager) : Base.NikeViewModel() {

    fun login (password : String,username : String) : Completable {
        progressLiveData.value = true
       return userRepositoryManager.login(password, username).doFinally {
           progressLiveData.postValue(false)
       }
    }

    fun signup (password : String,username : String) : Completable {
        progressLiveData.value = true
        return userRepositoryManager.signUp(password, username).doFinally {
            progressLiveData.postValue( false)
        }
    }

    fun setProgress() = progressLiveData

}