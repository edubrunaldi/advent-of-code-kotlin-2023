/**
 * col is the first (lowest) position of the number
 * e.g:
 * 467..114..
 * ...*......
 *
 * 467 => Position(0,0)
 * 114 => Position(0,5)
 */
private data class Position(val row: Int, val col: Int)
private data class Number(val value: Int, val position: Position)


private const val SYMBOLS = "*@+/%$#=-!^&"
private const val ASTERISK = "*"


private fun getSymbolPosition(row: Int, col: Int, char: Char, symbols: String): Position? =
    if(symbols.contains(char)) {
        Position(row, col)
    } else {
        null
    }

private fun findSymbols(line: String, row: Int, onlyAsterisk: Boolean = false): List<Position> =
    line.mapIndexedNotNull { index, it -> getSymbolPosition(row, index, it, if(onlyAsterisk) ASTERISK else SYMBOLS) }

private fun getValidAdjacentCells(position: Position, maxRow: Int, maxCol: Int): List<Position> {
    val positions = mutableListOf<Position>()
    if(position.row > 0) {
        // top top
        positions.add(Position(position.row - 1, position.col))

        if(position.col > 0) {
            // top left
            positions.add(Position(position.row - 1, position.col - 1))
        }

        if(position.col < maxCol - 1) {
            // top right
            positions.add(Position(position.row - 1, position.col + 1))
        }
    }
    if(position.col > 0) {
        // left
        positions.add(Position(position.row, position.col - 1))
    }
    if(position.col < maxCol - 1) {
        // right
        positions.add(Position(position.row, position.col + 1))
    }

    if(position.row < maxRow - 1) {
        // buttom buttom
        positions.add(Position(position.row + 1, position.col))

        if(position.col > 0) {
            // buttom left
            positions.add(Position(position.row + 1, position.col - 1))
        }

        if(position.col < maxCol - 1) {
            // buttom right
            positions.add(Position(position.row + 1, position.col + 1))
        }
    }

    return positions
}


private fun getNumbersAdjacentToSymbol(input: List<String>, position: Position, maxRow: Int, maxCol: Int): List<Number> =
    getValidAdjacentCells(position, maxRow, maxCol)
        .mapNotNull {
            if(input[it.row][it.col].isDigit()) {
                getNumberFromPosition(input, it)
            } else {
                null
            }
        }

private fun getNumberFromPosition(input: List<String>, position: Position): Number {
    val line = input[position.row]

    var s = ""
    var index = position.col
    while(index >= 0 && line[index].isDigit()) {
        s = line[index] + s
        index--
    }
    val col = if(index < 0) 0 else index
    index = position.col + 1
    while(index < line.length && line[index].isDigit()) {
        s += line[index]
        index++
    }
    return Number(s.toInt(), Position(position.row, col))
}


fun main() {

    fun part1(input: List<String>): Int =
       input
           .flatMapIndexed { row: Int, it: String ->  findSymbols(it, row) }
           .flatMap { getNumbersAdjacentToSymbol(input, it, input.size, input[0].length) }
           .distinct()
           .sumOf { it.value }

    fun gearRatio(numbersByAsterisk: List<Number>): Int =
        if (numbersByAsterisk.size == 2) {
            numbersByAsterisk[0].value * numbersByAsterisk[1].value
        } else {
            0
        }

    fun part2(input: List<String>): Int =
        input
            .flatMapIndexed { row: Int, it: String -> findSymbols(it, row, true) }
            .map { getNumbersAdjacentToSymbol(input, it, input.size, input[0].length) }
            .sumOf { gearRatio(it.distinct()) }

//     test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 4361)
    check(part2(testInput) == 467835)

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}
