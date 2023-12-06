
fun main() {
    data class RecordRace(val time: Long, val distance: Long)

    fun inputToRecordsRace(input: List<String>): List<RecordRace> {
        val time = input[0].removePrefix("Time:").trim().split(" ").mapNotNull { n -> n.toIntOrNull() }
        val distance = input[1].removePrefix("Distance:").trim().split(" ").mapNotNull { n -> n.toIntOrNull() }
        val recordsRace = mutableListOf<RecordRace>()
        time.forEachIndexed { index, i ->  recordsRace.add(RecordRace(i.toLong(), distance[index].toLong())) }
        return recordsRace
    }

    fun findTotalWinningMethods(recordRace: RecordRace): Int {
        var total = 0
        for (i in 0..recordRace.time) {
            val distance = i * (recordRace.time - i)
            if (distance > recordRace.distance) {
                total++
            }
        }
        return total
    }

    fun part1(input: List<String>): Int =
        inputToRecordsRace(input)
            .map { findTotalWinningMethods(it) }
            .fold(1) {acc: Int, i: Int ->  acc * i}

    fun part2(input: List<String>): Int =
        RecordRace(
            time = input[0].removePrefix("Time:").trim().replace(" ", "").toLong(),
            distance = input[1].removePrefix("Distance:").trim().replace(" ", "").toLong(),
        ).run {  findTotalWinningMethods(this) }

//     test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    check(part1(testInput) == 288)
    check(part2(testInput) == 71503)

    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}
