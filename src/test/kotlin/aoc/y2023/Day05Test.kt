package aoc.y2023

import aoc.utils.Utils
import org.junit.jupiter.api.Test

class Day05Test {

    data class Map(
        val destination: Long,
        val source: Long,
        val range: Long
    )

    private fun getSeedRanges(seedString: String): List<Pair<Long, Long>> {
        val seeds = seedString.substringAfter(": ").split(" ")
        val seedRanges = mutableListOf<Pair<Long, Long>>()
        for (index in seeds.indices) {
            if (index % 2 == 0 ) {
                seedRanges.add(seeds[index].toLong() to seeds[index+1].toLong())
            }
        }
        return seedRanges
    }

    private fun parsePart2Input(input: String): Pair<List<Pair<Long, Long>>, List<List<Map>>>  {
        var seedNumbers: List<Pair<Long, Long>> = emptyList()
        val maps: MutableList<List<Map>> = mutableListOf()
        input.split("\n\n").map {
            if(it.startsWith("seeds: ")) {
                seedNumbers = getSeedRanges(seedString = it)
            } else {
                val map = it.split("\n").toMutableList()
                map.removeAt(0)
                maps.add(map.map {
                    val values = it.split(" ")
                    Map(
                        destination = values[0].toLong(),
                        source = values[1].toLong(),
                        range = values[2].toLong()
                    )
                })
            }
        }
        return seedNumbers to maps
    }


    private fun parseInput(input: String): Pair<List<Long>, List<List<Map>>> {
        var seedNumbers: List<Long> = emptyList()
        val maps: MutableList<List<Map>> = mutableListOf()
        input.split("\n\n").map {
            if(it.startsWith("seeds: ")) {
                seedNumbers = it.substringAfter(": ").split(" ").map { it.toLong() }
            } else {
                val map = it.split("\n").toMutableList()
                map.removeAt(0)
                maps.add(map.map {
                    val values = it.split(" ")
                    Map(
                        destination = values[0].toLong(),
                        source = values[1].toLong(),
                        range = values[2].toLong()
                    )
                })
            }
        }
        return seedNumbers to maps
    }

    @Test
    fun solution1() {
        val input = Utils().readInputsFromResources("y2023", "day05", "txt")
        val seedNumbersToMaps: Pair<List<Long>, List<List<Map>>> = parseInput(input!!)

        val results = seedNumbersToMaps.first.map { seed ->
            var currentValue = seed

            seedNumbersToMaps.second.forEach { map ->
                val validMappings = map.filter { mapRow ->
                    currentValue in mapRow.source..< (mapRow.source + mapRow.range)
                }

                validMappings.forEach { mapping ->
                    currentValue = currentValue - mapping.source + mapping.destination
                }
            }

            currentValue
        }

        println("Solution: ${results.min()}")
    }

    @Test
    fun solution2() {
        val input = Utils().readInputsFromResources("y2023", "day05", "txt")
        val seedNumbersToMaps: Pair<List<Pair<Long, Long>>, List<List<Map>>> = parsePart2Input(input!!)


        var lowestValue = Long.MAX_VALUE
        seedNumbersToMaps.first.forEach { seedRange ->
            (seedRange.first..<seedRange.first + seedRange.second).forEach { seed ->
                var currentValue = seed
                seedNumbersToMaps.second.forEach { map ->
                    val validMappings = map.filter { mapRow ->
                        currentValue in mapRow.source..< (mapRow.source + mapRow.range)
                    }

                    validMappings.forEach { mapping ->
                        currentValue = currentValue - mapping.source + mapping.destination
                    }
                }
                if (currentValue < lowestValue) {
                    lowestValue = currentValue
                }
            }
        }
    }
}