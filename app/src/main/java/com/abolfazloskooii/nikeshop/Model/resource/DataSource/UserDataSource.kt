package com.abolfazloskooii.nikeshop.Model.resource.DataSource

import com.abolfazloskooii.nikeshop.Model.MessageResponse
import com.abolfazloskooii.nikeshop.Model.TokenResponse
import io.reactivex.rxjava3.core.Single

interface UserDataSource {

    fun login (password : String , username : String) : Single<TokenResponse>

    fun signUp (password : String , username : String) : Single<MessageResponse>

    fun runToken ()

    fun saveToken (Token : String , refreshToken : String? )

    fun getUserName() : String

    fun saveUserName ( username: String )

    fun signOut ()

    fun saveImage(uri : String)

    fun getPic() : String

}