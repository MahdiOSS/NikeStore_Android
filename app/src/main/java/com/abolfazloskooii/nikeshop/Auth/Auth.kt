package com.abolfazloskooii.nikeshop.Auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.abolfazloskooii.nikeshop.Base.Base
import com.abolfazloskooii.nikeshop.R
import timber.log.Timber

class Auth : Base.NikeActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.auth_activity)

        Toast.makeText(viewContext," وارد حساب کاربری خود شوید !", Toast.LENGTH_SHORT).show()

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.FrameAuth,LoginFragment())
            commit()
        }

    }
}