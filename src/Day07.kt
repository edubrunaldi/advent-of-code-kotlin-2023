
private enum class HandType {
    FIVE_OF_KIND,
    FOUR_OF_KIND,
    FULL_HOUSE,
    THREE_OF_KIND,
    TWO_PAIR,
    ONE_PAIR,
    HIGH_CARD;

    companion object {
        fun getHandTypeByCards(cards: String): HandType {
            val diffCards = cards.groupingBy { it }.eachCount()
            return if (diffCards.any { it.value == 5 }) {
                return FIVE_OF_KIND
            } else if (diffCards.any { it.value == 4 }) {
                return FOUR_OF_KIND
            } else if (diffCards.any { it.value == 3 } && diffCards.any { it.value == 2 }) {
                return FULL_HOUSE
            } else if (diffCards.any { it.value == 3 }) {
                return THREE_OF_KIND
            } else if (diffCards.count { it.value == 2 } == 2) {
                return TWO_PAIR
            } else if (diffCards.any { it.value == 2 }) {
                return ONE_PAIR
            } else {
                HIGH_CARD
            }
        }
        fun getHandTypeByCardsWithJoker(cards: String): HandType {
            val diffCards = cards.groupingBy { it }.eachCount()
            val response: HandType
                if (diffCards.any { it.value == 5 }) { // 5 of kind
                    response = FIVE_OF_KIND // A A A A A; J J J J J
            } else if (diffCards.any { it.value == 4 }) { // 4 of kind
                if (diffCards.contains('J')) {
                    response =FIVE_OF_KIND // A A A A J; J J J J A
                } else {
                    response = FOUR_OF_KIND // A A A A K
                }
            } else if (diffCards.any { it.value == 3 } && diffCards.any { it.value == 2 }) {
                if(diffCards.contains('J')) {
                    response =FIVE_OF_KIND // J J J K K ; J J K K K
                } else {
                    response = FULL_HOUSE // K K A A A
                }
            } else if (diffCards.any { it.value == 3 }) {
                if (diffCards.filter { it.key == 'J' }.isNotEmpty()) {
                    response = FOUR_OF_KIND // A A A J K; J J J A K
                } else {
                    response = THREE_OF_KIND // A A A K 9
                }
            } else if (diffCards.count { it.value == 2 } == 2) {
                if (diffCards.filter { it.key == 'J' }.any { it.value == 2 }) {
                    response = FOUR_OF_KIND // A A J J K
                } else if (diffCards.filter { it.key == 'J' }.any { it.value == 1 }) {
                    response = FULL_HOUSE // A A K K J
                } else {
                    response = TWO_PAIR // A A K K 9
                }
            } else if (diffCards.any { it.value == 2 }) {
                if (diffCards.contains('J')) {
                    response = THREE_OF_KIND // A A J K 9; J J A K 9
                } else {
                    response = ONE_PAIR // A A 9 8 7
                }
            } else {
                if (diffCards.contains('J')) {
                    response = ONE_PAIR // A K Q T J
                } else {
                    response = HIGH_CARD // A K Q T 9
                }
            }
            return response
        }
    }
}
fun main() {
    val rank: Set<Char> = setOf('A', 'K', 'Q', 'J', 'T', '9', '8', '7', '6', '5', '4', '3', '2')
    val rankWithJoker: Set<Char> = setOf('A', 'K', 'Q', 'T', '9', '8', '7', '6', '5', '4', '3', '2', 'J')

    data class Hand(val cards: String, val bind: Long, val type: HandType)

    fun handComparator(isPart1: Boolean = true): Comparator<Hand> = Comparator { hand1, hand2 ->
        val typeComparison = hand1.type.ordinal.compareTo(hand2.type.ordinal)
        if (typeComparison != 0) {
            typeComparison
        } else {
            val cardComparisonList = hand1.cards.zip(hand2.cards).map { (c1, c2) ->
                if(isPart1)
                    rank.indexOf(c1).compareTo(rank.indexOf(c2))
                else
                    rankWithJoker.indexOf(c1).compareTo(rankWithJoker.indexOf(c2))
            }
            cardComparisonList.find { it != 0 } ?: 0
        }
    }

    fun toHands(input: List<String>, isPart1: Boolean = true): List<Hand> {
        val hands = mutableListOf<Hand>()
        for (hand in input) {
            hands.add(
                Hand(
                    cards = hand.split(" ")[0].trim(),
                    bind = hand.split(" ")[1].trim().toLong(),
                    type = if(isPart1) HandType.getHandTypeByCards(hand.split(" ")[0].trim())
                    else HandType.getHandTypeByCardsWithJoker(hand.split(" ")[0].trim())
                )
            )
        }
        return hands
    }

    fun part1(input: List<String>): Long =
        toHands(input)
            .sortedWith(handComparator())
            .reversed()
            .mapIndexed { index, hand -> hand.bind * (index+1) }
            .sum()

    fun part2(input: List<String>): Long =
        toHands(input, false)
            .sortedWith(handComparator(false))
            .onEach { println(it) }
            .mapIndexed { index, hand -> hand.bind * (index+1) }
            .sum()

//     test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    check(part1(testInput) == 6440L)
    check(part2(testInput) == 5905L)

    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}
