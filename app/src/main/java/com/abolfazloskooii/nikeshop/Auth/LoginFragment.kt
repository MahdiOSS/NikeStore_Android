package com.abolfazloskooii.nikeshop.Auth

import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN
import com.abolfazloskooii.nikeshop.Base.Base
import com.abolfazloskooii.nikeshop.Base.CompletableNike
import com.abolfazloskooii.nikeshop.Base.threadNetWortRequest
import com.abolfazloskooii.nikeshop.R
import com.abolfazloskooii.nikeshop.ViewModels.AuthViewModel
import com.abolfazloskooii.nikeshop.databinding.FragmentLoginBinding
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.koin.android.ext.android.inject

class LoginFragment : Base.NikeFragment() {

    private val viewModel: AuthViewModel by inject()
    private val compositeDisposable = CompositeDisposable()
    lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(layoutInflater)
//        return inflater.inflate(R.layout.fragment_login, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val passwordEt = binding.passwordEt
        val loginBtn = binding.loginBtn
        val emailEt = binding.emailEt
        val signUpLinkBtn = binding.signUpLinkBtn

        viewModel.setProgress().observe(viewLifecycleOwner) {
            progress(it)
        }

        passwordEt.setOnClickListener {
            passwordEt.transformationMethod = PasswordTransformationMethod()
        }

        loginBtn.setOnClickListener {
            if (passwordEt.text.isNotEmpty() && emailEt.text.isNotEmpty()) {
                if (passwordEt.text.length >= 6) {
                    viewModel.login(passwordEt.text.toString(), emailEt.text.toString())
                        .threadNetWortRequest()
                        .subscribe(object : CompletableNike(compositeDisposable) {
                            override fun onComplete() {
                                requireActivity().finish()
                            }
                        })
                } else
                    showToast(getString(R.string.Error1), requireContext())
            } else
                showToast(getString(R.string.Error), requireContext())
        }
        signUpLinkBtn.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().apply {
                replace(R.id.FrameAuth, SignUpFragment())
                    .setTransition(TRANSIT_FRAGMENT_OPEN)
            }.commit()
        }
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.clear()
    }
}