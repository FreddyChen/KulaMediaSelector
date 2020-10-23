package com.freddy.kulamediaselector.ui

import android.Manifest
import android.animation.Animator
import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.recyclerview.widget.GridLayoutManager
import com.alibaba.fastjson.JSON
import com.freddy.kulamediaselector.R
import com.freddy.kulamediaselector.config.*
import com.freddy.kulamediaselector.listener.OnMediaScannerListener
import com.freddy.kulamediaselector.media.MediaScanner
import com.freddy.kulamediaselector.ui.widget.RecyclerViewSpacesItemDecoration
import com.freddy.kulamediaselector.utils.DensityUtil
import com.freddy.kulamediaselector.utils.UIUtil
import com.jaeger.library.StatusBarUtil
import kotlinx.android.synthetic.main.kula_mediaselector_activity_album.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

class AlbumActivity : AppCompatActivity() {

    private lateinit var mediaSelectorOptions: MediaSelectorOptions
    private var isArrowAnimatorFinished = true
    private var isShowingMediaFolderSelectPopupWindow = false
    private var mediaFolderSelectPopupWindow: MediaFolderSelectPopupWindow? = null
    private var mediaListAdapter: MediaListAdapter by Delegates.notNull()
    private var mediaList = arrayListOf<Media>()

    companion object {
        private const val TAG = "KulaMediaSelectorAlbumActivity"
        private val NECESSARY_PERMISSIONS = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.navigationBarColor = Color.parseColor("#000000")
        setContentView(R.layout.kula_mediaselector_activity_album)
        initData()
        initWidget()
        setListeners()
        checkPermissions()

        StatusBarUtil.setColor(this, Color.parseColor("#000000"))
    }

    @SuppressLint("LongLogTag")
    private fun initData() {
        mediaSelectorOptions =
            intent.getSerializableExtra(MediaSelectorOptions.KEY_MEDIA_SELECTOR_OPTIONS) as MediaSelectorOptions
        Log.d(TAG, "mediaSelectorOptions = $mediaSelectorOptions")
    }

    private fun initWidget() {
        UIUtil.expendTouchArea(btn_close, 72)
        mediaSelectorOptions.apply {
            topBarColor?.let {
                layout_top_bar.setBackgroundColor(it)
            }
            bottomBarColor?.let {
                layout_bottom_bar.setBackgroundColor(it)
            }
            titleBackgroudColor?.let {
                val gradientDrawable = GradientDrawable()
                gradientDrawable.cornerRadius =
                    DensityUtil.dip2px(applicationContext, 24.0f).toFloat()
                gradientDrawable.setColor(it)
                layout_select_folder.setBackgroundDrawable(gradientDrawable)
            }
            titleTextSize?.let {
                tv_folder_name.setTextSize(TypedValue.COMPLEX_UNIT_SP, it)
            }
            titleTextColor?.let {
                tv_folder_name.setTextColor(it)
            }
            confirmText?.let {
                btn_confirm.text = it
            }
            confirmTextSize?.let {
                btn_confirm.setTextSize(TypedValue.COMPLEX_UNIT_SP, it)
            }
            confirmTextColor?.let {
                btn_confirm.setTextColor(it)
            }
            btn_confirm.isEnabled = !selectedMediaList.isNullOrEmpty()
            if (selectedMediaList.isNullOrEmpty()) {
                refreshConfirmTextView(0)
            } else {
                selectedMediaList?.size?.let { refreshConfirmTextView(it) }
            }
            if (confirmNormalBackgroundColor != null && confirmPressedBackgroundColor != null && confirmDisabledBackgroundColor != null) {
                btn_confirm.setBackgroundColor(
                    confirmNormalBackgroundColor,
                    confirmPressedBackgroundColor,
                    confirmDisabledBackgroundColor
                )
            }
            previewText?.let {
                btn_preview.text = it
            }
            previewTextSize?.let {
                btn_preview.setTextSize(TypedValue.COMPLEX_UNIT_SP, it)
            }
            previewTextColor?.let {
                btn_preview.setTextColor(it)
            }
            if (previewNormalBackgroundColor != null && previewPressedBackgroundColor != null && previewDisabledBackgroundColor != null) {
                btn_preview.setBackgroundColor(
                    previewNormalBackgroundColor,
                    previewPressedBackgroundColor,
                    previewDisabledBackgroundColor
                )
            }
            layout_bottom_bar.visibility = if(selectType == SelectType.MULTIPLE) View.VISIBLE else View.GONE
            if(selectType == SelectType.MULTIPLE) {
                if(selectedMediaList.isNullOrEmpty()) {
                    refreshPreviewTextView(0)
                }else {
                    selectedMediaList?.size?.let { refreshPreviewTextView(it) }
                }
            }
        }

        initRecyclerView()
    }

    private fun refreshConfirmTextView(selectedCount: Int) {
        mediaSelectorOptions.confirmText?.let {
            if (selectedCount == 0) {
                btn_confirm.isEnabled = false
                btn_confirm.text = it
            } else {
                btn_confirm.isEnabled = true
                btn_confirm.text = it.plus("（${selectedCount}/${mediaSelectorOptions.maxSelectCount}）")
            }
        }
    }

    private fun refreshPreviewTextView(selectedCount: Int) {
        mediaSelectorOptions.confirmText?.let {
            btn_preview.isEnabled = selectedCount > 0
        }
    }

