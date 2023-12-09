fun main() {

    data class Extrapolate(val first: Long, val last: Long)

    fun subtractHistValues(diffHist: List<Long>): List<Long> {
        val newDiffHist = mutableListOf<Long>()

        for ((index, value) in diffHist.withIndex()) {
            if(index < diffHist.size - 1) {
                newDiffHist.add(diffHist[index+1] - value)
            }
        }

        return newDiffHist
    }

    fun findExtrapolateValue(valueHist: List<Long>): Extrapolate {
        val lastValues = mutableListOf<Long>()
        val firstValues = mutableListOf<Long>()
        var diffHist: List<Long> = valueHist.toList()
        while(!diffHist.all { it == 0L }) {
            firstValues.add(diffHist.first())
            lastValues.add(diffHist.last())
            diffHist = subtractHistValues(diffHist)
        }

        val firstExtrapolateValue = firstValues.reversed()
            .fold(0) {acc: Long, value: Long -> value - acc }
        return Extrapolate(firstExtrapolateValue, lastValues.sum())
    }

    fun part1(input: List<String>): Long =
        input
            .map { it.split(" ").map { n -> n.toLong() } }
            .sumOf { findExtrapolateValue(it).last }

    fun part2(input: List<String>): Long =
        input
            .map { it.split(" ").map { n -> n.toLong() } }
            .sumOf { findExtrapolateValue(it).first }

//     test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    check(part1(testInput) == 114L)
    check(part2(testInput) == 2L)
    val testInput1 = readInput("Day09_1_test")
    check(part1(testInput1) == -9L)
    check(part2(testInput1) == 13L)

    val input = readInput("Day09")
    part1(input).println()
    part2(input).println()
}
