package com.abolfazloskooii.nikeshop.Base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.abolfazloskooii.nikeshop.Auth.Auth
import com.abolfazloskooii.nikeshop.R
import com.abolfazloskooii.nikeshop.Servies.error.NikeException
import com.google.android.material.snackbar.Snackbar
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import timber.log.Timber


const val TAG = "ABOLFAZL"

interface Base {

    abstract class NikeActivity : NikeView, AppCompatActivity() {
        override val layout: CoordinatorLayout?
            get() {
                val viewGroup = window.decorView.findViewById(android.R.id.content) as ViewGroup
                if (viewGroup !is CoordinatorLayout) {
                    viewGroup.children.forEach {
                        if (it is CoordinatorLayout)
                            return it
                    }
                    throw IllegalStateException("RootView must be instance of CoordinatorLayout")
                } else
                    return viewGroup
            }
        override val viewContext: Context?
            get() = this


        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            EventBus.getDefault().register(this)
        }

        override fun onDestroy() {
            EventBus.getDefault().unregister(this)
            super.onDestroy()
        }
    }

    abstract class NikeFragment : NikeView, Fragment() {
        override val viewContext: Context?
            get() = context
        override val layout: CoordinatorLayout?
            get() = view as CoordinatorLayout?

        override fun onStart() {
            super.onStart()
            EventBus.getDefault().register(this)
        }

        override fun onStop() {
            super.onStop()
            EventBus.getDefault().unregister(this)
        }
    }

    interface NikeView {
        val layout: CoordinatorLayout?
        val viewContext: Context?


        fun showEmptyState(layoutId: Int): View? {
            viewContext.let {
                layout.let { view ->
                    var emptyState = view?.findViewById<View>(R.id.emptyStateRootView)
                    if (emptyState == null) {
                        emptyState = LayoutInflater.from(it).inflate(layoutId, layout, false)
                        layout?.addView(emptyState)
                    }
                    emptyState?.visibility = View.VISIBLE
                    return emptyState
                }
            }
            return null
        }


        fun progress(show: Boolean) {
            layout.let {
                viewContext.let { context ->
                    var loadingAnimation = it?.findViewById<View>(R.id.anim_layout)
                    if (loadingAnimation == null && show) {
                        loadingAnimation =
                            LayoutInflater.from(context).inflate(R.layout.anim_layout, it, false)
                        it?.addView(loadingAnimation)
                    }
                    loadingAnimation?.visibility = if (show) View.VISIBLE else View.GONE
                }
            }
        }

        fun showSnackBar(message: String) {
            Snackbar.make(layout!!, message, Snackbar.LENGTH_SHORT)
                .show()
        }
        fun showToast(message: String,context: Context){
            Toast.makeText(context,message,Toast.LENGTH_SHORT).show()
        }
        @Subscribe(threadMode = ThreadMode.MAIN)
        fun showError(nikeException: NikeException) {
            layout.let {
                viewContext?.let {
                    if (nikeException.errorType == NikeException.ErrorType.LOW) {
                        showToast(
                            nikeException.serverMessage ?: it.getString(nikeException.stringId), it
                        )
                    } else if (nikeException.errorType == NikeException.ErrorType.AUTH) {
                        it.startActivity(Intent(it, Auth::class.java))
                    }
                    EventBus.getDefault().unregister(this)
                }
            }
        }
    }

    abstract class NikeViewModel : ViewModel() {
        protected var progressLiveData = MutableLiveData<Boolean>()
        val disposable = CompositeDisposable()

        override fun onCleared() {
            disposable.clear()
            super.onCleared()
        }
    }
}


const val EXTRA_KEY = "data"