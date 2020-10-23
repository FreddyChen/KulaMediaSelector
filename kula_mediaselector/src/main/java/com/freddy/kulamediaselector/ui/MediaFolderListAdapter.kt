package com.freddy.kulamediaselector.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.freddy.kulamediaselector.KulaMediaSelectorHelper
import com.freddy.kulamediaselector.R
import com.freddy.kulamediaselector.config.MediaFolder
import com.google.android.material.imageview.ShapeableImageView

class MediaFolderListAdapter(
    private val context: Context,
    private val mediaFolderList: ArrayList<MediaFolder>
) : RecyclerView.Adapter<MediaFolderListAdapter.CViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CViewHolder {
        return CViewHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.kula_mediaselector_item_media_folder, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CViewHolder, position: Int) {
        val mediaFolder = mediaFolderList[position]
        mediaFolder.coverUri?.let {
            KulaMediaSelectorHelper.INSTANCE.imageLoaderEnginee?.apply {
                loadImage(context, it, holder.coverImageView)
            }
        }
        holder.folderNameTextView.text = mediaFolder.name?.plus("（${mediaFolder.medias.size}）")
        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(it, mediaFolder)
        }
    }

    override fun getItemCount(): Int {
        return mediaFolderList.size
    }

    private var onItemClickListener: ((v: View, mediaFolder: MediaFolder) -> Unit?)? = null
    fun setOnItemClickListener(onItemClickListener: ((v: View, mediaFolder: MediaFolder) -> Unit?)?) {
        this.onItemClickListener = onItemClickListener
    }

    class CViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val coverImageView: ShapeableImageView = itemView.findViewById(R.id.iv_cover)
        val folderNameTextView: TextView = itemView.findViewById(R.id.tv_folder_name)
    }
}