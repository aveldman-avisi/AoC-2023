package aoc.y2023

import org.junit.jupiter.api.Test

class Day06Test {

    data class Race(
        val time: Long,
        val distance: Long
    )

    val input1 = listOf(
        Race(40, 233),
        Race(82, 1011),
        Race(84, 1110),
        Race(92, 1487)
    )

    val input2 = listOf(
        Race(40828492, 233101111101487),
    )

    fun determineWaysAmountToBeatRace(race: Race) =
        (1..race.time).count { t ->
            t * (race.time - t) > race.distance
        }

    @Test
    fun solution1() {
        val solution = input1.map { determineWaysAmountToBeatRace(it) }.reduce { acc, num -> acc * num }
        println("Solution: $solution")
    }

    @Test
    fun solution2() {
        val solution = input2.map { determineWaysAmountToBeatRace(it) }.reduce { acc, num -> acc * num }
        println("Solution: $solution")
    }
}