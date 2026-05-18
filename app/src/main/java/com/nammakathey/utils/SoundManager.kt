package com.nammakathey.utils

import android.content.Context
import android.media.MediaPlayer

object SoundManager {
    fun playSound(context: Context, resId: Int) {
        try {
            val mediaPlayer = MediaPlayer.create(context, resId)
            mediaPlayer.setOnCompletionListener { it.release() }
            mediaPlayer.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
