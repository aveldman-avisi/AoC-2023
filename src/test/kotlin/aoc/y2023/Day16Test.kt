package aoc.y2023

import aoc.utils.Utils
import org.junit.jupiter.api.Test

class Day16Test {

    enum class DIRECTION { UP, RIGHT, DOWN, LEFT }

    private fun bounceRay(
        map: List<String>,
        startX: Int,
        startY: Int,
        direction: DIRECTION,
        visitedTiles: MutableSet<Triple<Int, Int, DIRECTION>>
    ): Set<Pair<Int, Int>> {

        if (when (direction) {
            DIRECTION.UP -> startY == -1
            DIRECTION.RIGHT -> startX == map[0].length
            DIRECTION.DOWN -> startY == map.size
            DIRECTION.LEFT -> startX == -1
        }) {
            return emptySet()
        }

        val result = mutableSetOf<Pair<Int, Int>>()
        var x = startX
        var y = startY
        var currentDirection = direction
        do {
            result += x to y
            val currentTile = map[y][x]
            if (currentTile != '.') {
                if (visitedTiles.contains(Triple(x, y, currentDirection))) return result
                visitedTiles.add(Triple(x, y, currentDirection))
                when (currentDirection) {
                    DIRECTION.UP -> {
                        when (currentTile) {
                            '\\' -> currentDirection = DIRECTION.LEFT
                            '/' -> currentDirection = DIRECTION.RIGHT
                            '-' -> {
                                currentDirection = DIRECTION.LEFT
                                result += bounceRay(map, x + 1, y, DIRECTION.RIGHT, visitedTiles)
                            }
                        }
                    }
                    DIRECTION.RIGHT -> {
                        when (currentTile) {
                            '\\' -> currentDirection = DIRECTION.DOWN
                            '/' -> currentDirection = DIRECTION.UP
                            '|' -> {
                                currentDirection = DIRECTION.UP
                                result += bounceRay(map, x, y + 1, DIRECTION.DOWN, visitedTiles)
                            }
                        }
                    }
                    DIRECTION.DOWN -> {
                        when (currentTile) {
                            '\\' -> currentDirection = DIRECTION.RIGHT
                            '/' -> currentDirection = DIRECTION.LEFT
                            '-' -> {
                                currentDirection = DIRECTION.LEFT
                                result += bounceRay(map, x + 1, y, DIRECTION.RIGHT, visitedTiles)
                            }
                        }
                    }
                    DIRECTION.LEFT -> {
                        when (currentTile) {
                            '\\' -> currentDirection = DIRECTION.UP
                            '/' -> currentDirection = DIRECTION.DOWN
                            '|' -> {
                                currentDirection = DIRECTION.UP
                                result += bounceRay(map, x, y + 1, DIRECTION.DOWN, visitedTiles)
                            }
                        }
                    }

                }
            }

            val move = when (currentDirection) {
                DIRECTION.UP -> x to y - 1
                DIRECTION.RIGHT -> x + 1 to y
                DIRECTION.DOWN -> x to y + 1
                DIRECTION.LEFT -> x - 1 to y
            }

            x = move.first
            y = move.second
        } while (!when (currentDirection) {
                DIRECTION.UP -> y == -1
                DIRECTION.RIGHT -> x == map[0].length
                DIRECTION.DOWN -> y == map.size
                DIRECTION.LEFT -> x == -1
        })
        return result
    }


    @Test
    fun solution1() {
        val input = Utils().readInputsFromResources("y2023", "day16", "txt")!!
        val litFields = bounceRay(input.split("\n"), 0, 0, DIRECTION.RIGHT, mutableSetOf()).size
        println("Solution: $litFields")
    }

    @Test
    fun solution2() {
        val input = Utils().readInputsFromResources("y2023", "day16", "txt")!!
        val map = input.split("\n")
        var result = 0
        map[0].indices.forEach { x ->
            val tempTop = bounceRay(map, x, 0, DIRECTION.DOWN, mutableSetOf()).size
            val tempBottom = bounceRay(map, x, map.size -1, DIRECTION.UP, mutableSetOf()).size

            result = maxOf(maxOf(tempTop, result), tempBottom)
        }

        map.indices.forEach { y ->
            val tempLeft = bounceRay(map, 0, y, DIRECTION.RIGHT, mutableSetOf()).size
            val tempRight = bounceRay(map, map[0].length-1, y, DIRECTION.LEFT, mutableSetOf()).size

            result = maxOf(maxOf(tempLeft, result), tempRight)
        }

        println("Solution: $result")
    }
}