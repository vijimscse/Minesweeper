package com.learn.minesweeper.dto

import com.learn.minesweeper.level.Level
import kotlin.random.Random

class Board {
    private lateinit var mBoard: ArrayList<ArrayList<Content>>
    private var boardSize = Level.EASY.boardSize
    private var mineCount = Level.EASY.mineCount

    private fun calculateNeighborMineCount() {
        for ((i, list) in mBoard.withIndex()) {
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

    private fun isMine(i: Int, j: Int) = if (i >= 0 && i < boardSize &&  j >= 0 &&
        j < boardSize) mBoard[i][j].type == CellType.MINE else false

    private fun generateMines() {
        val mineCount = mineCount
        val boardSize = boardSize

        for (i in 0 until mineCount) {
            var rowIndex = Random.nextInt(0, boardSize)
            var columnIndex = Random.nextInt(0, boardSize)

            val row = mBoard[rowIndex]
            if (row[columnIndex].type == CellType.MINE) {
                i.dec()
            } else {
                row[columnIndex] = Mine()
            }
        }
    }

    private fun initBoardContentList() {
        val boardSize = boardSize
        mBoard = arrayListOf()

        for (i in 0 until boardSize) {
            val list = arrayListOf<Content>()
            for (j in 0 until boardSize)
                list.add(Empty())
            mBoard.add(list)
        }
    }

    private fun openAllCells() {
        for (i in 0 until boardSize) {
            for (j in 0 until boardSize) {
                openACell(i, j)
            }
        }
    }

    private fun openAllEmptyCells(i: Int, j: Int) {
        if (i >= 0 && i < boardSize &&  j >= 0 &&
            j < boardSize && mBoard[i][j].type == CellType.EMPTY &&
            !mBoard[i][j].isOpen) {
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
        mBoard[i][j].isOpen = true
    }

    internal fun open(position: Pair<Int, Int>) {
        var element = mBoard[position.first][position.second]
        when (element.type) {
            CellType.MINE -> {
                // Open up all cells and Show Game over
                openAllCells()
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

    internal fun reset() {
        mBoard?.clear()
    }


    internal fun setBoardSize(size: Int) {
        boardSize = size
    }

    internal fun setMineCount(count: Int) {
        mineCount = count
    }

    internal fun getBoard() = mBoard

    internal fun initBoardContents() {
        initBoardContentList()
        generateMines()
        calculateNeighborMineCount()
    }
}