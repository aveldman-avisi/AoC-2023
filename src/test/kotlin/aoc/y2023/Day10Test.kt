package aoc.y2023

import aoc.utils.Utils
import org.junit.jupiter.api.Test

class Day10Test {

    fun parseInput(input: String) =
        input.split("\n").map { row -> row.map { it } }


//| is a vertical pipe connecting north and south.
//- is a horizontal pipe connecting east and west.
//L is a 90-degree bend connecting north and east.
//J is a 90-degree bend connecting north and west.
//7 is a 90-degree bend connecting south and west.
//F is a 90-degree bend connecting south and east.
//. is ground; there is no pipe in this tile.
//S is the starting position of the animal; there is a pipe on this tile, but your sketch doesn't show what shape the pipe has.

    private fun determineStartPipe(map: List<List<Char>>, startCoord: Pair<Int, Int>): Char {
        val (startY, startX) = startCoord
        val northChar = if (startY - 1 >= 0) map[startY - 1][startX] else '.'
        val eastChar = if (startX + 1 < map.first().size) map[startY][startX + 1] else '.'
        val southChar = if (startY + 1 < map.size) map[startY + 1][startX] else '.'
        val westChar = if (startX - 1 >= 0) map[startY][startX - 1] else '.'

        val possibleChars = mutableListOf('|', '-', 'L', 'J', '7', 'F')
        if (northChar != '|' && northChar != '7' && northChar != 'F') {
            possibleChars.removeAll(listOf('|', 'L', 'J'))
        }
        if (eastChar != 'J' && eastChar != '-' && eastChar != '7') {
            possibleChars.removeAll(listOf('L', 'F', '-'))
        }
        if (southChar != '|' && southChar != 'L' && southChar != 'J') {
            possibleChars.removeAll(listOf('|', '7', 'F'))
        }
        if (westChar != '-' && westChar != 'L' && westChar != 'F') {
            possibleChars.removeAll(listOf('-', 'J', '7'))
        }
        return possibleChars.first()
    }

    private fun findStartingCoordinates(map: List<List<Char>>): Pair<Int, Int> {
        val y = map.indexOfFirst { it.contains('S') }
        return y to map[y].indexOf('S')
    }

    private fun nextTile(map: List<List<Char>>, currentTileCoord: Pair<Int, Int>, previousTileCoord: Pair<Int, Int>): Pair<Int, Int> {
        val (currentY, currentX) = currentTileCoord
        val (previousY, previousX) = previousTileCoord
        val currentTileChar = map[currentY][currentX]
        val nextTile = when (currentTileChar) {
            '|' -> if (previousY == currentY + 1) { currentY - 1 to currentX } else { currentY + 1 to currentX }
            '-' -> if (previousX == currentX + 1) { currentY to currentX - 1 } else { currentY to currentX + 1 }
            'L' -> if (previousY == currentY - 1) { currentY to currentX + 1 } else { currentY - 1 to currentX }
            'J' -> if (previousY == currentY - 1) { currentY to currentX - 1 } else { currentY - 1 to currentX }
            '7' -> if (previousY == currentY + 1) { currentY to currentX - 1 } else { currentY + 1 to currentX }
            'F' -> if (previousY == currentY + 1) { currentY to currentX + 1 } else { currentY + 1 to currentX }
            else -> throw RuntimeException("No next tile found. Character was $currentTileChar at $currentY,$currentX")
        }

        return nextTile
    }

    @Test
    fun solution1(): List<Pair<Int, Int>> {
        val input = Utils().readInputsFromResources("y2023", "day10", "txt")
        val map = parseInput(input!!).map { it.toMutableList() }.toMutableList()
        val startingCoords = findStartingCoordinates(map)
        map[startingCoords.first][startingCoords.second] = determineStartPipe(map, startingCoords)

        val visitedTiles = mutableListOf(startingCoords)
        visitedTiles.add(nextTile(map, startingCoords, 1 to 0))

        while (visitedTiles.last() != startingCoords) {
            visitedTiles.add(nextTile(map, visitedTiles.last(), visitedTiles[visitedTiles.lastIndex - 1]))
        }

        println("Visited tiles: ${visitedTiles.size}")
        println("Furthest from start: ${visitedTiles.size / 2}")
        return visitedTiles
    }

    @Test
    fun solution2() {
        val input = Utils().readInputsFromResources("y2023", "day10", "txt")
        val map = parseInput(input!!).map { it.toMutableList() }.toMutableList()
        val startingCoords = findStartingCoordinates(map)
        map[startingCoords.first][startingCoords.second] = determineStartPipe(map, startingCoords)
        val tilesInLoop = solution1()
        val tempMap = map.mapIndexed { indexY, row ->
            row.mapIndexed { indexX, tile ->
                if (tilesInLoop.contains(indexY to indexX)) {
                    tile
                } else '.'
            }.toMutableList()
        }.toMutableList()

        val newMap = tempMap.map {
            it.joinToString("")
                .replace("-", "")
                .replace("L7", "|")
                .replace("FJ", "|")
                .replace("LJ", "||")
                .replace("F7", "||")
                .toList()
        }

        val solution = newMap.map { row ->
            row.filterIndexed { index, tile ->
                if (tile == '.') {
                    var wallCount = 0
                    for (counter in 0..<index) {
                        if (row[counter] == '|') wallCount++
                    }
                    wallCount % 2 == 1
                }  else false
            }.count()
        }.sum()
        println("Solution: $solution")
    }
}