package com.learn.minesweeper.utils

import android.content.res.Resources

object ScreenUtils {
//    private fun getScreenHeight(): Int {
//        val displayMetrics = DisplayMetrics()
//        windowManager.defaultDisplay.getMetrics(displayMetrics)
//
//        return displayMetrics.heightPixels
//    }

    fun getScreenHeight(): Int {
        return Resources.getSystem().displayMetrics.heightPixels
    }
}