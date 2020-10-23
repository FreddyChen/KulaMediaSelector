package com.freddy.kulamediaselector.ui.widget

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView
import com.freddy.kulamediaselector.R
import kotlin.properties.Delegates

class CMaxHeightRecyclerView : RecyclerView {

    var maxHeight: Int by Delegates.notNull()

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        val array = context.obtainStyledAttributes(attrs, R.styleable.CMaxHeightRecyclerView, defStyleAttr, 0)
        maxHeight = array.getDimensionPixelSize(R.styleable.CMaxHeightRecyclerView_kula_media_selector_chrv_maxHeight, 0)
        array.recycle()
    }

    override fun onMeasure(widthSpec: Int, heightSpec: Int) {
        var heightMeasureSpec  = heightSpec
        if(maxHeight > 0) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.AT_MOST)
        }
        super.onMeasure(widthSpec, heightMeasureSpec)
    }
}