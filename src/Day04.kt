import kotlin.math.pow

private data class Card(val winningNumbers: Set<Int>, val numbers: Set<Int>)

private fun toCard(line: String): Card {
    val (winningNumbers, numbers) = line
        .split(":")[1]
            .split("|")
            .map { it.split(" ") }
            .map {
                it.mapNotNull { n ->
                    n.toIntOrNull()
                }.toSet()
            }
    return Card(winningNumbers, numbers)
}

private fun calcPoints(it: Int): Int = 2F.pow(it-1).toInt()


private fun getTotalCards(cards: List<Card>): Int {
    val cardsNumber = HashMap<Card, Int>()
    cards.forEach { cardsNumber[it] = 1 }

    cards.forEachIndexed { index, card ->
        val matches = card.winningNumbers.intersect(card.numbers).size
        if(index + matches >= cards.size ) {
            throw RuntimeException("index+matchs >= cards.size: ${index+matches} >= ${cards.size}, cardindex $index, card: $card")
        }
        var i = 1
        while(i <= matches) {
            cardsNumber[cards[index + i]] = cardsNumber[cards[index + i]]!! + cardsNumber[card]!!
            i++
        }
    }

    println(cardsNumber)
    return cardsNumber.values.sum()
}
fun main() {

    fun part1(input: List<String>): Int =
       input.map { toCard(it) }
           .map { it.winningNumbers.intersect(it.numbers) }
           .map { it.size }
           .sumOf { calcPoints(it) }

    fun part2(input: List<String>): Int =
        getTotalCards(input.map { toCard(it) })

//     test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 30)

    val input = readInput("Day04")
    toCard(input[0])
    part1(input).println()
    part2(input).println()
}
