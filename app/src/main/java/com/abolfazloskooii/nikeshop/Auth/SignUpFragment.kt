package com.abolfazloskooii.nikeshop.Auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE
import com.abolfazloskooii.nikeshop.Base.Base
import com.abolfazloskooii.nikeshop.Base.CompletableNike
import com.abolfazloskooii.nikeshop.Base.threadNetWortRequest
import com.abolfazloskooii.nikeshop.R
import com.abolfazloskooii.nikeshop.ViewModels.AuthViewModel
import com.abolfazloskooii.nikeshop.databinding.FragmentSignUpBinding
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignUpFragment : Base.NikeFragment() {

    private val viewModel: AuthViewModel by viewModel()
    private val compositeDisposable = CompositeDisposable()
    lateinit var binding : FragmentSignUpBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignUpBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val passwordEt = binding.passwordEt
        val emailEt = binding.emailEt
        val signUpBtn = binding.signUpBtn
        val passwordEtRepeat = binding.passwordEtRepeat
        val loginLinkBtn = binding.loginLinkBtn


        viewModel.setProgress().observe(viewLifecycleOwner) {
            progress(it)
        }

        signUpBtn.setOnClickListener {
            if (passwordEt.text.isNotEmpty() && emailEt.text.isNotEmpty()) {
                if (passwordEt.text.toString() == passwordEtRepeat.text.toString()) {
                    if (passwordEt.text.length >= 6) {
                        viewModel.signup(passwordEt.text.toString(), emailEt.text.toString())
                            .threadNetWortRequest()
                            .subscribe(object : CompletableNike(compositeDisposable) {
                                override fun onComplete() {
                                    requireActivity().finish()
                                }
                            })
                    } else
                        showToast(getString(R.string.Error1),requireContext())
                } else
                    showToast(getString(R.string.Error),requireContext())
            }else
                showToast(getString(R.string.Error1),requireContext())

        }
        loginLinkBtn.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().apply {
                replace(R.id.FrameAuth, LoginFragment())
                    .setTransition(TRANSIT_FRAGMENT_FADE)
            }.commit()
        }
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.clear()
    }
}