package jmazes

import java.util.*

enum class Directions {EAST, SOUTH, WEST, NORTH}
class RowCol (val row : Int, val col : Int)

val relationship = mapOf(
        Directions.EAST to RowCol(0, 1),
        Directions.SOUTH to RowCol(1,0),
        Directions.WEST to RowCol(0, -1),
        Directions.NORTH to RowCol(-1, 0))

// ====================================================================

class MazeCell constructor(val position : RowCol) {
    var walls = arrayOf(true, true, true, true)
    var visited = false

    val col
        get() = position.col

    val row
        get() = position.row

    operator fun get(direction: Directions) = walls[direction.ordinal]

    constructor(row:Int, col:Int) : this(RowCol(row, col))

    fun removeWall(direction : Directions) {
        walls[direction.ordinal] = false
    }
}

// ====================================================================

open class Maze constructor(val rows: Int, val cols: Int,
                       val entry: RowCol = RowCol(0,0),
                       val exit: RowCol = RowCol(rows, cols),
                       val entrySide : Directions = Directions.WEST,
                       val exitSide : Directions = Directions.EAST
                        ) {
    val cells = List<MazeCell>(rows * cols) { MazeCell(it / cols, it % cols) }
    val entryCell
        get() = get(entry)

    val exitCell
        get() = get(exit)

    operator fun get(row : Int, col : Int) = cells[row * cols + col]
    operator fun get(rowCol : RowCol) = get(rowCol.row, rowCol.col)

    // ====================================================================

    private fun getNeighbours(currentCell : MazeCell) : Collection<MazeCell> {
        val  neighbours = mutableListOf<MazeCell>()

        if (currentCell.col > 0) {
            neighbours += this[currentCell.row, currentCell.col - 1]
        }
        if (currentCell.col < cols - 1) {
            neighbours += this[currentCell.row, currentCell.col + 1]
        }

        if (currentCell.row > 0) {
            neighbours += this[currentCell.row - 1, currentCell.col]
        }

        if (currentCell.row < rows - 1) {
            neighbours += this[currentCell.row + 1, currentCell.col]
        }
        return neighbours
    }

    // ====================================================================

    fun getNeigbour(cell : MazeCell, direction: Directions) : MazeCell? {
        val rel = relationship[direction] ?: return null

        return this[cell.row + rel.row, cell.col + rel.col]
    }

    fun getNeigbour(cell : MazeCell, direction: Int) : MazeCell? {
        return getNeigbour(cell, Directions.values()[direction])
    }

    fun removeWall(cell: MazeCell, direction: Int) {
        var cellB = getNeigbour(cell, direction)

        if (cellB != null) {
            removeCommonWall(cell, cellB)
        }
    }

    // ====================================================================

    private fun removeCommonWall(cellA: MazeCell, cellB : MazeCell) {
        if (cellA.col == cellB.col) {
            if (cellA.row < cellB.row) {
                cellA.removeWall(Directions.SOUTH)
                cellB.removeWall(Directions.NORTH)
            } else {
                cellA.removeWall(Directions.NORTH)
                cellB.removeWall(Directions.SOUTH)
            }
        } else {
            if (cellA.col < cellB.col) {
                cellA.removeWall(Directions.EAST)
                cellB.removeWall(Directions.WEST)
            } else {
                cellA.removeWall(Directions.WEST)
                cellB.removeWall(Directions.EAST)
            }
        }
    }

    // ====================================================================

    fun getNonEdgeCells(): Collection<MazeCell> {
        return  this.cells.slice(1..rows - 1).filter { it.col != 0 && it.col != cols - 1 }
    }

    // ====================================================================

    fun randomizeBacktracker(loops : Int = 0) {
        var stack = mutableListOf<MazeCell>()
        var currentCell = get(entry)
        currentCell.removeWall(entrySide)
        currentCell.visited = true
        stack.add(currentCell)

        exitCell.removeWall(exitSide)

        while (stack.size > 0) {
            currentCell = stack.removeLast()

            var neighbours = getNeighbours(currentCell).filter { it.visited == false }

            if (neighbours.isNotEmpty()) {
                stack.add(currentCell)
                var selectedNeighbour = neighbours.random()
                selectedNeighbour.visited = true
                removeCommonWall(currentCell, selectedNeighbour)

                stack.add(selectedNeighbour)
            }
        }

        if (loops > 0) {
            var nonEdge = getNonEdgeCells()
            var targets = nonEdge.shuffled().slice(0..loops)
            for (cell in targets) {
                var walls = cell.walls.indices.filter { cell.walls[it] } // Only walls that still exist
                if (walls.isNotEmpty()) {
                    var wallToRemove = walls.random()
                    removeWall(cell, wallToRemove)
                }
            }
        }
    }
}


fun main(args: Array<String>) {
    var maze = Maze(10, 10)
}

