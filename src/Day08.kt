private enum class Direction {
    LEFT,
    RIGHT;
}
fun main() {

    data class Nodes(val left: String, val right: String)

    fun Nodes.getNode(direction: Direction): String =
        if(direction == Direction.LEFT) left else right
    data class DesertMap(val directions: List<Direction>, val map: HashMap<String, Nodes>)

    fun toDesertMap(input: List<String>): DesertMap =
        DesertMap(
            directions = input[0].trim().map { if(it == 'L') Direction.LEFT else Direction.RIGHT },
            map = hashMapOf<String, Nodes>().apply {
                input.forEachIndexed { index, s ->
                    if(index >= 2) {
                        this[s.split(" = ")[0].trim()] = Nodes(
                            left = s.split(" = ")[1].split(", ")[0].trim().replace("(",""),
                            right = s.split(" = ")[1].split(", ")[1].trim().replace(")",""),
                        )
                    }
                }
            }
        )

    fun getNextNode(count: Long, desertMap: DesertMap, actualNode: String): String {
        val directionIndex = count % desertMap.directions.size
        val direction = desertMap.directions[directionIndex.toInt()]
        return desertMap.map[actualNode]!!.getNode(direction)
    }


    fun findLCM(a: Long, b: Long): Long {
        val larger = if (a > b) a else b
        val maxLcm = a * b
        var lcm = larger
        while (lcm <= maxLcm) {
            if (lcm % a == 0L && lcm % b == 0L) {
                return lcm
            }
            lcm += larger
        }
        return maxLcm
    }
    fun findLCMOfListOfNumbers(numbers: List<Long>): Long {
        var result = numbers[0]
        for (i in 1 until numbers.size) {
            result = findLCM(result, numbers[i])
        }
        return result
    }

    fun part1(input: List<String>): Long =
        with(toDesertMap(input)) {
            var count = 0L
            var node = "AAA"
            while (node != "ZZZ") {
                node = getNextNode(count, this, node)
                count++
            }
            count
        }

    fun part2(input: List<String>): Long {
        val desertMap = toDesertMap(input)
        val listNodes = desertMap.map.keys.filter { it[2] == 'A' }.toMutableList()
        val l = mutableListOf<Long>()
        for (node in listNodes) {
            var temp = node
            var i = 0L
            while(temp[2] != 'Z') {
                temp = getNextNode(i, desertMap, temp)
                i++
            }
            l.add(i)
        }
        return findLCMOfListOfNumbers(l)
    }

//     test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    check(part1(testInput) == 2L)
    check(part2(testInput) == 2L)
    val testInput1 = readInput("Day08_1_test")
    check(part1(testInput1) == 6L)
    check(part2(testInput1) == 6L)
    val testInput2 = readInput("Day08_2_test")
    check(part1(testInput2) == 2L)
    check(part2(testInput2) == 6L)

    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()
}
