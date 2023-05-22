package com.abolfazloskooii.nikeshop.View.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import com.abolfazloskooii.nikeshop.Auth.Auth
import com.abolfazloskooii.nikeshop.Base.Base
import com.abolfazloskooii.nikeshop.R
import com.abolfazloskooii.nikeshop.ViewModels.ProfileViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class ProfileFragment : Base.NikeFragment() {
    val viewModel: ProfileViewModel by viewModel()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<TextView>(R.id.favoriteSection).setOnClickListener{
            startActivity(Intent(requireContext(),FavoriteActivity::class.java))
        }

        view.findViewById<TextView>(R.id.order_His).setOnClickListener {
            startActivity(Intent(requireContext(),OrderHistoryActivity::class.java))
        }

    }

    private fun check(view: View) {

        val authBtn = view.findViewById<TextView>(R.id.authBtn)
        val usernameTv = view.findViewById<TextView>(R.id.usernameTv)

        if (viewModel.signIn) {
            authBtn.text = getString(R.string.signOut)
            authBtn.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_sign_out, 0)
            usernameTv.text = viewModel.username
            authBtn.setOnClickListener {
                viewModel.signOut()
                check(requireView())
            }
        } else {
            authBtn.text = getString(R.string.signIn)
            usernameTv.text = getString(R.string.guest_user)
            authBtn.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_sign_in, 0)
            authBtn.setOnClickListener {
                startActivity(Intent(requireContext(), Auth::class.java))
            }
        }
    }

    override fun onResume() {
        super.onResume()
        check(requireView())
    }
}