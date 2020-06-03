package com.learn.minesweeper

import android.content.Context
import android.graphics.Typeface
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.learn.minesweeper.dto.Content

class Cell(context: Context) : androidx.appcompat.widget.AppCompatTextView(context) {
    init {
        typeface = ResourcesCompat.getFont(context, R.font.roboto)
        setTypeface(typeface, Typeface.BOLD)
    }
}