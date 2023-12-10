import java.util.HashSet
import kotlin.io.path.Path

enum class PipeDirection {
    UP,
    DOWN,
    LEFT,
    RIGHT,
    NONE;
}

fun main() {
    data class Tile(val tile: Char, val position: Pair<Int,Int>)
    val tiles = mapOf(
        Pair('|', listOf(PipeDirection.UP, PipeDirection.DOWN)),
        Pair('-', listOf(PipeDirection.LEFT, PipeDirection.RIGHT)),
        Pair('L', listOf(PipeDirection.UP, PipeDirection.RIGHT)),
        Pair('J', listOf(PipeDirection.UP, PipeDirection.LEFT)),
        Pair('7', listOf(PipeDirection.LEFT, PipeDirection.DOWN)),
        Pair('F', listOf(PipeDirection.RIGHT, PipeDirection.DOWN)),
        Pair('.', listOf(PipeDirection.NONE, PipeDirection.NONE)),
    )

    fun Char.isStartPoint(): Boolean = this == 'S'



    fun findStartPoint(input: List<String>): Pair<Int, Int> {
        for ((i, row) in input.withIndex()) {
            for ((j, tile) in row.withIndex()) {
                if(tile.isStartPoint()) {
                    return Pair(i, j)
                }
            }
        }
        throw RuntimeException("m=findStartPoint; Tile with S not found")
    }

    fun findPipeOnStartPoint(input: List<String>, startPoint: Pair<Int, Int>): Char {
        val directions = mutableListOf<PipeDirection>()
        // up
        if(startPoint.first - 1 >= 0) {
            val tile = input[startPoint.first - 1][startPoint.second]
            if(tiles[tile]!!.contains(PipeDirection.DOWN)) {
                directions.add(PipeDirection.UP)
            }
        }
        // down
        if(startPoint.first + 1 < input.size) {
            val tile = input[startPoint.first + 1][startPoint.second]
            if(tiles[tile]!!.contains(PipeDirection.UP)) {
                directions.add(PipeDirection.DOWN)
            }
        }
        // left
        if(startPoint.second - 1 >= 0) {
            val tile = input[startPoint.first][startPoint.second - 1]
            if(tiles[tile]!!.contains(PipeDirection.RIGHT)) {
                directions.add(PipeDirection.LEFT)
            }
        }
        // right
        if(startPoint.second + 1 < input[0].length) {
            val tile = input[startPoint.first][startPoint.second + 1]
            if(tiles[tile]!!.contains(PipeDirection.LEFT)) {
                directions.add(PipeDirection.RIGHT)
            }
        }

        if(directions.size != 2) {
            throw RuntimeException("direction size unexpected, $directions")
        }

        return tiles.entries.find { it.value.containsAll(directions) }!!.key
    }

    fun getDirection(tile: Char, lastDirection: PipeDirection): PipeDirection {
        val directions = tiles[tile]!!
        return if(directions.contains(PipeDirection.UP) && lastDirection != PipeDirection.DOWN) {
            PipeDirection.UP
        } else if (directions.contains(PipeDirection.RIGHT) && lastDirection != PipeDirection.LEFT) {
            PipeDirection.RIGHT
        } else if (directions.contains(PipeDirection.DOWN) && lastDirection != PipeDirection.UP) {
            PipeDirection.DOWN
        } else {
            PipeDirection.LEFT
        }
    }

    fun getTile(nextTilePosition: PipeDirection, input: List<String>, position: Pair<Int, Int>): Tile =
        when(nextTilePosition) {
            PipeDirection.UP -> Tile(input[position.first - 1][position.second], Pair(position.first - 1, position.second))
            PipeDirection.RIGHT -> Tile(input[position.first][position.second + 1], Pair(position.first, position.second + 1))
            PipeDirection.DOWN -> Tile(input[position.first + 1][position.second], Pair(position.first + 1, position.second))
            PipeDirection.LEFT -> Tile(input[position.first][position.second - 1], Pair(position.first, position.second - 1))
            PipeDirection.NONE -> throw RuntimeException("m=getTile unexpected direction")
        }

    fun findPath(input: List<String>): List<Tile> {
        val startPoint: Pair<Int, Int> = findStartPoint(input)
        val startTile: Char = findPipeOnStartPoint(input, startPoint)
        val path = mutableListOf<Tile>().apply { add(Tile('S', startPoint)) }
        var lastDirection = PipeDirection.NONE
        while(path.count { it.tile == 'S' } < 2) {
            val nextTilePosition: PipeDirection = if(path.last().tile == 'S') {
                getDirection(startTile, lastDirection)
            } else {
                getDirection(path.last().tile, lastDirection)
            }
            val tile =getTile(nextTilePosition, input, path.last().position)
            lastDirection = nextTilePosition
            path.add(tile)
        }
        return path
    }

    fun part1(input: List<String>): Int =
       findPath(input).size / 2

    fun hasTileUp(pathPositions: HashSet<Pair<Int, Int>>, initialPosition: Pair<Int, Int>): Boolean {
        var position = Pair(initialPosition.first - 1, initialPosition.second)
        while(position.first >= 0) {
            if(pathPositions.contains(position)) {
                return true
            }
            position = Pair(position.first - 1, position.second)
        }
        return false
    }
    fun hasTileRight(pathPositions: HashSet<Pair<Int, Int>>, initialPosition: Pair<Int, Int>, max: Int): Boolean {
        var position = Pair(initialPosition.first, initialPosition.second + 1)
        while(position.second   < max) {
            if(pathPositions.contains(position)) {
                return true
            }
            position = Pair(position.first, position.second + 1)
        }
        return false
    }
    fun hasTileDown(pathPositions: HashSet<Pair<Int, Int>>, initialPosition: Pair<Int, Int>, max: Int): Boolean {
        var position = Pair(initialPosition.first + 1, initialPosition.second)
        while(position.first < max) {
            if(pathPositions.contains(position)) {
                return true
            }
            position = Pair(position.first + 1, position.second)
        }
        return false
    }
    fun hasTileLeft(pathPositions: HashSet<Pair<Int, Int>>, initialPosition: Pair<Int, Int>): Boolean {
        var position = Pair(initialPosition.first, initialPosition.second - 1)
        while(position.second >= 0) {
            if(pathPositions.contains(position)) {
                return true
            }
            position = Pair(position.first, position.second - 1)
        }
        return false
    }

    fun part2(input: List<String>): Int {
        var tilesEnclosed = 0
        val pathPositions = findPath(input).map { it.position }.toHashSet()
        for ((i, row) in input.withIndex()) {
            for ((j, tile) in row.withIndex()) {
                if(i > 0 && j > 0 && i < input.size - 1 && j < row.length - 1) { // if is not on edge
                    if(!pathPositions.contains(Pair(i,j))) {

                        if(hasTileUp(pathPositions, Pair(i,j)) &&
                            hasTileRight(pathPositions, Pair(i,j), input[0].length) &&
                            hasTileDown(pathPositions, Pair(i,j), input.size) &&
                            // has tile left
                            hasTileLeft(pathPositions, Pair(i,j)) && pathPositions.contains(Pair(i,j - 1))
                        ) {
                            tilesEnclosed++
                        }
                    }
                }
            }
        }
        return tilesEnclosed
    }

//     test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    check(part1(testInput) == 4)
    check(part2(testInput) == 1)
    val testInput1 = readInput("Day10_1_test")
    check(part1(testInput1) == 8)
    check(part2(testInput1) == 1)
    val testInput2 = readInput("Day10_2_test")
    check(part1(testInput2) == 23)
    check(part2(testInput2) == 4)
    val testInput3 = readInput("Day10_3_test")

    val input = readInput("Day10")
    part1(input).println()
    part2(input).println()
}
