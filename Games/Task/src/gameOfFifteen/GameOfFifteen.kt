package gameOfFifteen

import board.Cell
import board.Direction
import board.GameBoard
import board.createGameBoard
import game.Game

/*
 * Implement the Game of Fifteen (https://en.wikipedia.org/wiki/15_puzzle).
 * When you finish, you can play the game by executing 'PlayGameOfFifteen'.
 */
class GameOfFifteen(private val  initializer: GameOfFifteenInitializer = RandomGameInitializer()) : Game {
    private val boardSize = 4
    private val board: GameBoard<Int?> = createGameBoard(boardSize)

    init {
        val permutation = initializer.initialPermutation

        require(permutation.size == boardSize * boardSize - 1) {
            "Invalid initial permutation size"
        }

        val emptyCell = board.getAllCells().last() // Get the last cell as the empty cell
        board[emptyCell] = null // Set the empty cell value as null

        var index = 0

        for (cell in board.getAllCells().filter { it != emptyCell }) {
            board[cell] = permutation[index++]
        }
    }

    override fun initialize() {
        // Already initialized in the constructor
    }

    override fun canMove(): Boolean {
        return true // Game of Fifteen allows moves in any state
    }

    override fun hasWon(): Boolean {
        var previousValue = 0

        for (row in 1..boardSize) {
            for (col in 1..boardSize) {
                val value = board[board.getCell(row, col)]

                if (value != null && value != previousValue + 1) {
                    return false
                }

                previousValue = value ?: 0
            }
        }

        return true
    }

    override fun processMove(direction: Direction) {
        val emptyCell = findEmptyCell() ?: return

        val targetCell = when (direction) {
            Direction.UP -> board.getCellOrNull(emptyCell.i + 1, emptyCell.j)
            Direction.RIGHT -> board.getCellOrNull(emptyCell.i, emptyCell.j - 1)
            Direction.DOWN -> board.getCellOrNull(emptyCell.i - 1, emptyCell.j)
            Direction.LEFT -> board.getCellOrNull(emptyCell.i, emptyCell.j + 1)
        }

        if (targetCell != null) {
            board[emptyCell] = board[targetCell]
            board[targetCell] = null
        }
    }

    override fun get(i: Int, j: Int): Int? {
        return board[board.getCell(i, j)]
    }

    private fun findEmptyCell(): Cell? {
        return board.find { it == null }
    }
}

fun newGameOfFifteen(initializer: GameOfFifteenInitializer = RandomGameInitializer()): Game = GameOfFifteen(initializer)
