package com.example.historyvideo.base

import android.app.Activity
import android.inputmethodservice.Keyboard
import android.inputmethodservice.KeyboardView
import android.os.Bundle
import android.os.Handler
import android.os.PersistableBundle
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.historyvideo.R
import java.util.*
import kotlin.properties.Delegates

abstract class BaseActivity<V : BaseViewModel, B : ViewDataBinding> : AppCompatActivity() {
    private lateinit var binding: B
    private lateinit var viewModel: V
    private var isKeepShowingKeyboard by Delegates.notNull<Boolean>()
    private lateinit var handler: Handler

    protected abstract fun getLayoutId(): Int

    protected abstract fun getViewModelClass(): Class<V>

    protected abstract fun onAppEvent(event: AppEvent<String, Objects>)

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        binding = DataBindingUtil.setContentView(this, getLayoutId())
        window.navigationBarColor = resources.getColor(R.color.black)
        viewModel = ViewModelProvider(this).get(getViewModelClass())
        viewModel.getViewEventLiveData().observe(this) { event ->
            if (event == null) {
                return@observe
            }
            onAppEvent(event)
        }
        viewModel.getLoadingLiveData().observe(this) { loading ->
            showLoading(loading != null && loading)
        }
    }

    protected fun showLoading(isLoading: Boolean) {}

    fun getBinding(): B {
        return binding
    }

    fun getViewModel(): V {
        return viewModel
    }

    protected fun replaceFragment(id: Int, fragment: Fragment, addToBackStack: Boolean) {
        handler.post {
            val transaction =
                supportFragmentManager.beginTransaction()
            transaction.setCustomAnimations(
                R.anim.slide_in_from_right,
                R.anim.slide_out_to_left,
                R.anim.slide_in_from_left,
                R.anim.slide_out_to_right
            )
            transaction.replace(id, fragment, fragment.tag)
            if (addToBackStack) {
                transaction.addToBackStack(fragment.tag)
            }
            transaction.commitAllowingStateLoss()
        }
    }

    protected open fun replaceFragment(
        id: Int,
        fragment: Fragment?,
        addToBackStack: Boolean,
        tag: String?
    ) {
        handler.post {
            val transaction =
                supportFragmentManager.beginTransaction()
            transaction.setCustomAnimations(
                R.anim.slide_in_from_right,
                R.anim.slide_out_to_left,
                R.anim.slide_in_from_left,
                R.anim.slide_out_to_right
            )
            transaction.replace(id, fragment!!, tag)
            if (addToBackStack) {
                transaction.addToBackStack(tag)
            }
            transaction.commitAllowingStateLoss()
        }
    }

    protected open fun showFragment(
        id: Int,
        fragment: Fragment?,
        addToBackStack: Boolean,
        tag: String?
    ) {
        handler.post {
            val transaction =
                supportFragmentManager.beginTransaction()
            transaction.setCustomAnimations(
                R.anim.slide_in_from_right,
                R.anim.slide_out_to_left,
                R.anim.slide_in_from_left,
                R.anim.slide_out_to_right
            )
            transaction.add(id, fragment!!, tag)
            if (addToBackStack) {
                transaction.addToBackStack(tag)
            }
            transaction.commitAllowingStateLoss()
        }
    }

    protected open fun showFragmentWithoutAnimation(
        id: Int,
        fragment: Fragment?,
        addToBackStack: Boolean,
        tag: String?
    ) {
        handler.post {
            val transaction =
                supportFragmentManager.beginTransaction()
            transaction.add(id, fragment!!, tag)
            if (addToBackStack) {
                transaction.addToBackStack(tag)
            }
            transaction.commitAllowingStateLoss()
        }
    }

    fun setScreenNoLimit() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
    }

    open fun setWindowFlag(activity: Activity, bits: Int, on: Boolean) {
        val win = activity.window
        val winParams = win.attributes
        if (on) {
            winParams.flags = winParams.flags or bits
        } else {
            winParams.flags = winParams.flags and bits.inv()
        }
        win.attributes = winParams
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null && !isKeepShowingKeyboard) {
            KeyboardUtils.hideSoftKeyboard(this)
        }
        return super.dispatchTouchEvent(ev)
    }
}

