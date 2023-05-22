package com.abolfazloskooii.nikeshop.Model.resource

import com.abolfazloskooii.nikeshop.Model.MessageResponse
import com.abolfazloskooii.nikeshop.Model.TokenResponse
import com.abolfazloskooii.nikeshop.Model.resource.DataSource.UserDataSource
import com.abolfazloskooii.nikeshop.Servies.http.ApiServies
import com.google.gson.JsonObject
import io.reactivex.rxjava3.core.Single

const val CLIENT_SECRET = "kyj1c9sVcksqGU4scMX7nLDalkjp2WoqQEf8PKAC"

 class ServerUserDataSource(private val apiServies: ApiServies) : UserDataSource{

    override fun login(password : String , username : String): Single<TokenResponse> =
        apiServies.login(JsonObject().apply {
            addProperty("grant_type","password")
            addProperty("client_id",2)
            addProperty("username",username)
            addProperty("password",password)
            addProperty("client_secret", CLIENT_SECRET)
        })


    override fun signUp(password : String , username : String): Single<MessageResponse> =
        apiServies.signUp(JsonObject().apply {
            addProperty("email",username)
            addProperty("password",password)
        })



    override fun runToken() {
        TODO("Not yet implemented")
    }

    override fun saveToken(Token: String, refreshToken: String?) {
        TODO("Not yet implemented")
    }

    override fun getUserName(): String {
        TODO("Not yet implemented")
    }

     override fun saveUserName(username: String) {
         TODO("Not yet implemented")
     }

     override fun signOut() {
         TODO("Not yet implemented")
     }

     override fun saveImage(uri: String) {
         TODO("Not yet implemented")
     }

     override fun getPic(): String {
         TODO("Not yet implemented")
     }

 }