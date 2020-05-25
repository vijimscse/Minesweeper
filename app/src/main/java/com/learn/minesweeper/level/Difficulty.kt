package com.learn.minesweeper.level

enum class Difficulty(val mineCount: Int = 3, val boardSize: Int = 3) {
    EASY(3, 3),
    MEDIUM(6, 6),
    DIFFICULT(10, 10)
}