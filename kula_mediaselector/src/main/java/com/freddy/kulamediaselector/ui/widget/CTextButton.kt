package com.freddy.kulamediaselector.ui.widget

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import android.util.AttributeSet
import android.util.Log
import androidx.appcompat.widget.AppCompatTextView
import com.freddy.kulamediaselector.R
import kotlin.properties.Delegates

class CTextButton : AppCompatTextView {

    private var normalBackgroundColor by Delegates.notNull<Int>()
    private var pressedBackgroundColor by Delegates.notNull<Int>()
    private var disabledBackgroundColor by Delegates.notNull<Int>()
    private var cornerRadius by Delegates.notNull<Int>()

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        val array = context.obtainStyledAttributes(attrs, R.styleable.CTextButton, defStyleAttr, 0)
        normalBackgroundColor = array.getColor(R.styleable.CTextButton_kula_media_selector_ctb_normalBackgroundColor, Color.parseColor("#000000"))
        pressedBackgroundColor = array.getColor(R.styleable.CTextButton_kula_media_selector_ctb_pressedBackgroundColor, Color.parseColor("#000000"))
        disabledBackgroundColor = array.getColor(R.styleable.CTextButton_kula_media_selector_ctb_disabledBackgroundColor, Color.parseColor("#000000"))
        cornerRadius = array.getDimensionPixelOffset(R.styleable.CTextButton_kula_media_selector_ctb_cornerRadius, 24)
        array.recycle()

        init()
    }

    private fun init() {
        updateBackgroundDrawable()
    }

    private fun updateBackgroundDrawable() {
        val normalBackgroundDrawable = GradientDrawable()
        normalBackgroundDrawable.cornerRadius = cornerRadius.toFloat()
        normalBackgroundDrawable.setColor(normalBackgroundColor)

        val pressedBackgroundDrawable = GradientDrawable()
        pressedBackgroundDrawable.cornerRadius = cornerRadius.toFloat()
        pressedBackgroundDrawable.setColor(pressedBackgroundColor)

        val disabledBackgroundDrawable = GradientDrawable()
        disabledBackgroundDrawable.cornerRadius = cornerRadius.toFloat()
        disabledBackgroundDrawable.setColor(disabledBackgroundColor)

        val stateListDrawable = StateListDrawable()
        stateListDrawable.addState(intArrayOf(android.R.attr.state_pressed), pressedBackgroundDrawable)
        stateListDrawable.addState(intArrayOf(-android.R.attr.state_enabled), disabledBackgroundDrawable)
        stateListDrawable.addState(intArrayOf(), normalBackgroundDrawable)

        setBackgroundDrawable(stateListDrawable)
    }

    fun setBackgroundColor(normalBackgroundColor: Int? = null, pressedBackgroundColor: Int? = null, disabledBackgroundColor: Int? = null) {
        normalBackgroundColor?.let {
            this@CTextButton.normalBackgroundColor = it
        }
        pressedBackgroundColor?.let {
            this@CTextButton.pressedBackgroundColor = it
        }
        disabledBackgroundColor?.let {
            this@CTextButton.disabledBackgroundColor = it
        }

        updateBackgroundDrawable()
    }
}