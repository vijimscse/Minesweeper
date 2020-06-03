package com.learn.minesweeper

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.learn.minesweeper.dto.Board
import com.learn.minesweeper.level.Level

class GameViewModel : ViewModel() {
    private var mLevel: MutableLiveData<Level> = MutableLiveData(Level.EASY)
    private var mBoard = Board()

    internal fun initBoardContents() {
        mBoard.initBoardContents()
    }

    internal fun getGameLevel(): LiveData<Level> = mLevel

    internal fun setBoardSize(level: Level) {
        mBoard.setMineCount(level.mineCount)
        mBoard.setBoardSize(level.boardSize)
        mLevel.value = level
    }

    internal fun getBoardSize() = mLevel.value?.boardSize ?: Level.EASY.boardSize

    internal fun getBoard() = mBoard

    internal fun open(position: Pair<Int, Int>) {
        mBoard.open(position)
    }
}