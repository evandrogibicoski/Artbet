package com.glance.artbet.ui.base

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.androidadvance.topsnackbar.TSnackbar
import com.glance.artbet.R
import com.glance.artbet.mvvm.BaseViewModel
import com.glance.artbet.ui.fragments.connection_error.ConnectionErrorFragmentViewModel
import com.glance.artbet.ui.listeners.*
import com.glance.artbet.ui.models.TransitionAnimationStatus
import com.glance.artbet.utils.ActionDebounceFilter
import com.glance.artbet.utils.extensions.android.ConnectionError
import com.glance.artbet.utils.extensions.android.Failure
import com.glance.artbet.utils.extensions.android.Loading
import com.glance.artbet.utils.extensions.android.Success
import com.glance.artbet.utils.extensions.android.view.animateBounce
import com.glance.artbet.utils.extensions.fromJson
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import javax.inject.Inject

abstract class BaseDialogFragment<V : BaseViewModel> : DialogFragment(),
    FragmentBackPressedListener, MessageClickListener {

    protected var showToastListener = ShowToastListener.empty
    protected var showSnackListener = ShowSnackListener.empty
    protected var backListener = ActivityBackPressedListener.empty
    protected var setUpSnackBar = SetUpSnackBar.empty
    private var transitionProgressListener = FragmentTransitionProgressListener.empty
    protected var bottomNavigationHolderListener = BottomNavigationHolderListener.empty
    var fragmentViewCreatedListener = FragmentViewCreatedListener.empty

    protected lateinit var viewModel: V

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var baseContext: Context

    @Inject
    lateinit var baseActivity: BaseActivity<*>

    @Inject
    lateinit var compositeDisposable: CompositeDisposable

    private val clicksFilter = ActionDebounceFilter()

    protected var rootView: View? = null

    protected val connectionErrorViewModel: ConnectionErrorFragmentViewModel by lazy {
        baseActivity.injectViewModel<ConnectionErrorFragmentViewModel>(viewModelFactory)
    }

    @LayoutRes
    protected abstract fun layout(): Int

    protected abstract fun initialization(view: View, isFirstInit: Boolean)
    abstract fun provideViewModel(viewModelFactory: ViewModelProvider.Factory): V

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity<*>)
            context.activityComponent.inject(this as BaseDialogFragment<BaseViewModel>)
        if (context is ShowToastListener)
            showToastListener = context
        if (context is ShowSnackListener)
            showSnackListener = context
        if (context is ActivityBackPressedListener)
            backListener = context
        if (context is SetUpSnackBar)
            setUpSnackBar = context
        if (context is FragmentTransitionProgressListener)
            transitionProgressListener = context
        if (context is BottomNavigationHolderListener)
            bottomNavigationHolderListener = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = provideViewModel(viewModelFactory)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        val view = if (layout() != 0)
            addProgressView(inflater.inflate(layout(), container, false))
        else
            super.onCreateView(inflater, container, savedInstanceState)
        return if (rootView == null) view else rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        fragmentViewCreatedListener.onFragmentViewCreated(view)
        initialization(view, rootView == null)
        rootView = view
        listenLoadingStateUpdates()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun addProgressView(rootView: View): View {
        val rootLayout = FrameLayout(baseContext)
        val layoutParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT,
            Gravity.CENTER
        )
        rootLayout.layoutParams = layoutParams
        rootLayout.addView(rootView)

        val progressLayout = baseActivity.layoutInflater.inflate(R.layout.dialog_progress, null)
        rootLayout.addView(progressLayout)
        return rootLayout
    }

    @CallSuper
    override fun onResume() {
        super.onResume()
        getCustomDialogSize()?.let { customSize ->
            dialog?.window?.setLayout(
                customSize.first,
                customSize.second
            )
        }
    }

    /**
     * Custom Width, Height
     */
    protected open fun getCustomDialogSize(): Pair<Int, Int>? = null

    protected fun showProgress(show: Boolean) {
        requireActivity().findViewById<View>(R.id.progress_layout)?.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun listenLoadingStateUpdates() {
        viewModel.loadingStateData.observe(viewLifecycleOwner) {
            Log.d("BaseDialogFragment", it.toString())
            when (it) {
                is Loading -> onLoadingStateLoading(it)
                is Success -> onLoadingStateSuccess(it)
                else -> onLoadingStateError(it)
            }
        }
    }

    protected open fun onLoadingStateLoading(loadingState: Loading<*>) {
        showProgress(loadingState.isLoading)
    }

    protected open fun onLoadingStateSuccess(successState: Success<*>) {
//        if (successState.wasResent) closeConnectionErrorScreen()
    }

    protected open fun onLoadingStateError(errorState: Result<*>) {
        when (errorState) {
            is Failure -> showErrorSnack(errorState.errorMessage)
            is ConnectionError -> openConnectionErrorScreen(errorState)
        }
    }

    protected open fun openConnectionErrorScreen(request: ConnectionError<*>) {
        connectionErrorViewModel.putRequest(request)
        findNavController().navigate(R.id.open_connection_error_screen)
    }

    protected fun closeConnectionErrorScreen() {
        connectionErrorViewModel.setClosed()
    }

    override fun onDestroy() {
        baseActivity.toggleKeyboard(false)
        compositeDisposable.clear()
        super.onDestroy()
    }

    override fun onBackPressed() {
        backListener.onBackPressed(this)
    }

    override fun shouldExit() = true

    fun View.setNotAnimatedClickListener(
        durationMillis: Long = 400,
        onClick: (view: View) -> Unit = {}
    ) {
        setNotAnimatedClickListener(this, durationMillis, onClick)?.addToDispose()
    }

    override fun setNotAnimatedClickListener(
        view: View,
        durationMillis: Long,
        onClick: (view: View) -> Unit
    ): Disposable? {
        return RxView.clicks(view)
            .filter { clicksFilter.filterAction(durationMillis) }
            .subscribe(
                { onClick(view) },
                { showErrorSnack(it.message ?: getString(R.string.error_click_operation)) })
    }

    protected inline fun <reified T> getJsonArgument(key: String, defaultJson: String? = null): T? {
        return fromJson(
            if (defaultJson.isNullOrBlank()) arguments?.getString(key) ?: ""
            else defaultJson
        )
    }

    fun View.setBounceClickListener(
        durationMillis: Long = 400,
        onClick: (view: View) -> Unit = {}
    ) {
        setBounceClickListener(this, durationMillis, onClick)?.addToDispose()
    }

    override fun setBounceClickListener(
        view: View,
        durationMillis: Long,
        onClick: (view: View) -> Unit
    ): Disposable? {
        return RxView.clicks(view)
            .filter { clicksFilter.filterAction(durationMillis) }
            .subscribe(
                { view.animateBounce { onClick(view) } },
                { showErrorSnack(it.message ?: getString(R.string.error_click_operation)) })
    }

    protected open fun attachSnackBarView(): View? = setUpSnackBar.attachSnackBarView()

    protected fun showErrorSnack(message: String) {
        showSnackListener.showSnack(attachSnackBarView(), message, R.color.colorRed)
    }

    protected fun showSuccessSnack(message: String) {
        showSnackListener.showSnack(attachSnackBarView(), message, R.color.colorGreen)
    }

    protected fun showShortErrorSnack(message: String) {
        showSnackListener.showSnack(
            attachSnackBarView(),
            message,
            R.color.colorRed,
            TSnackbar.LENGTH_SHORT
        )
    }

    protected fun showShortSuccessSnack(message: String) {
        showSnackListener.showSnack(
            attachSnackBarView(),
            message,
            R.color.colorGreen,
            TSnackbar.LENGTH_SHORT
        )
    }

    protected fun Disposable.addToDispose() {
        compositeDisposable.add(this)
    }

    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        if (nextAnim != 0) {
            val anim = AnimationUtils.loadAnimation(activity, nextAnim)
            anim.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {
                    baseActivity.onFragmentTransitionProgressChanged(TransitionAnimationStatus.STARTED)
                }

                override fun onAnimationRepeat(animation: Animation) {
                    baseActivity.onFragmentTransitionProgressChanged(TransitionAnimationStatus.REPEATED)
                }

                override fun onAnimationEnd(animation: Animation) {
                    baseActivity.onFragmentTransitionProgressChanged(TransitionAnimationStatus.ENDED)
                }
            })
            return anim
        } else baseActivity.onFragmentTransitionProgressChanged(TransitionAnimationStatus.ANIMATION_NULL)
        return super.onCreateAnimation(transit, enter, nextAnim)
    }

}