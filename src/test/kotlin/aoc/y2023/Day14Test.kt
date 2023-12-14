package aoc.y2023

import aoc.utils.Utils
import aoc.utils.transpose
import org.junit.jupiter.api.Test

class Day14Test {

    private fun List<List<Char>>.tilt(): List<List<Char>> {
        val newList = this.map { it.toMutableList() }.toMutableList()

        for (x in this.first().indices) {
            for (y in indices) {
                var yd = y
                while (yd > 0) {
                    if (newList[yd][x] == 'O') {
                        if (newList[yd - 1][x] == '.') {
                            newList[yd - 1][x] = 'O'
                            newList[yd][x] = '.'
                        }
                    }
                    yd--
                }
            }
        }
        return newList
    }

    @Test
    fun solution1() {
        val input = Utils().readInputsFromResources("y2023", "day14", "txt")!!
        val resultList = input.split("\n").map { it.map { it } }.tilt()

        val solution = resultList.mapIndexed { index, row ->
            row.count { it == 'O' } * (resultList.size - index)
        }.sum()

        println("Solution: $solution")



        println(resultList.joinToString("\n") { it.joinToString("") })
    }

    @Test
    fun solution2() {
        val input = Utils().readInputsFromResources("y2023", "day14", "txt")!!
            .split("\n").map { it.map { it } }

        val resultList = hashMapOf<String, List<List<Char>>>()

        var temp = input
        var repeats = 0
        repeat(1_000) {
            repeats++
            if (resultList.contains(temp.joinToString("\n") { it.joinToString("") })) {
                temp = resultList[temp.joinToString("\n") { it.joinToString("") }]!!
            } else {
                temp = temp.tilt()
                temp = temp.transpose().tilt().transpose()
                temp = temp.reversed().tilt().reversed()
                temp = temp.transpose().reversed().tilt().reversed().transpose()

                resultList[temp.joinToString("\n") { it.joinToString("") }] = temp
            }
            if (repeats % 1_000_000 == 0) {
                println("At repeat: $repeats")
            }
        }
        val solution = temp.mapIndexed { index, row ->
            row.count { it == 'O' } * (temp.size - index)
        }.sum()
        println("Solution: $solution")

        println(temp.joinToString("\n") { it.joinToString("") })
    }
}