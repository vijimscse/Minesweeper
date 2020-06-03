package com.learn.minesweeper.dto

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.learn.minesweeper.level.Level
import kotlin.random.Random

class Board {
    private lateinit var mCells: ArrayList<ArrayList<Cell>>
    private var mBoardSize = Level.EASY.boardSize
    private var mMineCount = Level.EASY.mineCount
    private var mGameOver: MutableLiveData<Boolean> = MutableLiveData(false)

    private fun calculateNeighborMineCount() {
        for ((i, list) in mCells.withIndex()) {
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

    private fun isMine(i: Int, j: Int) = if (i in 0 until mBoardSize && j in 0 until mBoardSize) mCells[i][j].type == CellType.MINE else false

    private fun generateMines() {
        val mineCount = mMineCount
        val boardSize = mBoardSize

        for (i in 0 until mineCount) {
            var rowIndex = Random.nextInt(0, boardSize)
            var columnIndex = Random.nextInt(0, boardSize)

            val row = mCells[rowIndex]
            if (row[columnIndex].type == CellType.MINE) {
                i.dec()
            } else {
                row[columnIndex] = Mine()
            }
        }
    }

    private fun initBoardContentList() {
        val boardSize = mBoardSize
        mCells = arrayListOf()

        for (i in 0 until boardSize) {
            val list = arrayListOf<Cell>()
            for (j in 0 until boardSize)
                list.add(Empty())
            mCells.add(list)
        }
    }

    private fun openAllCells() {
        for (i in 0 until mBoardSize) {
            for (j in 0 until mBoardSize) {
                openACell(i, j)
            }
        }
    }

    private fun openAllEmptyCells(i: Int, j: Int) {
        if (i in 0 until mBoardSize && j in 0 until mBoardSize && mCells[i][j].type == CellType.EMPTY && !mCells[i][j].isOpen) {
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
        mCells[i][j].isOpen = true
    }

    internal fun open(position: Pair<Int, Int>) {
        var element = mCells[position.first][position.second]
        when (element.type) {
            CellType.MINE -> {
                // Open up all cells and Show Game over
                openAllCells()
                mGameOver.value = true
            }
            CellType.EMPTY -> {
                // Open up all empty neighbor cells
                openAllEmptyCells(position.first, position.second)
            }
            CellType.NUMBER -> {
                // Open up and show count
                openACell(position.first, position.second)
            }
        }
    }

    internal fun setBoardSize(size: Int) {
        mBoardSize = size
    }

    internal fun setMineCount(count: Int) {
        mMineCount = count
    }

    internal fun getCells() = mCells

    internal fun initBoardContents() {
        initBoardContentList()
        generateMines()
        calculateNeighborMineCount()
    }

    internal fun isGameOver(): LiveData<Boolean> = mGameOver
}