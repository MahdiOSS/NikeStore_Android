package com.abolfazloskooii.nikeshop.Servies.error

import com.abolfazloskooii.nikeshop.R
import org.json.JSONObject
import retrofit2.HttpException
import timber.log.Timber

class ConvertException() {
     object errorNikeE {

        fun convert(throwable: Throwable?): NikeException {

            return if (throwable is HttpException) {

                val errorJsonObject = JSONObject(throwable.response()?.errorBody()!!.string())
                val errorMessage = errorJsonObject.getString("message")

                if (throwable.code() == 401) {
                    return NikeException(
                        NikeException.ErrorType.AUTH,
                        serverMessage = errorMessage
                    )
                } else
                    NikeException(NikeException.ErrorType.LOW, serverMessage = errorMessage)
            } else
                NikeException(NikeException.ErrorType.LOW, R.string.unknown_Error, null)

        }


    }
}
