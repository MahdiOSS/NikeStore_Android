package com.abolfazloskooii.nikeshop.Servies.error

import androidx.annotation.StringRes



class NikeException(val errorType: ErrorType, @StringRes val stringId: Int = 0,val serverMessage: String?) : Throwable() {
    enum class ErrorType {
        LOW, DIALOG, AUTH
    }
}