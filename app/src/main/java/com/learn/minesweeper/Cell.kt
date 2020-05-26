package com.learn.minesweeper

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.annotation.Dimension
import androidx.core.content.res.ResourcesCompat
import com.learn.minesweeper.dto.Content

class Cell(context: Context, var content: Content) : androidx.appcompat.widget.AppCompatTextView(context) {
    init {
        typeface = ResourcesCompat.getFont(context, R.font.roboto)
        setTypeface(typeface, Typeface.BOLD)
//        setTextSize(Dimension.DP, 30F)
    }
}