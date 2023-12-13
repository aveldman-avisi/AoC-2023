package aoc.y2023

import aoc.utils.Utils
import aoc.utils.transpose
import org.junit.jupiter.api.Test

class Day13Test {

    private fun getLineDifference(lines: List<List<Int>>, left: Int, right: Int, stepSize: Int): Int {
        var sumXOR = 0
        if (left >= 0 && right < lines.size) {
            val leftValue = lines[left]
            val rightValue = lines[right]

            sumXOR = leftValue.mapIndexed { index, value ->
                value xor rightValue[index]
            }.sum()

            sumXOR += getLineDifference(lines, left - stepSize, right + stepSize, stepSize)
        }
        return sumXOR
    }

    @Test
    fun solution1() {
        val input = Utils().readInputsFromResources("y2023", "day13", "txt")!!

        val indicesOfHorizontalMirrors = mutableListOf<Int>()
        val indicesOfVerticalMirrors = mutableListOf<Int>()

        input.split("\n\n").map { pattern ->
            val lines = pattern.split("\n").map { it.map { if (it == '#') 1 else 0 } }

            indicesOfHorizontalMirrors.addAll(lines.mapIndexedNotNull { index, _ ->
                if (index != 0) {
                    if (getLineDifference(lines, left = index - 1, right = index, stepSize = 1) == 0) {
                        index
                    } else null
                } else null
            })

            val transposedList = lines.map{ it.map { it } }.transpose()
            indicesOfVerticalMirrors.addAll(transposedList.mapIndexedNotNull { index, _ ->
                if (index != 0) {
                    if (getLineDifference(transposedList, left = index - 1, right = index, stepSize = 1) == 0) {
                        index
                    } else null
                } else null
            })
        }

        println("Solution: ${indicesOfVerticalMirrors.sum() + 100 * indicesOfHorizontalMirrors.sum()}")
    }

    @Test
    fun solution2() {
        val input = Utils().readInputsFromResources("y2023", "day13", "txt")!!

        val indicesOfHorizontalMirrors = mutableListOf<Int>()
        val indicesOfVerticalMirrors = mutableListOf<Int>()

        input.split("\n\n").map { pattern ->
            val lines = pattern.split("\n").map { it.map { if (it == '#') 1 else 0 } }

            indicesOfHorizontalMirrors.addAll(lines.mapIndexedNotNull { index, _ ->
                if (index != 0) {
                    val difference = getLineDifference(lines, left = index - 1, right = index, stepSize = 1)
                    if (difference == 1) {
                        index
                    } else null
                } else null
            })

            val transposedList = lines.map{ it.map { it } }.transpose()
            indicesOfVerticalMirrors.addAll(transposedList.mapIndexedNotNull { index, _ ->
                if (index != 0) {
                    val difference = getLineDifference(transposedList, left = index - 1, right = index, stepSize = 1)
                    if (difference == 1) {
                        index
                    } else null
                } else null
            })
        }

        println("Solution: ${indicesOfVerticalMirrors.sum() + 100 * indicesOfHorizontalMirrors.sum()}")
    }
}