package com.glance.artbet.mvvm

import android.app.Application
import androidx.lifecycle.AndroidViewModel

abstract class BaseViewModel(app: Application) : AndroidViewModel(app), LoadingStateListener {

}