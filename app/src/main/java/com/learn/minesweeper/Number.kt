package com.learn.minesweeper

class Number(val count: Int = 0): Content() {
    var type = CellType.NUMBER
        private set
}