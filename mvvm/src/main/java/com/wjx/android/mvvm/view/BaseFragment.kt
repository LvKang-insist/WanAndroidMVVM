package com.wjx.android.mvvm.view

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.kingja.loadsir.core.LoadService
import com.wjx.android.mvvm.R
import com.wjx.android.mvvm.viewmodel.BaseViewModel
import com.wjx.android.wanandroidmvvm.common.utils.CommonUtil

/**
 * Created with Android Studio.
 * Description:
 * @author: Wangjianxian
 * @CreateDate: 2020/6/8 8:28
 */
abstract class BaseFragment<VM : BaseViewModel<*>, DB : ViewDataBinding> : Fragment() {
    protected lateinit var mViewModel: VM

    protected lateinit var mDataBinding: DB

    protected lateinit var loadService: LoadService<*>

    open fun initView() {}

    open fun initData() {}

    open fun reLoad() = initData()

    abstract fun getLayoutId(): Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mViewModel = ViewModelProvider(this).get(CommonUtil.getClass(this))
        mDataBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        mDataBinding.lifecycleOwner = this
        return mDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
        initData()
        initStatusBarColor()
    }

    private fun initStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            requireActivity().window.statusBarColor = ContextCompat.getColor(
                requireActivity(),
                R.color.always_white_text
            )
        }
        if (ColorUtils.calculateLuminance(requireContext().getColor(R.color.always_white_text)) >= 0.5) {
            // 设置状态栏中字体的颜色为黑色
            requireActivity().window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            // 跟随系统
            requireActivity().window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        }
    }
}