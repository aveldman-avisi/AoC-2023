package aoc.y2023

import aoc.utils.Utils
import org.junit.jupiter.api.Test
import java.lang.Exception

class Day18Test {

    enum class DIRECTION { UP, RIGHT, DOWN, LEFT }

    @Test
    fun solution1() {
        val input = Utils().readInputsFromResources("y2023", "day18", "txt")!!
        val digSites = getDigSites(input.split("\n"))
        val border = digSites.sumOf { it.second }

        var lastPoint: Pair<Long, Long> = Pair(0,0)
        val points = digSites.map { (direction, distance) ->
            val newPoint = when (direction) {
                DIRECTION.UP -> Pair(lastPoint.first, lastPoint.second - distance)
                DIRECTION.RIGHT -> Pair(lastPoint.first + distance, lastPoint.second)
                DIRECTION.DOWN -> Pair(lastPoint.first, lastPoint.second + distance)
                DIRECTION.LEFT -> Pair(lastPoint.first - distance, lastPoint.second)
            }
            lastPoint = newPoint
            newPoint
        }

        val area = shoelace(points) - (border / 2) + 1 + border
        println("Solution: $area")
    }

    @Test
    fun solution2() {
        val input = Utils().readInputsFromResources("y2023", "day18", "txt")!!
        val digSites = getDigSites2(input.split("\n"))
        val border = digSites.sumOf { it.second.toLong() }

        var lastPoint: Pair<Long, Long> = Pair(0,0)
        val points = digSites.map { (direction, distance) ->
            val newPoint = when (direction) {
                DIRECTION.UP -> Pair(lastPoint.first, lastPoint.second - distance)
                DIRECTION.RIGHT -> Pair(lastPoint.first + distance, lastPoint.second)
                DIRECTION.DOWN -> Pair(lastPoint.first, lastPoint.second + distance)
                DIRECTION.LEFT -> Pair(lastPoint.first - distance, lastPoint.second)
            }
            lastPoint = newPoint
            newPoint
        }

        val area = shoelace(points) - (border / 2) + 1 + border
        println("Solution: $area")
    }

    @OptIn(ExperimentalStdlibApi::class)
    private fun getDigSites2(lines: List<String>): List<Pair<DIRECTION, Int>> =
        lines.map {
            val (_, _, encodedDistance) = it.split(" ")
            val distance = encodedDistance.substringAfter('#').substring(0, 5)
            val decodedDistance = distance.hexToInt(HexFormat.Default)

            when (val direction = encodedDistance.substringAfter('#').substring(5,6).toInt()) {
                3 -> DIRECTION.UP
                0 -> DIRECTION.RIGHT
                1 -> DIRECTION.DOWN
                2 -> DIRECTION.LEFT
                else -> throw Exception("Unexpected direction $direction")
            } to decodedDistance
        }


    private fun getDigSites(lines: List<String>): List<Pair<DIRECTION, Int>> =
        lines.map {
            val (direction, distance, color) = it.split(" ")
            when (direction) {
                "U" -> DIRECTION.UP
                "R" -> DIRECTION.RIGHT
                "D" -> DIRECTION.DOWN
                "L" -> DIRECTION.LEFT
                else -> throw Exception("Unexpected direction $direction")
            } to distance.toInt()
        }

    private fun shoelace(points: List<Pair<Long, Long>>) =
        points.indices.sumOf {
            val (x1, y1) = points[it]
            val (x2, y2) = if (it + 1 < points.size) {
                points[(it + 1)]
            } else {
                points[0]
            }

            x1 * y2 - y1 * x2
        } / 2
}