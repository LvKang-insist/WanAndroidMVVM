package com.wjx.android.wanandroidmvvm.base.view


import android.text.TextUtils
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kingja.loadsir.callback.SuccessCallback
import com.wjx.android.wanandroidmvvm.base.viewmodel.BaseViewModel
import com.wjx.android.wanandroidmvvm.common.callback.EmptyCallBack
import com.wjx.android.wanandroidmvvm.common.callback.ErrorCallBack
import com.wjx.android.wanandroidmvvm.common.callback.LoadingCallBack
import com.wjx.android.wanandroidmvvm.common.state.State
import com.wjx.android.wanandroidmvvm.common.state.StateType
import com.wjx.android.wanandroidmvvm.common.utils.CommonUtil
import org.jetbrains.anko.toast

/**
 * Created with Android Studio.
 * Description:
 * @author: Wangjianxian
 * @date: 2020/02/22
 * Time: 16:30
 */
abstract class BaseLifeCycleActivity<VM : BaseViewModel<*>> : BaseActivity() {
    protected lateinit var mViewModel: VM

    override fun initView() {
        showLoading()

        mViewModel = ViewModelProvider(this).get(CommonUtil.getClass(this))

        mViewModel.loadState.observe(this, observer)

        // 初始化View的Observer
        initDataObserver()
    }

    abstract fun initDataObserver()


    open fun showLoading() {
        loadService.showCallback(LoadingCallBack::class.java)
    }

    open fun showSuccess() {
        loadService.showCallback(SuccessCallback::class.java)
    }

    open fun showError(msg: String) {
        if (!TextUtils.isEmpty(msg)) {
            toast(msg)
        }
        loadService.showCallback(ErrorCallBack::class.java)
    }

    open fun showTip(msg: String) {
        if (!TextUtils.isEmpty(msg)) {
            toast(msg)
        }
        loadService.showCallback(SuccessCallback::class.java)
    }

    open fun showEmpty() {
        loadService.showCallback(EmptyCallBack::class.java)
    }

    /**
     * 分发应用状态
     */
    private val observer by lazy {
        Observer<State> {
            it?.let {
                when (it.code) {
                    StateType.SUCCESS -> showSuccess()
                    StateType.LOADING -> showLoading()
                    StateType.ERROR -> showTip(it.message)
                    StateType.NETWORK_ERROR -> showError("网络出现问题啦")
                    StateType.TIP -> showTip(it.message)
                    StateType.EMPTY -> showEmpty()
                }
            }
        }
    }
}