package com.learn.minesweeper.level

enum class Level(val mineCount: Int = 3, val boardSize: Int = 3) {
    EASY(3, 3),
    MEDIUM(10, 6),
    DIFFICULT(20, 10)
}