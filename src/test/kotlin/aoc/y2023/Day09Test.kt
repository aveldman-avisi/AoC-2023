package aoc.y2023

import aoc.utils.Utils
import org.junit.jupiter.api.Test

class Day09Test {

    fun parseInput(input: String) =
        input.split("\n").map { it.split(" ").map { it.toLong() } }


    fun getDifferences(sequence: List<Long>): List<Long> =
        sequence.mapIndexedNotNull { index, value ->
            if (index < sequence.size - 1) {
                sequence[index + 1] - value
            } else null
        }

    fun getAllDifferences(sequences: List<Long>): List<List<Long>> {
        val allDifferences = mutableListOf(sequences)
        while (!allDifferences.last().all { it == 0L }) {
            allDifferences.add(getDifferences(allDifferences.last()))
        }
        return allDifferences
    }



    @Test
    fun solution1() {
        val input = Utils().readInputsFromResources("y2023", "day09", "txt")
        val sequences = parseInput(input!!)
        val solution = sequences.map {
            val allDifferences = getAllDifferences(it)

            var lastDiff = 0L
            allDifferences.map { it.toMutableList() }.map { differences ->
                differences.add(differences.last() + lastDiff)
                lastDiff = differences.last()
                differences
            }.last().last()
        }
        println(solution.sum())
    }

    @Test
    fun solution2() {
        val input = Utils().readInputsFromResources("y2023", "day09", "txt")
        val sequences = parseInput(input!!)
        val solution = sequences.map {
            val allDifferences = getAllDifferences(it.reversed())

            var lastDiff = 0L
            allDifferences.map { it.toMutableList() }.map { differences ->
                differences.add(differences.last() + lastDiff)
                lastDiff = differences.last()
                differences
            }.last().last()
        }
        println(solution.sum())
    }
}