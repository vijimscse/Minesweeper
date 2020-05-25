package com.learn.minesweeper

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import com.learn.minesweeper.dto.CellType
import com.learn.minesweeper.dto.Content
import com.learn.minesweeper.dto.Empty
import com.learn.minesweeper.dto.Mine
import com.learn.minesweeper.level.Difficulty
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private var difficultyLevel: Difficulty = Difficulty.EASY
    private lateinit var contentList: ArrayList<ArrayList<Content>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initBoardContentList()
        generateMines()
        calculateNeighborMineCount()
       // addCells()
    }

    private fun calculateNeighborMineCount() {

    }

    private fun generateMines() {
        val mineCount = difficultyLevel.mineCount
        val boardSize = difficultyLevel.boardSize

        for (i in 0 until mineCount) {
            var i = Random.nextInt(0, boardSize)
            var j = Random.nextInt(0, boardSize)

            val row = contentList[i]
            if (row[j].type == CellType.MINE) {
                i.dec()
            } else {
                row[j] = Mine()
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

        for (i in 0..3) {
            val ll = LinearLayout(this)
            ll.orientation = LinearLayout.HORIZONTAL
            val cell = Cell(this, null)


        }
    }
}
