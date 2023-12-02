val hash = HashMap<String, Int>()
    .apply { this["one"] = 1 }
    .apply { this["two"] = 2 }
    .apply { this["three"] = 3 }
    .apply { this["four"] = 4 }
    .apply { this["five"] = 5 }
    .apply { this["six"] = 6 }
    .apply { this["seven"] = 7 }
    .apply { this["eight"] = 8 }
    .apply { this["nine"] = 9 }

data class DigitAndIndex(val digit: Int, val lastIndex: Int)

/**
 * Calculates the sum of two integers at the specified indices in the given list, if empty return 0.
 */
private fun List<Int>.sumOfFirstAndLast(): Int =
    takeIf { it.isNotEmpty() }
        ?.let { it[0] + it[it.size - 1] }
        ?: 0

private fun getLineCalibration(line: String): Int =
    line
        .mapNotNull { it.digitToIntOrNull() }
        .sumOfFirstAndLast()

private fun getLineCalibrationWithLetter(line: String): Int {
    val list = mutableListOf<Int>()
    var digitAndIndex = getNextDigit(line, 0)
        .also { if(it.digit != -1) list.add(it.digit) }

    while (digitAndIndex.lastIndex < line.length) {
        digitAndIndex = getNextDigit(line, digitAndIndex.lastIndex)
            .also { if(it.digit != -1) list.add(it.digit) }
    }
    return list.sumOfFirstAndLast()
}
private fun getNextDigit(line: String, index: Int): DigitAndIndex {
    var aux = index + 1
    var digit = -1

    while(aux <= line.length) {
        val sub = line.substring(index, aux)
        if (hash.keys.contains(sub)) {
            digit = hash[sub]!!
            break
        } else if(line[index].isDigit()) {
            digit = line[index].digitToInt()
            break
        }
        aux++
    }
    return DigitAndIndex(digit, index + 1)

}

fun main() {
    fun part1(input: List<String>): Int =
        input.sumOf { getLineCalibration(it) }

    fun part2(input: List<String>): Int =
        input.sumOf { getLineCalibrationWithLetter(it) }

//     test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 1)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
