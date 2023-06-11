package board

import board.Direction.*

open class SquareBoardImplementation(override val width: Int): SquareBoard {
    private val cells: Array<Array<Cell>> = Array(width) { Array(width) { Cell(0, 0) } }

    init {
        for (i in 1..width) {
            for (j in 1..width) {
                cells[i - 1][j - 1] = Cell(i, j)
            }
        }
    }

    override fun getCellOrNull(i: Int, j: Int): Cell? {
        return cells.getOrNull(i - 1)?.getOrNull(j - 1)
    }

    override fun getCell(i: Int, j: Int): Cell {
        return getCellOrNull(i, j) ?: throw IllegalArgumentException("Invalid cell coordinates")
    }

    override fun getAllCells(): Collection<Cell> {
        return cells.flatten()
    }

    override fun getRow(i: Int, jRange: IntProgression): List<Cell> {
        return jRange.mapNotNull { j -> getCellOrNull(i, j) }
    }

    override fun getColumn(iRange: IntProgression, j: Int): List<Cell> {
        return iRange.mapNotNull { i -> getCellOrNull(i, j) }
    }

    override fun Cell.getNeighbour(direction: Direction): Cell? {
        return when (direction) {
            UP -> getCellOrNull(i - 1,  j)
            RIGHT -> getCellOrNull(i,  j + 1)
            DOWN -> getCellOrNull(i + 1,  j)
            LEFT -> getCellOrNull(i,  j - 1)
        }
    }
}

class GameBoardImplementation<T>(override val width: Int): GameBoard<T>, SquareBoardImplementation(width) {
    private val cellValues: MutableMap<Cell, T?> = mutableMapOf()

    init {
        for(r in 1..width) {
            for(c in 1..width) {
                cellValues[Cell(r, c)] = null
            }
        }
    }

    override fun getCellOrNull(i: Int, j: Int): Cell? {
        if (i !in 1..width || j !in 1..width) {
            return null
        }

        return cellValues.keys.find { it.i == i && it.j == j }
    }

    override fun getCell(i: Int, j: Int): Cell {
        if (i !in 1..width || j !in 1..width) {
            throw IllegalArgumentException("Invalid cell coordinates")
        }

        return getCellOrNull(i, j)!!
    }

    override fun getAllCells(): Collection<Cell> {
        return cellValues.keys
    }

    override fun get(cell: Cell): T? {
        return cellValues[cell]
    }

    override fun all(predicate: (T?) -> Boolean): Boolean {
        return cellValues.values.all(predicate)
    }

    override fun any(predicate: (T?) -> Boolean): Boolean {
        return cellValues.values.any(predicate)
    }

    override fun find(predicate: (T?) -> Boolean): Cell? {
        return cellValues.entries.find { predicate(it.value) }?.key
    }

    override fun filter(predicate: (T?) -> Boolean): Collection<Cell> {
        return cellValues.filter { predicate(it.value) }.keys
    }

    override fun set(cell: Cell, value: T?) {
        cellValues[cell] = value
    }
}

fun createSquareBoard(width: Int): SquareBoard = SquareBoardImplementation(width)
fun <T> createGameBoard(width: Int): GameBoard<T> = GameBoardImplementation(width)
