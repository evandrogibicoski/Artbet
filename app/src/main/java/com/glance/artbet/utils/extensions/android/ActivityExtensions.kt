package com.glance.artbet.utils.extensions.android

import android.app.Activity
import android.graphics.Color
import android.graphics.Rect
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import androidx.annotation.ColorRes
import androidx.annotation.IdRes
import androidx.core.content.ContextCompat
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.glance.artbet.R


fun Activity.setFullscreen(enable: Boolean) {
    if (enable) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                // Set the content to appear under the system bars so that the
                // content doesn't resize when the system bars hide and show.
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                // Hide the nav bar and status bar
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.attributes.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }
    } else {
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
    }
}

fun Activity.setDarkStatusBarText(isDarkTexts: Boolean) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val flags = window.decorView.systemUiVisibility
        window.decorView.systemUiVisibility =
            if (!isDarkTexts) flags and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            else flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    } else {
        window.statusBarColor =
            if (isDarkTexts) getColorRes(R.color.colorBlackOp15) else Color.TRANSPARENT
    }
}

fun Activity.setTransparentStatusBar(isTransparent: Boolean) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        window.apply {
            decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            if (isTransparent) addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            else {
                clearFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE and View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            }
            statusBarColor = if (isTransparent) Color.TRANSPARENT else Color.WHITE
        }
    } else {
        if (isTransparent) window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        else window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    }
}

fun Activity.setTransparentNavigationBar(isTransparent: Boolean) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        window.apply {
            if (isTransparent) addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            else clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        }
    }
}

fun ViewGroup.findViewAt(x: Int, y: Int): View? {
    forEach { child ->
        if (child is ViewGroup) {
            val foundView = child.findViewAt(x, y)
            if (foundView != null && foundView.isShown) {
                return foundView
            }
        } else {
            val location = intArrayOf(0, 0)
            child.getLocationOnScreen(location)
            val rect = Rect(
                location[0],
                location[1],
                location[0] + child.width,
                location[1] + child.height
            )
            if (rect.contains(x, y)) {
                return child
            }
        }
    }
    return null
}

/*fun Activity.hideKeyboardOnOutsideClick(view: View, clearFocus: Boolean = true) {
    if (view !is EditText) {
        val tapDetector = GestureDetectorCompat(this, OnSwipeTouchListener {
            hideKeyboard()
            if (clearFocus) {
                clearFocusFromEditTexts(view)
                setFocusToButtons(view)
            }
        })

        view.setOnTouchListener { _, event ->
            tapDetector.onTouchEvent(event)
        }
    }

    if (view is ViewGroup) {
        for (i: Int in 0..view.childCount) {
            view.getChildAt(i)?.let {
                hideKeyboardOnOutsideClick(it, clearFocus)
            }
        }
    }
}*/

fun clearFocusFromEditTexts(view: View) {
    if (view is EditText) {
        view.clearFocus()
    }
    if (view is ViewGroup) {
        for (i: Int in 0..view.childCount) {
            view.getChildAt(i)?.let {
                clearFocusFromEditTexts(it)
            }
        }
    }
}

fun setFocusToButtons(view: View) {
    if (view is Button) {
        view.requestFocus()
    }
    if (view is ViewGroup) {
        for (i: Int in 0..view.childCount) {
            view.getChildAt(i)?.let {
                setFocusToButtons(it)
            }
        }
    }
}

fun Activity.showKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    view.requestFocus()
    inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0)
}

fun Activity.hideKeyboard() {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    currentFocus?.let { focus ->
        inputMethodManager.hideSoftInputFromWindow(focus.windowToken, 0)
    }
}

fun Activity.showKeyboard() {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.toggleSoftInputFromWindow(
        window.decorView.windowToken,
        InputMethodManager.SHOW_FORCED, 0
    )
}

fun Activity.setStatusBarColor(@ColorRes colorResourceId: Int) {
    val window: Window = this.window
    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    window.statusBarColor = getColorRes(colorResourceId)
}

fun Activity.vibrate(durationMillis: Long) {
    val vibrator = ContextCompat.getSystemService(baseContext, Vibrator::class.java)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        vibrator?.vibrate(
            VibrationEffect.createOneShot(
                durationMillis,
                VibrationEffect.DEFAULT_AMPLITUDE
            )
        )
    } else {
        vibrator?.vibrate(durationMillis)
    }
}

fun FragmentActivity.addFragment(@IdRes containerId: Int, fragment: Fragment) {
    supportFragmentManager.beginTransaction()
        .add(containerId, fragment)
        .setPrimaryNavigationFragment(fragment)
        .commit()
}

val Activity.displayMetrics: DisplayMetrics
    get() {
        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)
        return metrics
    }

val Activity.statusBarHeight: Int
    get() {
        try {
            val windowStatusBarHeight = calculateStatusBarHeightByWindow()
            if (windowStatusBarHeight != 0) return windowStatusBarHeight

            val resourcesStatusBarHeight = calculateStatusBarHeightByResources()
            if (resourcesStatusBarHeight != 0) return resourcesStatusBarHeight
        } catch (e: Exception) {
        }
        return 0
    }

fun Activity.calculateStatusBarHeightByWindow(): Int {
    val rectangle = Rect()
    window.decorView.getWindowVisibleDisplayFrame(rectangle)
    return rectangle.top
}

/***
 * Magic method to detect keyboard, be careful
 */
fun Activity.onKeyboardStateAction(
    root: View?,
    onKeyboardState: (isShown: Boolean, heightDiffNegativePx: Float) -> Unit
) {
    if (root == null) {
        Log.d(this::class.java.simpleName, "root is NULL")
        return
    }

    root.viewTreeObserver.addOnGlobalLayoutListener {
        val r = Rect()
        root.getWindowVisibleDisplayFrame(r)

        val keyboardHeight = root.rootView.height - r.bottom

        val heightDiffPx = -(root.bottom.toFloat() - r.bottom.toFloat())

        if (keyboardHeight > 500) {
            onKeyboardState.invoke(true, heightDiffPx)
        } else {
            onKeyboardState.invoke(false, heightDiffPx)
        }
    }
}
