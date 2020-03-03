package com.wjx.android.wanandroidmvvm.base.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import com.google.android.material.circularreveal.CircularRevealCompat
import com.google.android.material.circularreveal.CircularRevealFrameLayout
import com.google.android.material.circularreveal.CircularRevealWidget
import com.google.android.material.circularreveal.coordinatorlayout.CircularRevealCoordinatorLayout
import com.wjx.android.wanandroidmvvm.ui.activity.ArticleDetailActivity
import java.lang.reflect.ParameterizedType
import java.util.*
import kotlin.math.hypot


/**
 * author：  HyZhan
 * created： 2018/10/11 16:04
 * desc：    工具类
 */
object Util {
    fun <T> getClass(t: Any): Class<T> {
        // 通过反射 获取父类泛型 (T) 对应 Class类
        return (t.javaClass.genericSuperclass as ParameterizedType)
                .actualTypeArguments[0]
                as Class<T>
    }

    fun randomColor(): Int {
        val random = Random()
        //0-190, 如果颜色值过大,就越接近白色,就看不清了,所以需要限定范围
        val red = random.nextInt(150)
        //0-190
        val green = random.nextInt(150)
        //0-190
        val blue = random.nextInt(150)
        //使用rgb混合生成一种新的颜色,Color.rgb生成的是一个int数
        return Color.rgb(red, green, blue)
    }

    fun px2sp(context: Context, pxValue: Float): Float {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return (pxValue / fontScale + 0.5f)
    }

    fun startWebView(
        context: Context,
        title: String?,
        url: String?
    ) {
        val intent = Intent()
        intent.setClass(context, ArticleDetailActivity::class.java)
        intent.putExtra("title", title)
        intent.putExtra("url", url)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    /**
     * 计算渐变颜色值 ARGB
     *
     * @param fraction
     * 变化比率 0~1
     * @param startValue
     * 初始色值
     * @param endValue
     * 结束色值
     * @return
     */
    fun evaluate(fraction: Float, startValue: Any, endValue: Int): Int {
        val startInt = startValue as Int
        val startA = startInt shr 24 and 0xff
        val startR = startInt shr 16 and 0xff
        val startG = startInt shr 8 and 0xff
        val startB = startInt and 0xff
        val endInt = endValue
        val endA = endInt shr 24 and 0xff
        val endR = endInt shr 16 and 0xff
        val endG = endInt shr 8 and 0xff
        val endB = endInt and 0xff
        return ((startA + (fraction * (endA - startA)).toInt() shl 24)
                or (startR + (fraction * (endR - startR)).toInt() shl 16)
                or (startG + (fraction * (endG - startG)).toInt() shl 8)
                or (startB + (fraction * (endB - startB)).toInt()))
    }


    fun Activity.setReveal(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            val container = window.decorView as FrameLayout
            val rootLayout = container.getChildAt(0)
            container.removeView(rootLayout)
            val layout = CircularRevealFrameLayout(this)
            layout.addView(rootLayout, ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT)
            container.addView(layout)
            container.onPreDrawLayout {
                circularReveal(layout)
            }
        }
    }

    inline fun <T : View> T.onPreDrawLayout(crossinline  onLayout: T.()->Unit){
        if (viewTreeObserver.isAlive) {
            viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {

                override fun onPreDraw(): Boolean {
                    onLayout()
                    viewTreeObserver.removeOnPreDrawListener(this)
                    return false
                }
            })
        }
    }

    fun circularReveal(rootLayout : CircularRevealFrameLayout) {
        val centerX = rootLayout.width.toFloat() / 2
        val centerY = rootLayout.height.toFloat() / 2
        val finalRadius = hypot(
            centerX.coerceAtLeast(rootLayout.width  - centerX),
            centerY.coerceAtLeast(rootLayout.height - centerY)
        )

        rootLayout.revealInfo = CircularRevealWidget.RevealInfo(centerX, centerY, 0f)
        val circularReveal = CircularRevealCompat.createCircularReveal(rootLayout, centerX, centerY, finalRadius)
        circularReveal.duration = 500
        circularReveal.interpolator = LinearInterpolator()
        circularReveal.startDelay = 1
        circularReveal.start()
    }

    fun circularFinishReveal(rootLayout: View) {
        val centerX = rootLayout.width.toFloat() /2
        val centerY = rootLayout.height.toFloat() /2
        val finalRadius = hypot(
            centerX.coerceAtLeast(rootLayout.width - centerX),
            centerY.coerceAtLeast(rootLayout.height - centerY)
        )
        val circularReveal = ViewAnimationUtils.createCircularReveal(
            rootLayout,
            (rootLayout.width * 0.5).toInt(),
            (rootLayout.height * 0.5).toInt(), finalRadius,0f
        )
        circularReveal.duration = 500
        circularReveal.interpolator = LinearInterpolator()
        rootLayout.postDelayed({
            rootLayout.alpha = 0.8f
        },250)
        circularReveal.start()
    }

}