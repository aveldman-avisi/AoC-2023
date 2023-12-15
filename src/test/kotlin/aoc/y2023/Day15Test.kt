package aoc.y2023

import aoc.utils.Utils
import org.junit.jupiter.api.Test

class Day15Test {

    data class Lens(
        val label: String,
        var length: Int
    )

    private fun calcCode(step: List<Char>): Long {
        var current = 0L
        step.forEach {
            current += it.toLong()
            current *= 17
            current %= 256
        }
        return current
    }

    @Test
    fun solution1() {
        val input = Utils().readInputsFromResources("y2023", "day15", "txt")!!
        val inputChars = input.split(",").map { it.map { it } }

        val result = inputChars.map { step ->
            calcCode(step)
        }.sum()

        println("Solution: $result")
    }

    @Test
    fun solution2() {
        val input = Utils().readInputsFromResources("y2023", "day15", "txt")!!
        val inputChars = input.split(",").map { it.map { it } }
        val boxes = MutableList(256) { mutableListOf<Lens>() }
        inputChars.map { step ->
            if (step.last().isDigit()) {
                val label = step.subList(0, step.indexOf('='))
                val box = calcCode(label).toInt()
                val length = step.subList(step.indexOf('=') + 1, step.size).joinToString("").toInt()
                val index = boxes[box].indexOfFirst { it.label == label.joinToString("") }
                if (index < 0) {
                    boxes[box].add(Lens(label.joinToString(""), length))
                } else {
                    boxes[box][index].length = length
                }

            } else {
                val label = step.subList(0, step.indexOf('-'))
                val box = calcCode(label).toInt()
                boxes[box].removeIf { it.label == label.joinToString("") }

            }
        }
        val focusingPower = boxes.mapIndexed { boxIndex, box ->
            box.mapIndexed { lensIndex, lens ->
                (1 + boxIndex) * (lensIndex + 1) * lens.length
            }.sum()
        }.sum()

        println("Solution: $focusingPower")

    }
}