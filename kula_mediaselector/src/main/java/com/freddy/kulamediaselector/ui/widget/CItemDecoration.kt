package com.freddy.kulamediaselector.ui.widget

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlin.properties.Delegates

class CItemDecoration(
    private var dividerHeight: Int = 1,
    private var leftMargin: Int = 0,
    private var rightMargin: Int = 0,
    dividerColor: Int = Color.GRAY
) : RecyclerView.ItemDecoration() {

    private var paint: Paint by Delegates.notNull()

    init {
        paint = Paint()
        paint.isAntiAlias = true
        paint.color = dividerColor
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        if(parent.getChildAdapterPosition(view) != 0) {
            outRect.top = dividerHeight
        }
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        val childCount = parent.childCount
        for(i in 0 until childCount) {
            val view = parent.getChildAt(i)
            val index = parent.getChildAdapterPosition(view)
            if(index == 0) continue
            val dividerLeftMargin = parent.paddingLeft + leftMargin
            val dividerTopMargin = view.top - dividerHeight
            val dividerRightMargin = parent.width - parent.paddingRight - rightMargin
            val dividerBottomMargin = view.top
            c.drawRect(dividerLeftMargin.toFloat(), dividerTopMargin.toFloat(), dividerRightMargin.toFloat(), dividerBottomMargin.toFloat(), paint)
        }
    }
}