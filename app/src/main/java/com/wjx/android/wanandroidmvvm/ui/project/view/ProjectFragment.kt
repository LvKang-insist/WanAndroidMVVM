package com.wjx.android.wanandroidmvvm.ui.project.view

import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.wjx.android.wanandroidmvvm.R
import com.wjx.android.wanandroidmvvm.base.view.BaseLifeCycleFragment
import com.wjx.android.wanandroidmvvm.common.utils.ChangeThemeEvent
import com.wjx.android.wanandroidmvvm.common.utils.ColorUtil
import com.wjx.android.wanandroidmvvm.ui.project.adapter.ProjectTabAdapter
import com.wjx.android.wanandroidmvvm.ui.project.data.ProjectTabResponse
import com.wjx.android.wanandroidmvvm.ui.project.viewmodel.ProjectViewModel
import kotlinx.android.synthetic.main.fragment_project.*
import org.greenrobot.eventbus.Subscribe

/**
 * Created with Android Studio.
 * Description:
 * @author: Wangjianxian
 * @date: 2020/02/22
 * Time: 20:00
 */
class ProjectFragment : BaseLifeCycleFragment<ProjectViewModel>() {

    companion object {
        fun getInstance(): ProjectFragment? {
            return ProjectFragment()
        }
    }
    override fun getLayoutId(): Int = R.layout.fragment_project

    override fun initView() {
        super.initView()
        initColor()
    }

    private fun initColor() {
        project_tab.dividerColor = ColorUtil.getColor(activity!!)
        project_tab.indicatorColor = ColorUtil.getColor(activity!!)
    }

    override fun initData() {
        super.initData()
        mViewModel.loadProjectTab()
    }

    override fun initDataObserver() {
        mViewModel.mProjectTabData.observe(this, Observer { response ->
            response?.let {
                initProjectArticleFragment(it)
            }
        })
    }

    private fun initProjectArticleFragment(dataList : List<ProjectTabResponse>) {
        val tabs = arrayListOf<String>()
        val fragments = arrayListOf<Fragment>()
        for (data in dataList) {
            tabs.add(data.name)
            fragments.add(ProjectArticleFragment.newInstance(data.id))
        }
        project_viewpager.adapter = ProjectTabAdapter(childFragmentManager, tabs, fragments)
        project_tab.setViewPager(project_viewpager)
    }

    @Subscribe
    fun settingEvent(event: ChangeThemeEvent) {
        initColor()
    }
}