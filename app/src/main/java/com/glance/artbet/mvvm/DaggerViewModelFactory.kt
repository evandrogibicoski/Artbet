package com.glance.artbet.mvvm

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.glance.artbet.data.local_storage.LocalStorageRepository
import javax.inject.Inject
import javax.inject.Provider


@Suppress("UNCHECKED_CAST")
class DaggerViewModelFactory @Inject constructor(
        private var viewModelsMap: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>,
        private val app: Application,
//        private val chatApiRepository: ChatApiRepository,
        private val localStorageRepository: LocalStorageRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return checkChatViewModel(modelClass) ?: getExistingViewModel(modelClass)
    }

    private fun <T : ViewModel> checkChatViewModel(modelClass: Class<T>): T? {
//        if (modelClass == ChatFragmentViewModel::class.java) {
//            val chatModel =
//                    ChatFragmentViewModel(app, chatApiRepository, localStorageRepository) as T
//            val changed = viewModelsMap.toMutableMap().apply {
//                put(modelClass, Provider {
//                    chatModel
//                })
//            }
//            viewModelsMap = changed
//            return chatModel
//        }
        return null
    }

    fun <T : ViewModel> getExistingViewModel(modelClass: Class<T>): T {
        val creator = viewModelsMap[modelClass] ?: viewModelsMap.asIterable().firstOrNull {
            modelClass.isAssignableFrom(it.key)
        }?.value ?: throw IllegalArgumentException("unknown model class $modelClass")
        return try {
            creator.get() as T
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}