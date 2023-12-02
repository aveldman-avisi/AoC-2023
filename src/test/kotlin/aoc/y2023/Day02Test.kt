package aoc.y2023

import aoc.utils.Utils
import org.junit.jupiter.api.Test

class Day02Test {
    data class Hand(
        val red: Int,
        val blue: Int,
        val green: Int
    )

    data class Game(
        val id: Int,
        val hands: List<Hand>
    )

    private fun parseInputToGames(input: String) =
        input.split("\n").map { game ->
            val gameId = game.substring(IntRange(5, game.indexOf(":")-1)).toInt()
            game.substringAfter(":").let {
                val hands = it.split(";").map { pull ->
                    pull.split(",").map {
                        it.trimStart().split(" ").let {
                            it[0] to it[1]
                        }
                    }
                }.map { parseListToHand(it) }
                Game(gameId, hands)
            }
        }

    private fun parseListToHand(balls: List<Pair<String, String>>) =
        Hand(
            red = balls.firstOrNull { it.second == "red" }?.first?.toInt() ?: 0,
            blue = balls.firstOrNull { it.second == "blue" }?.first?.toInt() ?: 0,
            green = balls.firstOrNull { it.second == "green" }?.first?.toInt() ?: 0,
        )


    @Test
    fun solution1() {
        val input = Utils().readInputsFromResources("y2023", "day02", "txt")
        val games = parseInputToGames(input!!)

        val validGames = games.filter {
            it.hands.all {
                it.red <= 12 && it.green <= 13 && it.blue <= 14
            }
        }
        println("Solution: ${validGames.sumOf { it.id }}")
    }

    @Test
    fun solution2() {
        val input = Utils().readInputsFromResources("y2023", "day02", "txt")
        val games = parseInputToGames(input!!)

        val powers = games.map {
            val red = it.hands.maxOf { it.red }
            val green = it.hands.maxOf { it.green }
            val blue = it.hands.maxOf { it.blue }
            red * green * blue
        }

        println("Solution: ${powers.sumOf { it }}")
    }
}
