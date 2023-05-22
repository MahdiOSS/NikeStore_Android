package com.abolfazloskooii.nikeshop.Model.resource

import android.content.SharedPreferences
import com.abolfazloskooii.nikeshop.Model.MessageResponse
import com.abolfazloskooii.nikeshop.Model.TokenContainer
import com.abolfazloskooii.nikeshop.Model.TokenResponse
import com.abolfazloskooii.nikeshop.Model.resource.DataSource.UserDataSource
import io.reactivex.rxjava3.core.Single

class LocalUserDataSource(private val sharedPreferences: SharedPreferences) : UserDataSource {
    override fun login(password : String , username : String): Single<TokenResponse> {
        TODO("Not yet implemented")
    }

    override fun signUp(password : String , username : String): Single<MessageResponse> {
        TODO("Not yet implemented")
    }

    override fun runToken() {
        TokenContainer.update(sharedPreferences.getString("Token",null),sharedPreferences.getString("refresh_Token",null))
    }

    override fun saveToken(Token: String, refreshToken: String?) {
        sharedPreferences.edit().apply {
            putString("Token" , Token)
            putString("refresh_Token" , refreshToken)
        }.apply()
    }

    override fun getUserName(): String = sharedPreferences.getString("username","") ?: ""

    override fun saveUserName(username: String) {
        sharedPreferences.edit().putString("username",username).apply()
    }

    override fun signOut() {
        sharedPreferences.edit().clear().apply()
    }

    override fun saveImage(uri: String) {
        sharedPreferences.edit().putString("pic",uri).apply()
    }

    override fun getPic(): String =
        sharedPreferences.getString("pic","") ?: ""


}