package com.learn.minesweeper

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.learn.minesweeper.dto.Board
import com.learn.minesweeper.level.Level

class GameViewModel : ViewModel() {
    private var mLevel: MutableLiveData<Level> = MutableLiveData(Level.EASY)
    private var board = Board()

    internal fun initBoardContents() {
        board.initBoardContents()
    }

    internal fun getGameLevel(): LiveData<Level> = mLevel

    internal fun setBoardSize(level: Level) {
        board.setMineCount(level.mineCount)
        board.setBoardSize(level.boardSize)
        mLevel.value = level
    }

    internal fun getBoardSize() = mLevel.value?.boardSize ?: Level.EASY.boardSize

    internal fun getBoard() = board.getBoard()

    internal fun open(position: Pair<Int, Int>) {
        board.open(position)
    }
}