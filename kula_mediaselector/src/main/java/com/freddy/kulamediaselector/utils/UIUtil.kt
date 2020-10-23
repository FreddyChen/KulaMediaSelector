package com.freddy.kulamediaselector.utils

import android.graphics.Rect
import android.view.TouchDelegate
import android.view.View


object UIUtil {

    /**
     * 扩展点击区域的范围
     *
     * @param view       需要扩展的元素，此元素必需要有父级元素
     * @param expendSize 需要扩展的尺寸
     */
    fun expendTouchArea(view: View?, expendSize: Int) {
        if (view != null) {
            val parentView: View = view.parent as View
            parentView.post {
                val rect = Rect()
                view.getHitRect(rect) //如果太早执行本函数，会获取rect失败，因为此时UI界面尚未开始绘制，无法获得正确的坐标
                rect.left -= expendSize
                rect.top -= expendSize
                rect.right += expendSize
                rect.bottom += expendSize
                parentView.touchDelegate = TouchDelegate(rect, view)
            }
        }
    }
}