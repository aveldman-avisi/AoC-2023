package aoc.y2023

import aoc.utils.Utils
import org.junit.jupiter.api.Test
import java.util.*

class Day17Test {

    @Test
    fun solution1() {
        val input = Utils().readInputsFromResources("y2023", "day17", "txt")!!
        val map = input.split("\n").map { it.map { it.toString().toInt() } }
        val solution = findPath(map, 1, 3)
        println("Solution: $solution")
    }

    @Test
    fun solution2() {
        val input = Utils().readInputsFromResources("y2023", "day17", "txt")!!
        val map = input.split("\n").map { it.map { it.toString().toInt() } }
        val solution = findPath(map, 4, 10)
        println("Solution: $solution")
    }


    data class Position(val x: Int, val y: Int)
    data class State(val heatLoss: Int, val position: Position, val direction: Int)

    private fun findPath(data: List<List<Int>>, minLength: Int, maxLength: Int): Int {
        val height = data.size
        val width = data[0].size
        val start = Position(0, 0)
        val end = Position(height - 1, width - 1)
        val visited = mutableMapOf<Pair<Position, Int>, Int>()
        val directions = listOf(Position(0, 1), Position(1, 0), Position(0, -1), Position(-1, 0))
        val queue = PriorityQueue(compareBy<State> { it.heatLoss })

        queue.add(State(0, start, -1))

        while (queue.isNotEmpty()) {
            val (currentHeatLoss, currentPosition, currentDirection) = queue.poll()

            // Niet terug of vooruit
            val allowedDirections = (0..<4).filter { it != currentDirection && (it + 2) % 4 != currentDirection }

            for (direction in allowedDirections) {
                var nextHeatLoss = currentHeatLoss
                // Bereken voor de maximale lengte van rechte pad
                for (step in 1..maxLength) {
                    val nextPosition = Position(
                        currentPosition.x + directions[direction].x * step,
                        currentPosition.y + directions[direction].y * step
                    )
                    if (nextPosition.x in 0..<height && nextPosition.y in 0..<width) {
                        nextHeatLoss += data[nextPosition.x][nextPosition.y]
                        if (nextHeatLoss < visited.getOrDefault(Pair(nextPosition, direction), Int.MAX_VALUE)) {
                            visited[Pair(nextPosition, direction)] = nextHeatLoss
                            // Alleen als de huidige step meer is dan de minimale lengte van een rechte lijn
                            if (step >= minLength) {
                                queue.add(State(nextHeatLoss, nextPosition, direction))
                            }
                        }
                    }
                }
            }

            if (currentPosition == end) {
                return currentHeatLoss
            }
        }

        // 906 blijkbaar niet goed
        return Int.MAX_VALUE
    }
}