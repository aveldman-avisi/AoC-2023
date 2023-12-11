package aoc.y2023

import aoc.utils.Utils
import org.junit.jupiter.api.Test
import kotlin.math.abs
import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.min

class Day11Test {

    data class Map(
        val space: List<List<Char>>,
        val emptyCols: List<Int>,
        val emptyRows: List<Int>
    )

    private fun parseInput1(input: String): List<List<Char>> {
        val space = mutableListOf<MutableList<Char>>()
        val splitInput = input.split("\n")
        splitInput.forEach {
            val bytes = it.map { it }
            if (bytes.all { it == '.' }) {
                space.add(bytes.toMutableList())
            }
            space.add(bytes.toMutableList())
        }

        var addedLines = 0
        for (xCol in splitInput.first().indices) {
            var allEmpty = true

            for (yCol in splitInput.indices) {
                if (splitInput[yCol][xCol] == '#') {
                    allEmpty = false
                }
            }
            if (allEmpty) {
                space.forEach {
                    it.add(xCol + addedLines, '.')
                }
                addedLines++
            }
        }

        space.forEach { println(it.joinToString("")) }

        return space
    }

    private fun parseInput2(input: String): Map {
        val splitInput = input.split("\n")
        val emptyCols = mutableListOf<Int>()
        val emptyRows = mutableListOf<Int>()

        splitInput.mapIndexed { index, row ->
            val bytes = row.map { it }
            if (bytes.all { it == '.' }) {
                emptyRows.add(index)
            }
        }

        for (xCol in splitInput.first().indices) {
            var allEmpty = true

            for (yCol in splitInput.indices) {
                if (splitInput[yCol][xCol] == '#') {
                    allEmpty = false
                }
            }
            if (allEmpty) {
                emptyCols.add(xCol)
            }
        }
        return Map(splitInput.map { it.map { it } }, emptyCols, emptyRows)
    }

    @Test
    fun solution1() {
        val input = Utils().readInputsFromResources("y2023", "day11", "txt")
        val space = parseInput1(input!!)

        val galaxies = mutableListOf<Pair<Int, Int>>()

        space.mapIndexed { yIndex, row ->
            row.mapIndexed { xIndex, _ ->
                if (space[yIndex][xIndex] == '#') {
                    galaxies.add(yIndex to xIndex)
                }
            }
        }

        val result = galaxies.sumOf { firstGalaxy ->
            galaxies.sumOf { secondGalaxy ->
                if (firstGalaxy == secondGalaxy) {
                    0
                }
                val x1 = min(firstGalaxy.second, secondGalaxy.second)
                val y1 = min(firstGalaxy.first, secondGalaxy.first)
                val x2 = max(firstGalaxy.second, secondGalaxy.second)
                val y2 = max(firstGalaxy.first, secondGalaxy.first)

                abs(x1 - x2) + abs (y1 - y2)
            }
        }

        println("Solution: ${result.absoluteValue / 2}")
    }

    @Test
    fun solution2() {
        val input = Utils().readInputsFromResources("y2023", "day11", "txt")
        val (space, emptyCols, emptyRows) = parseInput2(input!!)
        val galaxies = mutableListOf<Pair<Int, Int>>()

        space.mapIndexed { yIndex, row ->
            row.mapIndexed { xIndex, _ ->
                if (space[yIndex][xIndex] == '#') {
                    galaxies.add(yIndex to xIndex)
                }
            }
        }

        val result = galaxies.sumOf { firstGalaxy ->
            galaxies.sumOf { secondGalaxy ->
                if (firstGalaxy == secondGalaxy) {
                    0L
                } else {
                    val x1 = min(firstGalaxy.second, secondGalaxy.second)
                    val y1 = min(firstGalaxy.first, secondGalaxy.first)
                    val x2 = max(firstGalaxy.second, secondGalaxy.second)
                    val y2 = max(firstGalaxy.first, secondGalaxy.first)


                    var temp = (abs(x1 - x2) + abs(y1 - y2)).toLong()

                    temp += (1000000L - 1L) * (emptyCols.filter { it in x1..<x2 }.size)
                    temp += (1000000L - 1L) * (emptyRows.filter { it in y1..<y2 }.size)

                    temp
                }
            }
        }
        println("Solution: ${result.absoluteValue / 2}")
    }
}