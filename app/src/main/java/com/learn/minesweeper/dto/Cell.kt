package com.learn.minesweeper.dto

abstract class Cell(var isOpen: Boolean = false,
                    var type : CellType
) {
}