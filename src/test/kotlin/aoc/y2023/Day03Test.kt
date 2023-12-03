package aoc.y2023

import aoc.utils.Utils
import org.junit.jupiter.api.Test

class Day03Test {

    data class SpecialCharacterCoord(
        val character: Char,
        val y: Int,
        val x: Int
    )

    private fun parseInputToPartNumbers(input: String) {
        val grid = input.split("\n").map { it.toList() }
        val foundNumbers = mutableListOf<Pair<Int, SpecialCharacterCoord>>()

        grid.forEachIndexed { y, line ->
            var currentNumberValid: Pair<Boolean, SpecialCharacterCoord?> = false to null
            val potentialNumbers = mutableListOf<Char>()
            line.forEachIndexed { x, value ->
                if (value.isDigit()) {
                    potentialNumbers.add(value)
                    val specialCharacterCoord = specialCharacterAdjacent(grid,getAdjacentPositions(grid, y ,x))
                    if (specialCharacterCoord != null) {
                        currentNumberValid = true to specialCharacterCoord
                    }
                }

                if (!value.isDigit() || x == line.size - 1) {
                    if (potentialNumbers.isNotEmpty() && currentNumberValid.first) {
                        foundNumbers.add(String(potentialNumbers.toCharArray()).toInt() to currentNumberValid.second!!)
                    }
                    currentNumberValid = false to null
                    potentialNumbers.clear()
                }
            }
        }

        var totalNumber = 0
        foundNumbers.forEach { firstPos ->
            if (firstPos.second.character == '*') {
                foundNumbers.firstOrNull { secondPos ->
                    firstPos !== secondPos &&
                        firstPos.second.x == secondPos.second.x &&
                        firstPos.second.y == secondPos.second.y
                }.let {pos ->
                    if (pos != null) {
                        totalNumber += pos.first * firstPos.first
                    }
                }
            }
        }
        totalNumber /= 2
        println(totalNumber)
    }


    @Test
    fun solution() {
        val input = Utils().readInputsFromResources("y2023", "day03", "txt")
        parseInputToPartNumbers(input!!)
    }

    private fun isSpecialCharacter(c: Char): Boolean =
        c != ' ' && c != '.' && !c.isDigit()


    private fun specialCharacterAdjacent(grid: List<List<Char>>, adjacentPositions: List<Pair<Int,Int>>): SpecialCharacterCoord? =
        adjacentPositions.firstNotNullOfOrNull {
            val character = grid[it.first][it.second]
            if (isSpecialCharacter(character)) {
                SpecialCharacterCoord(character ,it.first, it.second)
            } else null
        }

    private fun getAdjacentPositions(grid: List<List<Char>>, y: Int, x: Int): List<Pair<Int,Int>> {
        val adjacentPositions = mutableListOf<Pair<Int, Int>>()
        for (deltaY in -1..1) {
            for (deltaX in -1..1) {
                if (deltaX == 0 && deltaY == 0) continue
                val possibleX = x + deltaX
                val possibleY = y + deltaY
                if (possibleX < 0 || possibleY < 0) continue
                if (possibleY >= grid.size || possibleX >= grid[possibleY].size) continue
                adjacentPositions.add(Pair(possibleY, possibleX))
            }
        }
        return adjacentPositions
    }
}