package com.learn.minesweeper

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.learn.minesweeper.dto.*
import com.learn.minesweeper.dto.Number
import com.learn.minesweeper.level.Level
import kotlin.random.Random

class GameViewModel() : ViewModel() {
    private var mLevel: MutableLiveData<Level> = MutableLiveData(Level.EASY)
    private lateinit var mContentList: ArrayList<ArrayList<Content>>

    internal fun initBoardContents() {
        initBoardContentList()
        generateMines()
        calculateNeighborMineCount()
    }

    internal fun getGameLevel(): LiveData<Level> = mLevel

    internal fun setBoardSize(level: Level) {
        mLevel.value = level
    }

    internal fun getBoardSize() = mLevel.value?.boardSize ?: Level.EASY.boardSize

    internal fun getContentList() = mContentList

    private fun calculateNeighborMineCount() {
        for ((i, list) in mContentList.withIndex()) {
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
    }

    private fun isMine(i: Int, j: Int) = if (i >= 0 && i < mLevel.value?.boardSize ?: 0 &&  j >= 0 &&
        j < mLevel.value?.boardSize ?: 0) mContentList[i][j].type == CellType.MINE else false

    private fun generateMines() {
        val mineCount = mLevel.value?.mineCount ?: 0
        val boardSize = mLevel.value?.boardSize ?: 0

        for (i in 0 until mineCount) {
            var rowIndex = Random.nextInt(0, boardSize)
            var columnIndex = Random.nextInt(0, boardSize)

            val row = mContentList[rowIndex]
            if (row[columnIndex].type == CellType.MINE) {
                i.dec()
            } else {
                row[columnIndex] = Mine()
            }
        }
    }

    private fun initBoardContentList() {
        val boardSize = mLevel.value?.boardSize ?: 0
        mContentList = arrayListOf()

        for (i in 0 until boardSize) {
            val list = arrayListOf<Content>()
            for (j in 0 until boardSize)
                list.add(Empty())
            mContentList.add(list)
        }
    }

    private fun openAllCells() {
        for (i in 0 until (mLevel.value?.boardSize ?: Level.EASY.boardSize)) {
            for (j in 0 until (mLevel.value?.boardSize ?: Level.EASY.boardSize)) {
                openACell(i, j)
            }
        }
    }

    private fun openAllEmptyCells(i: Int, j: Int) {
        if (i >= 0 && i < mLevel.value?.boardSize ?: 0 &&  j >= 0 &&
            j < mLevel.value?.boardSize ?: 0 && mContentList[i][j].type == CellType.EMPTY &&
            !mContentList[i][j].isOpen) {
            openACell(i, j)

            openAllEmptyCells(i-1, j-1)
            openAllEmptyCells(i-1, j)
            openAllEmptyCells(i-1, j+1)

            openAllEmptyCells(i, j-1)
            openAllEmptyCells(i, j+1)
            openAllEmptyCells(i+1, j-1)

            openAllEmptyCells(i+1, j)
            openAllEmptyCells(i+1, j+1)
        } else {
            return
        }
    }

    private fun openACell(i: Int, j: Int) {
        mContentList[i][j].isOpen = true
    }

    internal fun open(tagPair: Pair<Int, Int>) {
        var element = getContentList()[tagPair.first][tagPair.second]
        when (element.type) {
            CellType.MINE -> {
                // Open up all cells and Show Game over
                openAllCells()
            }
            CellType.EMPTY -> {
                // Open up all empty neighbor cells
                openAllEmptyCells(tagPair.first, tagPair.second)
            }
            CellType.NUMBER -> {
                // Open up and show count
                openACell(tagPair.first, tagPair.second)
            }
        }
    }

    internal fun reset() {
        mContentList.clear()
    }
}