private data class CubeSet(val blue: Int, val green: Int, val red: Int) {
    constructor(input: String) : this(
        blue = extractColorValues(input, "blue"),
        green = extractColorValues(input, "green"),
        red = extractColorValues(input, "red")
    )

    companion object {
        private fun extractColorValues(input: String, color: String): Int {
            val regex = "(\\d+)\\s+$color".toRegex()
            return regex.findAll(input)
                .sumOf { it.groupValues[1].toInt() }
        }
    }
}

private data class Game(val id: Int, val cubeSets: List<CubeSet>) {
    companion object {
        private const val MAX_RED = 12
        private const val MAX_BLUE = 14
        private const val MAX_GREEN = 13
        fun fromString(input: String): Game {
            val parts = input.split(':')
            val id = parts[0].replace("Game", "").trim().toInt()
            val cubeSets = parts[1].split(';').map { CubeSet(it.trim()) }
            return Game(id, cubeSets)
        }
    }

    fun isValid(): Boolean =
        cubeSets.all { it.blue <= MAX_BLUE }&&
                cubeSets.all { it.green <= MAX_GREEN } &&
                cubeSets.all  { it.red <= MAX_RED }

    fun minCubeSet(): CubeSet =
        CubeSet(
            blue = cubeSets.maxOf { it.blue },
            green = cubeSets.maxOf { it.green },
            red = cubeSets.maxOf { it.red }
        )
}
fun main() {
    fun part1(input: List<String>): Int =
        input.map { Game.fromString(it) }
            .filter { it.isValid() }
            .sumOf { it.id }

    fun part2(input: List<String>): Int =
        input.map { Game.fromString(it) }
            .map { it.minCubeSet() }
            .sumOf { it.blue * it.green * it.red }


//     test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 8)
    check(part2(testInput) == 2286)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}
