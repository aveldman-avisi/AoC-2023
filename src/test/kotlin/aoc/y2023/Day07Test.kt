package aoc.y2023

import aoc.utils.Utils
import org.junit.jupiter.api.Test

class Day07Test {
    data class Hand(
        val strength: Int,
        val cards: String,
        val bid: Int
    ) : Comparable<Hand> {
        override fun compareTo(other: Hand): Int {
            if (this.strength > other.strength) {
                return 1
            }
            if (this.strength < other.strength) {
                return -1
            }

            val thisJokers = this.cards.count { it == 'J' }
            val otherJokers = other.cards.count { it == 'J' }

            for (index in this.cards.indices) {
                if (this.cards[index] != other.cards[index]) {
                    val card1 = getCardValue(this.cards[index])
                    val card2 = getCardValue(other.cards[index])
                    if (card1 != card2) {
                        return card1.compareTo(card2)
                    }
                }
            }
            return 0
        }

        private fun getCardValue(card: Char): Int {
            return when (card) {
                'A' -> 14
                'K' -> 13
                'Q' -> 12
//                'J' -> 11
                'J' -> 1
                'T' -> 10
                else -> card.toString().toInt()
            }
        }
    }

    private fun parseInput(input: String): List<Hand> =
        input.split("\n").map {
            val cards = it.substringBefore(" ")
            val strength = determineHandStrength(cards)
            val bid = it.substringAfter(" ").toInt()
            Hand(strength, cards, bid)
        }


    private fun determineHandStrength(hand: String): Int {
        val cardsAmount = hand.groupBy { it }.filter { it.key != 'J' }.map { it.value.size }
        val jokers = hand.count { it == 'J' }

        if (cardsAmount.contains(5)) {
            return 7 // 5 of a kind
        }
        if (cardsAmount.contains(4)) {
            if (jokers > 0) { return 7 } // 5 of a kind using joker
            return 6 // 4 of a kind
        }
        if (cardsAmount.contains(3)) {
            if (jokers > 0) {
                if (jokers >= 2) { return 7 } // 5 of a kind using jokers
                return 6 // 4 of a kind using jokers
            }

            if (cardsAmount.contains(2)) { return 5 } // Full house

            return 4 // 3 of a kind
        }
        if (cardsAmount.contains(2)) {
            if (jokers > 0) {
                if (jokers > 2) { return 7 } // 5 of a kind using jokers
                if (jokers == 2) { return 6 } // 4 of a kind using jokers
                if (cardsAmount.filter { it >= 2 }.size >= 2 ) {
                    return 5 // full house using jokers
                }
                return 4 // 3 of a kind using jokers
            }

            if (cardsAmount.filter { it >= 2 }.size > 1) {
                return 3 // 2 Pair
            }

            return 2 // Pair
        }

        if (jokers > 0) {
            if (jokers == 5) { return 7 } // 5 of a kind using jokers
            if (jokers == 4) { return 7 } // 5 of a kind using jokers
            if (jokers == 3) { return 6 } // 4 of a kind using jokers
            if (jokers == 2) { return 4 } // 3 of a kind using jokers
            if (jokers == 1) { return 2 } // pair using jokers
        }
        return 1 // High card
    }


    @Test
    fun solution() {
        val input = Utils().readInputsFromResources("y2023", "day07", "txt")
        val hands = parseInput(input!!)
        val sortedHands = hands.sorted()
        val solution = sortedHands.mapIndexed { index, hand -> hand.bid * (index + 1) }.sumOf { it }
        println("Solution: $solution")
    }

}