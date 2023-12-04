package aoc.y2023

import aoc.utils.Utils
import org.junit.jupiter.api.Test

class Day04Test {

    data class Card(
        val id: Int,
        val winningNumbers: List<Int>,
        val numbers: List<Int>
    )

    private fun parseInputToCards(input: String): List<Card> =
        input.split("\n").map { card ->
            val cardId = card.substring(IntRange(5, card.indexOf(":") - 1)).trim().toInt()
            card.replace("  ", " ").substringAfter(":").let {
                val winningNumbers = it.substringBefore("|").trim().split(" ").map { it.toInt() }
                val numbers = it.substringAfter("|").trim().split(" ").map { it.toInt() }
                Card(
                    id = cardId,
                    winningNumbers = winningNumbers,
                    numbers = numbers
                )
            }
        }

    @Test
    fun solution1() {
        val input = Utils().readInputsFromResources("y2023", "day04", "txt")
        val cards = parseInputToCards(input!!)

        val solution = cards.map { card ->
            card.winningNumbers.intersect(card.numbers).foldIndexed(0) { _, sum, _ ->
                if (sum == 0) {
                    1
                } else {
                    sum * 2
                }
            }
        }.sum()
        println("Solution: $solution")
    }

    @Test
    fun solution2() {
        val input = Utils().readInputsFromResources("y2023", "day04", "txt")
        var indicesFinished = 0
        val cards = parseInputToCards(input!!)
        val cardsAndAmount = cards.map { 1 to it.copy() }.toMutableList()

        while (indicesFinished < cards.count()) {
            val card = cards[indicesFinished]
            repeat(cardsAndAmount.first { it.second.id == card.id }.first) {
                val winAmount = card.winningNumbers.intersect(card.numbers).count()
                for (i in 1..winAmount) {
                    if (indicesFinished + i < cards.count()) {
                        val nextCard = cards[indicesFinished + i]
                        val cardAndAmount = cardsAndAmount.first { it.second.id == nextCard.id }
                        cardsAndAmount.remove(cardAndAmount)
                        cardsAndAmount.add(cardAndAmount.first + 1 to cardAndAmount.second)
                    }
                }
            }
            indicesFinished++
        }
        val solution = cardsAndAmount.sumOf { it.first }
        println("Solution: $solution")
    }
}