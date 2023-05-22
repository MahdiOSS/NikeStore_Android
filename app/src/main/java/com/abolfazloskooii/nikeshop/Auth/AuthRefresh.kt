package com.abolfazloskooii.nikeshop.Auth

import com.abolfazloskooii.nikeshop.Model.TokenContainer
import com.abolfazloskooii.nikeshop.Model.resource.CLIENT_SECRET
import com.abolfazloskooii.nikeshop.Model.resource.DataSource.UserDataSource
import com.abolfazloskooii.nikeshop.Servies.http.ApiServies
import com.google.gson.JsonObject
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import timber.log.Timber

class AuthRefresh : Authenticator, KoinComponent {

    private val apiServies: ApiServies by inject()
    private val localUserDataSource: UserDataSource by inject()

    override fun authenticate(route: Route?, response: Response): Request? {
        Timber.tag("REFRESH_").i(response.request.url.toString())
        if (TokenContainer.token != null && !response.request.url.pathSegments.last()
                .equals("token", false)
        ) {

            try {
                val refresh = refreshToken()
                if (refresh.isEmpty())
                    return null

                return response.request.newBuilder()
                    .header("Authentication", "Bearer ${TokenContainer.token}").build()

            } catch (e: Exception) {
                Timber.e(e)
            }
        }


        return null
    }

    private fun refreshToken(): String {

        val response = apiServies.refreshToken(JsonObject().apply {
            addProperty("grant_type", "refresh_token")
            addProperty("client_id", 2)
            addProperty("refresh_token", TokenContainer.refreshToken)
            addProperty("client_secret", CLIENT_SECRET)
        }).execute()

        response.body().let {
            TokenContainer.update(it?.access_token, it?.refresh_token)
            localUserDataSource.saveToken(it!!.access_token, it.refresh_token)
            return it.access_token
        }

    }
}