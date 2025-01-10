package com.isodev.wumpusworld.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isodev.wumpusworld.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

class GameViewModel : ViewModel() {
    private val _gameState = MutableStateFlow(initializeGame())
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()

    //Oyunun başlangıcı
    private fun initializeGame(): GameState {
        val grid = MutableList(4) { MutableList(4) { Cell() } }
        
        // Wumpusun Random yeri
        var wumpusPos: Position
        do {
            wumpusPos = Position(Random.nextInt(4), Random.nextInt(4))
        } while (wumpusPos == Position(0, 0))
        grid[wumpusPos.x][wumpusPos.y] = Cell(content = CellContent.WUMPUS)

        // Altının Yeri
        var goldPos: Position
        do {
            goldPos = Position(Random.nextInt(4), Random.nextInt(4))
        } while (goldPos == Position(0, 0) || goldPos == wumpusPos)
        grid[goldPos.x][goldPos.y] = Cell(content = CellContent.GOLD)

        // 2 tane pitin yeri
        repeat(2) {
            var pitPos: Position
            do {
                pitPos = Position(Random.nextInt(4), Random.nextInt(4))
            } while (pitPos == Position(0, 0) || 
                    pitPos == wumpusPos || 
                    pitPos == goldPos ||
                    grid[pitPos.x][pitPos.y].content == CellContent.PIT)
            grid[pitPos.x][pitPos.y] = Cell(content = CellContent.PIT)
        }

        // Önce bu hücrenin uyarılarını güncelle
        updateCellWarnings(Position(0, 0), grid)

        // Sonra ziyaret edildi olarak işaretle
        grid[0][0] = grid[0][0].copy(
            state = CellState.VISIBLE,
            isVisited = true
        )

        return GameState(grid = grid)
    }

    fun movePlayer(newPosition: Position) {
        val currentState = _gameState.value
        if (currentState.isGameOver || !isValidMove(newPosition)) return

        viewModelScope.launch {
            val newGrid = currentState.grid.map { it.toMutableList() }.toMutableList()
            val cell = newGrid[newPosition.x][newPosition.y]

            when (cell.content) {
                CellContent.WUMPUS -> endGame(false, "Oyun Bitti! Wumpusu Buldun!")
                CellContent.PIT -> endGame(false, "Oyun Bitti! Çukura Düştün!")
                CellContent.GOLD -> endGame(true, "Tebrikler! Altını Buldun!")
                else -> {
                    // Update the current cell with its warnings and mark as visited
                    updateCellState(newPosition, newGrid)
                    
                    // Update visibility of adjacent cells
                    updateAdjacentCellsVisibility(newPosition, newGrid)
                    
                    _gameState.value = currentState.copy(
                        grid = newGrid,
                        playerPosition = newPosition
                    )
                }
            }
        }
    }

    private fun isValidMove(position: Position): Boolean {
        return position.x in 0..3 && position.y in 0..3 &&
                (position.isAdjacent(_gameState.value.playerPosition) ||
                        position == _gameState.value.playerPosition)
    }

    private fun updateCellState(position: Position, grid: MutableList<MutableList<Cell>>) {
        // First update the warnings for this cell
        updateCellWarnings(position, grid)
        
        // Then mark it as visited
        grid[position.x][position.y] = grid[position.x][position.y].copy(
            isVisited = true
        )
    }

    private fun hasAdjacentContent(
        position: Position,
        content: CellContent,
        grid: List<List<Cell>>
    ): Boolean {
        val adjacentPositions = listOf(
            Position(position.x - 1, position.y),
            Position(position.x + 1, position.y),
            Position(position.x, position.y - 1),
            Position(position.x, position.y + 1)
        )

        return adjacentPositions.any { pos ->
            pos.x in 0..3 && pos.y in 0..3 && grid[pos.x][pos.y].content == content
        }
    }

    private fun endGame(isWin: Boolean, message: String) {
        _gameState.value = _gameState.value.copy(
            isGameOver = true,
            isWin = isWin,
            message = message
        )
    }

    fun shootArrow(position: Position) {
        val currentState = _gameState.value
        if (!currentState.hasArrow || currentState.isGameOver) return

        viewModelScope.launch {
            if (currentState.grid[position.x][position.y].content == CellContent.WUMPUS) {
                endGame(true, "Wumpusu Öldürdün!")
            } else {
                _gameState.value = currentState.copy(
                    hasArrow = false,
                    message = "Kaçırdın! Ok boşa gitti."
                )
            }
        }
    }

    fun restartGame() {
        _gameState.value = initializeGame()
    }

    private fun updateAdjacentCellsVisibility(position: Position, grid: MutableList<MutableList<Cell>>) {
        val adjacentPositions = listOf(
            Position(position.x - 1, position.y),
            Position(position.x + 1, position.y),
            Position(position.x, position.y - 1),
            Position(position.x, position.y + 1)
        ).filter { pos -> pos.x in 0..3 && pos.y in 0..3 }

        // Sadece ziyaret edilmemiş ve komşu olmayan hücreleri gizle
        for (i in 0..3) {
            for (j in 0..3) {
                val currentPos = Position(i, j)
                val cell = grid[i][j]
                if (!cell.isVisited && !adjacentPositions.contains(currentPos)) {
                    grid[i][j] = cell.copy(state = CellState.HIDDEN)
                }
            }
        }

        // Ziyaret edilmemiş komşu hücreler için uyarıları göster
        for (pos in adjacentPositions) {
            if (!grid[pos.x][pos.y].isVisited) {
                updateCellWarnings(pos, grid)
            }
        }
    }

    // Hücre uyarılarını güncellemek için yeni yardımcı fonksiyon
    private fun updateCellWarnings(position: Position, grid: MutableList<MutableList<Cell>>) {
        val hasWumpusNearby = hasAdjacentContent(position, CellContent.WUMPUS, grid)
        val hasPitNearby = hasAdjacentContent(position, CellContent.PIT, grid)
        val hasGoldNearby = hasAdjacentContent(position, CellContent.GOLD, grid)

        val newState = when {
            hasWumpusNearby && hasPitNearby -> CellState.BOTH_WARNINGS
            hasWumpusNearby -> CellState.STENCH
            hasPitNearby -> CellState.BREEZE
            hasGoldNearby -> CellState.GLITTER
            else -> CellState.VISIBLE
        }

        grid[position.x][position.y] = grid[position.x][position.y].copy(
            state = newState
        )
    }
} 