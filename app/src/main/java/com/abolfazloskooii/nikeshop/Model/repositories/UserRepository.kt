package com.abolfazloskooii.nikeshop.Model.repositories

import com.abolfazloskooii.nikeshop.Model.TokenContainer
import com.abolfazloskooii.nikeshop.Model.TokenResponse
import com.abolfazloskooii.nikeshop.Model.repositories.Manager.UserRepositoryManager
import com.abolfazloskooii.nikeshop.Model.resource.DataSource.UserDataSource
import io.reactivex.rxjava3.core.Completable

class UserRepository(
    private val serverUserDataSource: UserDataSource,
    private val localUserDataSource: UserDataSource
) : UserRepositoryManager {

    override fun login(password: String, username: String): Completable
        = serverUserDataSource.login(password, username).doOnSuccess {
            onSuccessREQ(username,it)
        }.ignoreElement()


    override fun signUp(password: String, username: String): Completable =
         serverUserDataSource.signUp(password, username).flatMap {
            serverUserDataSource.login(password, username)
        }.
        doOnSuccess {
           onSuccessREQ(username,it)
        }.ignoreElement()


    override fun runToken() = localUserDataSource.runToken()

    override fun getUserName(): String =
        localUserDataSource.getUserName()

    override fun signOut() {
        localUserDataSource.signOut()
        TokenContainer.update(null,null)
    }

    override fun saveImage(uri: String) {
        localUserDataSource.saveImage(uri)
    }

    override fun getImage(): String = localUserDataSource.getUserName()


    private fun onSuccessREQ (username: String,it: TokenResponse){
        TokenContainer.update(it.access_token,it.refresh_token)
        localUserDataSource.saveToken(it.access_token, it.refresh_token)
        localUserDataSource.saveUserName(username)
    }


}