    private fun initRecyclerView() {
        recycler_view.setHasFixedSize(true)
        recycler_view.itemAnimator = null
        recycler_view.layoutManager = GridLayoutManager(this, mediaSelectorOptions.spanCount)
        val decorationMap = HashMap<String, Int>()
        decorationMap[RecyclerViewSpacesItemDecoration.LEFT_DECORATION] =
            DensityUtil.dip2px(applicationContext, 2.0f)
        decorationMap[RecyclerViewSpacesItemDecoration.TOP_DECORATION] =
            DensityUtil.dip2px(applicationContext, 2.0f)
        decorationMap[RecyclerViewSpacesItemDecoration.RIGHT_DECORATION] =
            DensityUtil.dip2px(applicationContext, 2.0f)
        recycler_view.addItemDecoration(RecyclerViewSpacesItemDecoration(decorationMap))
        mediaListAdapter = MediaListAdapter(this, mediaList, mediaSelectorOptions)
        recycler_view.adapter = mediaListAdapter
    }

    private fun refreshMediaListAdapter(mediaList: ArrayList<Media>) {
        this.mediaList.clear()
        this.mediaList.addAll(mediaList)
        mediaListAdapter.notifyDataSetChanged()
    }

    private fun setListeners() {
        layout_select_folder.setOnClickListener {
            if (!isArrowAnimatorFinished) return@setOnClickListener
            showMediaFolderSelectPopupWindow()
        }
        btn_close.setOnClickListener {
        }
        btn_confirm.setOnClickListener {
        }
        btn_preview.setOnClickListener {
        }
        mediaListAdapter.setOnMediaListSelectedListener {
            refreshConfirmTextView(it.size)
            refreshPreviewTextView(it.size)
        }
    }

    private fun showMediaFolderSelectPopupWindow() {
        mediaFolderSelectPopupWindow?.apply {
            if (!isShowingMediaFolderSelectPopupWindow) {
                showAsDropDown(layout_top_bar)
                isShowingMediaFolderSelectPopupWindow = true
                startArrowAnimator()
                updateRecyclerViewBackgroundColor(false)
            }
        }
    }

    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(
                applicationContext,
                NECESSARY_PERMISSIONS[0]
            ) != PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(
                applicationContext,
                NECESSARY_PERMISSIONS[0]
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, NECESSARY_PERMISSIONS, 101)
        } else {
            scanMedias()
        }
    }

    private fun scanMedias() {
        val mediaScanner = MediaScanner(applicationContext, mediaSelectorOptions)
        mediaScanner.scanMedias(MediaType.ALL, object : OnMediaScannerListener {
            override fun onResultMediaFolders(mediaFolders: ArrayList<MediaFolder>) {
                Log.d("FreddyChen", JSON.toJSONString(mediaFolders))
                GlobalScope.launch(Dispatchers.Main) {
                    if (mediaFolders.isEmpty()) {
                        return@launch
                    }

                    val firstMediaFolder = mediaFolders[0]
                    updateFolderNameTextView(firstMediaFolder.name)
                    refreshMediaListAdapter(firstMediaFolder.medias)

                    mediaFolderSelectPopupWindow =
                        MediaFolderSelectPopupWindow(
                            this@AlbumActivity,
                            mediaFolders,
                            mediaSelectorOptions
                        )
                    mediaFolderSelectPopupWindow?.setOnDismissListener {
                        isShowingMediaFolderSelectPopupWindow = false
                        startArrowAnimator()
                        updateRecyclerViewBackgroundColor(true)
                    }
                    mediaFolderSelectPopupWindow?.setOnMediaFolderSelectedListener { _, mediaFolder ->
                        updateFolderNameTextView(mediaFolder.name)
                        refreshMediaListAdapter(mediaFolder.medias)
                    }
                }
                Log.d("AlbumActivity", "mediaFolders = $mediaFolders, size = ${mediaFolders.size}")
            }
        })
    }

    private fun updateFolderNameTextView(folderName: String?) {
        tv_folder_name.text = folderName
    }

    private fun startArrowAnimator() {
        val animator = if (!isShowingMediaFolderSelectPopupWindow) {
            ObjectAnimator.ofFloat(iv_arrow, "rotation", 180.0f, 360.0f)
        } else {
            ObjectAnimator.ofFloat(iv_arrow, "rotation", 0.0f, 180.0f)
        }
        animator.duration = 350
        animator.interpolator = LinearInterpolator()
        animator.addListener(arrowAnimatorStateListener)
        animator.start()
    }

    private val arrowAnimatorStateListener = object : Animator.AnimatorListener {
        override fun onAnimationStart(p0: Animator?) {
            isArrowAnimatorFinished = false
        }

        override fun onAnimationEnd(p0: Animator?) {
            isArrowAnimatorFinished = true
        }

        override fun onAnimationCancel(p0: Animator?) {
        }

        override fun onAnimationRepeat(p0: Animator?) {
        }
    }

    private fun updateRecyclerViewBackgroundColor(transparent: Boolean) {
        val animator = if (!transparent) {
            ValueAnimator.ofInt(Color.TRANSPARENT, 0X9F000000.toInt())
        } else {
            ValueAnimator.ofInt(0X9F000000.toInt(), Color.TRANSPARENT)
        }
        animator.setEvaluator(ArgbEvaluator())
        animator.addUpdateListener { animation ->
            val value = animation.animatedValue as Int
            view_mask.setBackgroundColor(value)
        }
        animator.interpolator = FastOutSlowInInterpolator()
        animator.duration = 350
        animator.start()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            101 -> {
                if (grantResults.size != NECESSARY_PERMISSIONS.size || grantResults[0] != PackageManager.PERMISSION_GRANTED || grantResults[1] != PackageManager.PERMISSION_GRANTED) {
                    finish()
                    return
                }
                scanMedias()
            }
        }
    }
}