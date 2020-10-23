package com.freddy.kulamediaselector.listener

import com.freddy.kulamediaselector.config.MediaFolder

interface OnMediaScannerListener {
    fun onResultMediaFolders(mediaFolders: ArrayList<MediaFolder>)
}