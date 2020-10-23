package com.freddy.kulamediaselector.ui

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.freddy.kulamediaselector.R
import com.freddy.kulamediaselector.config.MediaFolder
import com.freddy.kulamediaselector.config.MediaSelectorOptions
import com.freddy.kulamediaselector.ui.widget.CItemDecoration
import com.freddy.kulamediaselector.ui.widget.CMaxHeightRecyclerView
import com.freddy.kulamediaselector.utils.DensityUtil

class MediaFolderSelectPopupWindow(
    private val context: Context,
    private val mediaFolderList: ArrayList<MediaFolder>,
    private val mediaSelectorOptions: MediaSelectorOptions
) : PopupWindow() {

    init {
        contentView = LayoutInflater.from(context)
            .inflate(R.layout.kula_mediaselector_popupwindow_media_folder_select, null)
        width = ViewGroup.LayoutParams.MATCH_PARENT
        height = ViewGroup.LayoutParams.WRAP_CONTENT
        isOutsideTouchable = true
        isFocusable = true
        initRecyclerView()
    }

    private fun initRecyclerView() {
        val recyclerView = contentView.findViewById<CMaxHeightRecyclerView>(R.id.recycler_view)
        recyclerView.setHasFixedSize(false)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.maxHeight = DensityUtil.dip2px(context, (80.0f + 0.35f) * mediaSelectorOptions.maxVisibilityFolderCount)
        recyclerView.addItemDecoration(
            CItemDecoration(
                dividerHeight = DensityUtil.dip2px(
                    context,
                    0.35f
                ),
                leftMargin = DensityUtil.dip2px(context, 24.0f),
            )
        )
        val adapter = MediaFolderListAdapter(context, mediaFolderList)
        adapter.setOnItemClickListener { v, mediaFolder ->
            dismiss()
            onMediaFolderSelectedListener?.invoke(v, mediaFolder)
        }
        recyclerView.adapter = adapter
    }

    override fun showAsDropDown(anchor: View?) {
        contentView.post {
            contentView.translationY = -contentView.height.toFloat()
            val animator = ObjectAnimator.ofFloat(
                contentView,
                "translationY",
                -contentView.height.toFloat(),
                0.0f
            )
            animator.duration = 350
            animator.interpolator = FastOutSlowInInterpolator()
            animator.start()
        }
        super.showAsDropDown(anchor)
    }

    override fun dismiss() {
        val animator =
            ObjectAnimator.ofFloat(contentView, "translationY", 0.0f, -contentView.height.toFloat())
        animator.duration = 250
        animator.interpolator = FastOutSlowInInterpolator()
        animator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(p0: Animator?) {
            }

            override fun onAnimationEnd(p0: Animator?) {
                super@MediaFolderSelectPopupWindow.dismiss()
            }

            override fun onAnimationCancel(p0: Animator?) {
            }

            override fun onAnimationRepeat(p0: Animator?) {
            }
        })
        animator.start()
    }

    private var onMediaFolderSelectedListener: ((v: View, mediaFolder: MediaFolder) -> Unit?)? =
        null

    fun setOnMediaFolderSelectedListener(onMediaFolderSelectedListener: ((v: View, mediaFolder: MediaFolder) -> Unit?)?) {
        this.onMediaFolderSelectedListener = onMediaFolderSelectedListener
    }
}