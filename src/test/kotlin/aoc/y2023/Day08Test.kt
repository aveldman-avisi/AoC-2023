package aoc.y2023

import aoc.utils.Utils
import aoc.utils.lcm
import org.junit.jupiter.api.Test

class Day08Test {

    data class Element(
        val id: String,
        var left: Element?,
        var right: Element?
    )

    private fun parseInput(input: String): Pair<String, List<Element>> {
        val lines = input.split("\n")
        val instructions = lines[0]
        val elementsToText = lines.subList(2, lines.size).map {
            Element(id = it.substringBefore(" = "), left = null, right = null) to it.substringAfter("= (").substringBefore(")")
        }

        elementsToText.map { (element, text) ->
            val paths = text.split(", ")
            element.left = elementsToText.first { it.first.id == paths.first() }.first
            element.right = elementsToText.first { it.first.id == paths.last() }.first
        }

        return instructions to  elementsToText.map { it.first }
    }



    @Test
    fun solution1() {
        val input = Utils().readInputsFromResources("y2023", "day08", "txt")
        val (instruction, elements) = parseInput(input!!)

        val endingElement = elements.find { it.id == "ZZZ" }!!
        var currentElement = elements.find { it.id == "AAA" }!!

        var currentInstructionList = instruction.toMutableList()
        var steps = 0
        while(currentElement != endingElement) {
            steps++


            if (currentInstructionList.isEmpty()) {
                currentInstructionList = instruction.toMutableList()
            }
            val currentInstruction = currentInstructionList.removeAt(0)
            currentElement = if (currentInstruction == 'L') {
                currentElement.left!!
            } else {
                currentElement.right!!
            }
        }
        println("Solution: $steps")
    }

    @Test
    fun solution2() {
        val input = Utils().readInputsFromResources("y2023", "day08", "txt")
        val (instruction, elements) = parseInput(input!!)

        val currentElements = elements.filter { it.id.endsWith('A') }

        val stepsPerPath = currentElements.map { element ->
            var currentElement = element
            var steps = 0
            var currentInstructionList = instruction.toMutableList()

            while(!currentElement.id.endsWith('Z')) {
                steps++

                if (currentInstructionList.isEmpty()) {
                    currentInstructionList = instruction.toMutableList()
                }
                val currentInstruction = currentInstructionList.removeAt(0)
                currentElement = if (currentInstruction == 'L') {
                    currentElement.left!!
                } else {
                    currentElement.right!!
                }
            }
            steps
        }

        println("Steps per path: $stepsPerPath")

        val solution = stepsPerPath.map { it.toLong() }.lcm()
        println("Solution: $solution")

    }
}