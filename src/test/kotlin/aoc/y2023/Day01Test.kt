package aoc.y2023

import aoc.utils.Utils
import org.junit.jupiter.api.Test

class Day01Test {
    @Test
    fun solution1() {
        val input = Utils().readInputsFromResources("y2023", "day01", "txt")
        val numbers = input!!.split("\n").map { line ->
            val digits = line.filter { it.isDigit() }
            "${digits.first()}${digits.last()}"
        }
        println("Solution: ${numbers.sumOf { it.toInt() }}")
    }

    @Test
    fun solution2() {
        val input = Utils().readInputsFromResources("y2023", "day01", "txt")
        val digitMap = mapOf(
            "one" to "o1e",
            "two" to "t2o",
            "three" to "t3e",
            "four" to "f4r",
            "five" to "f5e",
            "six" to "s6x",
            "seven" to "s7n",
            "eight" to "e8t",
            "nine" to "n9e"
        )
        val numbers = input!!.split("\n").map { line ->
            // Some written digits overlap, Both should be replaced...
            var newLine = ""
            line.forEach {
                newLine += it
                digitMap.map { (word, digit) -> newLine = newLine.replace(word, digit) }
            }
            val digits = newLine.filter { it.isDigit() }
            "${digits.first()}${digits.last()}"
        }
        println("Solution: ${numbers.sumOf { it.toInt() }}")
    }
}