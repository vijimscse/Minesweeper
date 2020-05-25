package com.learn.minesweeper

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Gravity
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.learn.minesweeper.dto.*
import com.learn.minesweeper.dto.Number
import com.learn.minesweeper.level.Difficulty
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.random.Random


class MainActivity : AppCompatActivity() {

    private var difficultyLevel: Difficulty = Difficulty.DIFFICULT
    private lateinit var contentList: ArrayList<ArrayList<Content>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initBoardContentList()
        generateMines()
        calculateNeighborMineCount()
        addCells()
    }

    private fun calculateNeighborMineCount() {
        for ((i, list) in contentList.withIndex()) {
           for ((j, element) in list.withIndex()) {
               var mineCount = 0

               if (element.type != CellType.MINE) {
                   if (isMine(i-1, j-1)) mineCount++
                   if (isMine(i-1, j)) mineCount++
                   if (isMine(i-1, j+1)) mineCount++
                   if (isMine(i, j-1)) mineCount++
                   if (isMine(i, j+1)) mineCount++
                   if (isMine(i+1, j-1)) mineCount++
                   if (isMine(i+1, j)) mineCount++
                   if (isMine(i+1, j+1)) mineCount++
                   if (mineCount > 0) {
                       list[j] =  Number(mineCount)
                   }
               }
           }
        }

        println(contentList)
    }

    private fun isMine(i: Int, j: Int) = if (i >= 0 && i < difficultyLevel.boardSize &&  j >= 0 &&
        j < difficultyLevel.boardSize) contentList[i][j].type == CellType.MINE else false

    private fun generateMines() {
        val mineCount = difficultyLevel.mineCount
        val boardSize = difficultyLevel.boardSize

        for (i in 0 until mineCount) {
            var rowIndex = Random.nextInt(0, boardSize)
            var columnIndex = Random.nextInt(0, boardSize)

            val row = contentList[rowIndex]
            if (row[columnIndex].type == CellType.MINE) {
                i.dec()
            } else {
                row[columnIndex] = Mine()
            }
        }
    }

    private fun initBoardContentList() {
        val boardSize = difficultyLevel.boardSize
        contentList = arrayListOf()

        for (i in 0 until boardSize) {
            val list = arrayListOf<Content>()
            for (j in 0 until boardSize)
                list.add(Empty())
            contentList.add(list)
        }
    }

    private fun addCells() {
        val height: Int = getScreenHeight()

        for ((i, list) in contentList.withIndex()) {
            val row = LinearLayout(this)
            row.gravity = Gravity.CENTER_HORIZONTAL
            row.orientation = LinearLayout.HORIZONTAL
            row.weightSum = difficultyLevel.boardSize.toFloat()
            for ((j, element) in list.withIndex()) {
                val cell = Cell(this, element)
                cell.background = ContextCompat.getDrawable(this, R.drawable.cell_background_not_open)
                val param = LinearLayout.LayoutParams(0, (height / (difficultyLevel.boardSize) - 50), 1F)
                cell.layoutParams = param
                row.addView(cell)
                cell.setOnClickListener {
                        when (cell.content.type) {
                            CellType.MINE -> {
                                // Open up all cells and Game over

                            }
                            CellType.EMPTY -> {
                                // Open up all empty neighbor cells
                            }
                            CellType.NUMBER -> {
                                // Open up and show count
                            }
                        }
                }
            }
            board.addView(row)
        }
    }

    private fun getScreenHeight(): Int {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }
}
