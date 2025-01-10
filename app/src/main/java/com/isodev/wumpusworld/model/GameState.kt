package com.isodev.wumpusworld.model

data class Position(val x: Int, val y: Int) {
    fun isAdjacent(other: Position): Boolean {
        return (Math.abs(x - other.x) == 1 && y == other.y) ||
                (Math.abs(y - other.y) == 1 && x == other.x)
    }
}

enum class CellContent {
    EMPTY,
    WUMPUS,
    PIT,
    GOLD
}

enum class CellState {
    HIDDEN,
    VISIBLE,
    BREEZE,
    STENCH,
    BOTH_WARNINGS,
    GLITTER
}

data class Cell(
    val content: CellContent = CellContent.EMPTY,
    val state: CellState = CellState.HIDDEN,
    val isVisited: Boolean = false
)

data class GameState(
    val grid: List<List<Cell>> = List(4) { List(4) { Cell() } },
    val playerPosition: Position = Position(0, 0),
    val hasArrow: Boolean = true,
    val isGameOver: Boolean = false,
    val isWin: Boolean = false,
    val message: String = ""
) 