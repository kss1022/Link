package com.example.link.ui.start

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import com.example.link.R
import com.example.link.databinding.ActivityStartBinding
import com.example.link.ui.base.BaseActivity
import com.example.link.util.DeviceUtil
import com.example.link.util.lifecycle.SingleLiveEvent
import com.example.link.util.lifecycle.SystemUIType
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class StartActivity : BaseActivity<ActivityStartBinding, StartViewModel>() {

    override val viewModel: StartViewModel by viewModels()
    private val sharedViewModel : StartSharedViewModel by viewModels()

    @Inject
    lateinit var backPressCallback: OnBackPressedCallback

    @Inject
    lateinit var systemUIEvent: SingleLiveEvent<SystemUIType>

    override fun getViewBinding(): ActivityStartBinding =
        ActivityStartBinding.inflate(layoutInflater)

    override fun initViews() {
        binding.toolbar.isGone = true
    }

    override fun observeData() {
        sharedViewModel.navChangeEvent.observe(this){
            binding.toolbar.visibility = View.VISIBLE
        }


        systemUIEvent.observe(this) {
            when (it) {
                SystemUIType.NORMAL -> disableFullscreen()
                SystemUIType.FULLSCREEN -> enableFullscreen()
                SystemUIType.FULLSCREEN_WITHOUT_SYSTEM_UI -> hideSystemUI()
                SystemUIType.TRANSPARENT -> enableTransparent()
                else -> Unit
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onBackPressedDispatcher.addCallback(this, backPressCallback)
    }


    override fun onDestroy() {
        super.onDestroy()
        backPressCallback.remove()
    }




    /**
     * SystemUI
     */

    private fun disableFullscreen() {
        if (DeviceUtil.isAndroid5Later()) {
            window.apply {
                val systemUIBackgroundColor =
                    ContextCompat.getColor(this@StartActivity, R.color.whit_a50)
                statusBarColor = systemUIBackgroundColor
                navigationBarColor =
                    ContextCompat.getColor(this@StartActivity, R.color.black)
                clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
            }
        }
    }

    private fun enableFullscreen() {
        if (DeviceUtil.isAndroid5Later()) {
            val colorRes = R.color.green
            val systemUIBackgroundColor = ContextCompat.getColor(this, colorRes)
            window.statusBarColor = systemUIBackgroundColor
            window.navigationBarColor = ContextCompat.getColor(this@StartActivity, R.color.black)
            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            } else {
                window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            }

            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        }
    }

    private fun enableTransparent() {
        if (DeviceUtil.isAndroid5Later()) {
            window.apply {
                val systemUIBackgroundColor =
                    ContextCompat.getColor(this@StartActivity, R.color.black)
                statusBarColor = systemUIBackgroundColor
                navigationBarColor = ContextCompat.getColor(this@StartActivity, R.color.black)
            }
        }
    }


    private fun hideSystemUI() {
        if (DeviceUtil.isAndroid5Later()) {
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN)

        }
    }
}