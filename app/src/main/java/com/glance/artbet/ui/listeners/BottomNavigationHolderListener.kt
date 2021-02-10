package com.glance.artbet.ui.listeners

import com.glance.artbet.ui.base.BaseFragment
import kotlin.reflect.KClass

interface BottomNavigationHolderListener {
    fun setSelectedTab(tabStartFragmentClass: KClass<out BaseFragment<*>>)
    fun setBottomNavigationViewVisibility(visibility: Int)
    fun getSelectedNavHostId(): Int
    fun showUnreadIndicator(shouldShow: Boolean)

    companion object {
        val empty = object : BottomNavigationHolderListener {
            override fun setSelectedTab(tabStartFragmentClass: KClass<out BaseFragment<*>>) = Unit
            override fun setBottomNavigationViewVisibility(visibility: Int) = Unit
            override fun getSelectedNavHostId() = 0
            override fun showUnreadIndicator(shouldShow: Boolean) {}
        }
    }
}