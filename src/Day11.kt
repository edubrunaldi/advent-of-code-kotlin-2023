import kotlin.math.abs

fun main() {

    val galaxy = '#'

    fun findEmptySpace(universe: List<String>): Pair<MutableSet<Int>, MutableSet<Int>> {
        val rowsToExpand = mutableSetOf<Int>()
        val colsToExpand = mutableSetOf<Int>()
        for ((i, row) in universe.withIndex()) {
            var isRowEmpty = true
            for ((j, col) in row.withIndex()) {
                if(col == galaxy) {
                    isRowEmpty = false
                }
                var isColEmpty = true
                for (row2 in universe) {
                    if(row2[j] == galaxy) {
                        isColEmpty = false
                    }
                }
                if(isColEmpty) {
                    colsToExpand.add(j)
                }
            }
            if(isRowEmpty) {
                rowsToExpand.add(i)
            }
        }

        return rowsToExpand to colsToExpand
    }

    fun expandUniverse(universe: List<String>): List<List<Char>> {
        val expandedUniverse = universe.map { it.toMutableList() }.toMutableList()

        val (rowsToExpand, colsToExpand) = findEmptySpace(universe)

        for ((i, row) in rowsToExpand.withIndex()) {
            expandedUniverse.add(row + i, universe[row].toMutableList())
        }
        for ((i, col) in colsToExpand.withIndex()) {
            expandedUniverse.forEach {
                it.add(col + i, '.')
            }
        }

        return expandedUniverse
    }

    fun Pair<Int, Int>.dist(galaxy2: Pair<Int, Int>) =
        abs(first - galaxy2.first) + abs(second - galaxy2.second)

    fun part1(input: List<String>): Int  {
        // expand every row/col
        val expandedGalaxy = expandUniverse(input)
            .onEach {
                it.forEach { c -> print(c) }
                println()
            }

        val galaxies = expandedGalaxy.flatMapIndexed { i: Int, chars: List<Char> ->
            chars.mapIndexedNotNull { j, c ->
                if(c == galaxy) {
                    Pair(i,j)
                } else {
                    null
                }

            }
        }.toMutableList()


        // for every galaxy
        // find the shortest path for the other galaxy
        // sum it
        var sum = 0
        while(galaxies.isNotEmpty()) {
            val currentGalaxy = galaxies.removeFirst()
            sum += galaxies.sumOf { currentGalaxy.dist(it) }
        }

        return sum
    }

    fun getDistance(
        galaxy1: Pair<Int, Int>,
        galaxy2: Pair<Int, Int>,
        universe: List<List<Char>>,
        add: Int,
        rowsToExpand: HashSet<Int>,
        colsToExpand: HashSet<Int>
    ): Long {
        val lowestRow = minOf(galaxy1.first, galaxy2.first)
        val highestRow = maxOf(galaxy1.first, galaxy2.first)
        var sum = 0L
        for (i in (lowestRow+1..highestRow)) {
            if(rowsToExpand.contains(i)) {
                sum += add
            } else {
                sum++
            }
        }

        val lowestCol = minOf(galaxy1.second, galaxy2.second)
        val highestCol = maxOf(galaxy1.second, galaxy2.second)
        for (i in (lowestCol..highestCol)) {
            if(colsToExpand.contains(i)) {
                sum += add
            } else {
                sum++
            }
        }

        return sum - 1
    }

    fun part2(input: List<String>): Long  {
        val (rowsToExpand, colsToExpand) = findEmptySpace(input).let { Pair(it.first.toHashSet(), it.second.toHashSet()) }
        val galaxies = input.map { it.toList() }.flatMapIndexed { i: Int, chars: List<Char> ->
            chars.mapIndexedNotNull { j, c ->
                if(c == galaxy) {
                    Pair(i,j)
                } else {
                    null
                }

            }
        }.toMutableList()

        var sum = 0L
        while (galaxies.isNotEmpty()) {
            val currentGalaxy = galaxies.removeFirst()
            sum += galaxies.sumOf { getDistance(currentGalaxy, it, input.map { i -> i.toList() }, 1000000, rowsToExpand, colsToExpand) }
        }

        return sum
    }


//     test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test")
    check(part1(testInput) == 374)
    check(part2(testInput) == 82000210L)

    val input = readInput("Day11")
    part1(input).println()
    part2(input).println()
}
