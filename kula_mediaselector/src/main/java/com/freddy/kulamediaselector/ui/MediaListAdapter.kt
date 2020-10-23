package com.freddy.kulamediaselector.ui

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.freddy.kulamediaselector.KulaMediaSelectorHelper
import com.freddy.kulamediaselector.R
import com.freddy.kulamediaselector.config.Media
import com.freddy.kulamediaselector.config.MediaSelectorOptions
import com.freddy.kulamediaselector.config.MediaType
import com.freddy.kulamediaselector.config.SelectType
import com.freddy.kulamediaselector.utils.DateUtil
import com.freddy.kulamediaselector.utils.DensityUtil
import com.freddy.kulamediaselector.utils.UIUtil
import kotlin.properties.Delegates

class MediaListAdapter(
    private val context: Context,
    private val mediaList: ArrayList<Media>,
    private val mediaSelectorOptions: MediaSelectorOptions
) : RecyclerView.Adapter<MediaListAdapter.CViewHolder>() {

    private var itemWidth: Int by Delegates.notNull()
    private var selectedMediaList = arrayListOf<Media>()

    companion object {
        private const val PAYLOAD_CORNER_MARKER_TEXT_VIEW_ADD = 101
        private const val PAYLOAD_CORNER_MARKER_TEXT_VIEW_REMOVE = 102
        private const val PAYLOAD_CORNER_MARKER_TEXT_VIEW_SORT = 103
    }

    init {
        itemWidth = (DensityUtil.getScreenWidth(context) - DensityUtil.dip2px(
            context,
            2.0f
        ) * mediaSelectorOptions.spanCount - DensityUtil.dip2px(
            context,
            2.0f
        ) * mediaSelectorOptions.spanCount) / mediaSelectorOptions.spanCount
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CViewHolder {
        return CViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.kula_mediaselector_item_media,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
        } else {
            when (payloads[0] as Int) {
                PAYLOAD_CORNER_MARKER_TEXT_VIEW_ADD -> {
                    holder.cornerMarkerTextView.isSelected = true
                    holder.cornerMarkerTextView.text = selectedMediaList.size.toString()
                    startSelectedStatusAnimator(holder.cornerMarkerTextView)
                }

                PAYLOAD_CORNER_MARKER_TEXT_VIEW_REMOVE -> {
                    holder.cornerMarkerTextView.isSelected = false
                    holder.cornerMarkerTextView.text = null
                }

                PAYLOAD_CORNER_MARKER_TEXT_VIEW_SORT -> {
                    holder.cornerMarkerTextView.text =
                        (selectedMediaList.indexOf(mediaList[position]) + 1).toString()
                }
            }
        }
    }

    override fun onBindViewHolder(holder: CViewHolder, position: Int) {
        val media = mediaList[position]
        media.thumbnail?.let {
            KulaMediaSelectorHelper.INSTANCE.imageLoaderEnginee?.apply {
                loadImage(context, it, holder.mediaImageView)
            }
        }

        holder.videoTagLayout.visibility = View.GONE
        if (media.mediaType == MediaType.VIDEO) {
            holder.videoTagLayout.visibility = View.VISIBLE
            holder.videoDurationTextView.text = DateUtil.formatDate(
                media.duration,
                DateUtil.TIME_MM_SS
            )
        }

        holder.cornerMarkerTextView.visibility = View.GONE
        if (mediaSelectorOptions.selectType == SelectType.MULTIPLE) {
            holder.cornerMarkerTextView.visibility = View.VISIBLE
            UIUtil.expendTouchArea(holder.cornerMarkerTextView, 48)
            val isSelected = selectedMediaList.contains(media)
            if (isSelected) {
                holder.cornerMarkerTextView.text = (selectedMediaList.indexOf(media) + 1).toString()
            } else {
                holder.cornerMarkerTextView.text = null
            }
            holder.cornerMarkerTextView.isSelected = isSelected
            holder.cornerMarkerTextView.setOnClickListener {
                if(!it.isSelected) {
                    if(selectedMediaList.size == mediaSelectorOptions.maxSelectCount) {
                        Toast.makeText(context, "最多只能选择${mediaSelectorOptions.maxSelectCount}个文件", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                    selectedMediaList.add(media)
                    notifyItemChanged(position, PAYLOAD_CORNER_MARKER_TEXT_VIEW_ADD)
                }else {
                    selectedMediaList.remove(media)
                    notifyItemChanged(position, PAYLOAD_CORNER_MARKER_TEXT_VIEW_REMOVE)
                    for(selectedMedia in selectedMediaList) {
                        notifyItemChanged(
                            mediaList.indexOf(selectedMedia),
                            PAYLOAD_CORNER_MARKER_TEXT_VIEW_SORT
                        )
                    }
                }

                onMediaListSelectedListener?.invoke(selectedMediaList)
            }
        }

        val layoutParams = holder.mediaImageView.layoutParams
        layoutParams.width = itemWidth
        layoutParams.height = itemWidth
        holder.mediaImageView.layoutParams = layoutParams

        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(it, media)
        }
    }

    private fun startSelectedStatusAnimator(cornerMarkerTextView: View) {
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(
            ObjectAnimator.ofFloat(cornerMarkerTextView, "scaleX", 0.5f,
                0.6f,
                0.7f,
                0.8f,
                0.9f,
                1.0f,
                1.1f,
                1.2f,
                1.3f,
                1.35f,
                1.30f,
                1.25f,
                1.2f,
                1.15f,
                1.1f,
                1.0f),
            ObjectAnimator.ofFloat(cornerMarkerTextView, "scaleY", 0.5f,
                0.6f,
                0.7f,
                0.8f,
                0.9f,
                1.0f,
                1.1f,
                1.2f,
                1.3f,
                1.35f,
                1.30f,
                1.25f,
                1.2f,
                1.15f,
                1.1f,
                1.0f)
        )
        animatorSet.duration = 150
        animatorSet.start()
    }

    override fun getItemCount(): Int {
        return mediaList.size
    }

    private var onItemClickListener: ((v: View, media: Media) -> Unit?)? = null
    fun setOnItemClickListener(onItemClickListener: ((v: View, media: Media) -> Unit?)?) {
        this.onItemClickListener = onItemClickListener
    }

    private var onMediaListSelectedListener: ((selectedMediaList: ArrayList<Media>) -> Unit?)? = null
    fun setOnMediaListSelectedListener(onMediaListSelectedListener: ((selectedMediaList: ArrayList<Media>) -> Unit?)) {
        this.onMediaListSelectedListener = onMediaListSelectedListener
    }

    class CViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mediaImageView: ImageView = itemView.findViewById(R.id.iv_media)
        val videoTagLayout: ViewGroup = itemView.findViewById(R.id.layout_video_tag)
        val cornerMarkerTextView: TextView = itemView.findViewById(R.id.tv_corner_marker)
        val videoDurationTextView: TextView = itemView.findViewById(R.id.tv_video_duration)
    }
}