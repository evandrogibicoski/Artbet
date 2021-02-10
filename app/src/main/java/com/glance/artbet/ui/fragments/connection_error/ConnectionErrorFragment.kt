package com.glance.artbet.ui.fragments.connection_error

import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.glance.artbet.R
import com.glance.artbet.ui.base.BaseFragment
import com.glance.artbet.utils.extensions.android.injectViewModel
import com.glance.artbet.utils.extensions.android.setDarkStatusBarText
import com.glance.artbet.utils.extensions.android.setTransparentStatusBar
import kotlinx.android.synthetic.main.fragment_connection_error.*

class ConnectionErrorFragment : BaseFragment<ConnectionErrorFragmentViewModel>() {

    override fun provideViewModel(viewModelFactory: ViewModelProvider.Factory): ConnectionErrorFragmentViewModel {
        return baseActivity.injectViewModel(viewModelFactory)
    }

    override fun layout() = R.layout.fragment_connection_error

    override fun initialization(view: View, isFirstInit: Boolean) {
        baseActivity.setTransparentStatusBar(true)
        baseActivity.setDarkStatusBarText(true)
        if (isFirstInit) {
            initClicks()
            listenUpdates()
        }
    }

    override fun shouldExit() = false

    private fun initClicks(){
        retry_button.setBounceClickListener {
            viewModel.callRequest()
        }
    }

    private fun listenUpdates(){
        viewModel.resentSuccessfullyData.observe(this){
            if (it) onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.clear()
    }
}