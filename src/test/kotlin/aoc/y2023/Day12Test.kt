package aoc.y2023

import aoc.utils.Utils
import aoc.utils.getIndicesOf
import org.junit.jupiter.api.Test

class Day12Test {

    data class SpringRow(
        var springs: List<Char>,
        var groups: List<Int>

    )

    private fun parseInput(input: String): List<SpringRow> =
        input.split("\n").map {
            SpringRow(
                springs = it.substringBefore(" ").map { it },
                groups = it.substringAfter(" ").split(",").map { it.toInt() }
            )
        }

    private fun unfoldSprings(springs: List<Char>): List<Char> {
        val mutableSprings = springs.toMutableList()
        repeat(4) {
            mutableSprings.add('?')
            mutableSprings.addAll(springs)
        }
        return mutableSprings
    }

    private fun unfoldGroups(groups: List<Int>): List<Int> {
        val mutableGroups = groups.toMutableList()
        repeat(4) {
            mutableGroups.addAll(groups)
        }
        return groups
    }

    private fun tryCombinations(springs: List<Char>, corruptedIndices: List<Int>, currentIndex: Int, groups: List<Int>, countBroken: Int): List<Boolean> {
        if (currentIndex == corruptedIndices.size) {
            return listOf(combinationIsValid(springs, groups))
        }

        val one = springs.toMutableList()
        one[corruptedIndices[currentIndex]] = '.'
        val two = springs.toMutableList()
        two[corruptedIndices[currentIndex]] = '#'

        val combinations = mutableListOf<List<Boolean>>()
        if (springs.size - currentIndex >= groups.sum() - countBroken) {
            combinations.add(tryCombinations(one, corruptedIndices, currentIndex+1, groups, countBroken))
        }

        if (countBroken < groups.sum()) {
            combinations.add(tryCombinations(two, corruptedIndices, currentIndex+1, groups, countBroken+1))
        }

        return combinations.flatten()
    }

    private fun combinationIsValid(springs: List<Char>, groups: List<Int>): Boolean {
        val groupLengths = mutableListOf<Int>()
        var groupLength = 0
        springs.forEach {
            if (it == '#') {
                groupLength++
            } else {
                if (groupLength > 0) {
                    groupLengths.add(groupLength)
                }
                groupLength = 0
            }
        }
        if (groupLength > 0) { groupLengths.add(groupLength) }

        if (groupLengths.size != groups.size) {
            return false
        }

        val result = groupLengths.mapIndexed { index, group ->
            groupLengths[index] == groups[index]
        }.all { it }

        return result
    }

    private val cache = hashMapOf<Pair<String, List<Int>>, Long>()
    private fun count(config: String, groups: List<Int>): Long {
        if (groups.isEmpty()) return if ("#" in config) 0 else 1
        if (config.isEmpty()) return 0

        return cache.getOrPut(config to groups) {
            var result = 0L
            if (config.first() in ".?")
                result += count(config.drop(1), groups)
            if (config.first() in "#?" && groups.first() <= config.length && "." !in config.take(groups.first()) && (groups.first() == config.length || config[groups.first()] != '#'))
                result += count(config.drop(groups.first() + 1), groups.drop(1))
            result
        }
    }


    @Test
    fun solution1() {
        val input = Utils().readInputsFromResources("y2023", "day12", "txt")
        val springRows = parseInput(input!!)

        val validCombinations = springRows.map {
            val indices = it.springs.getIndicesOf('?')
            tryCombinations(it.springs, indices, 0, it.groups, 0)
        }.flatten().count { it }

        println("Solution: $validCombinations")
    }


    @Test
    fun solution2() {
        val input = Utils().readInputsFromResources("y2023", "day12", "txt")
        val lines = input!!.split("\n")
        val result = lines.sumOf { it.split(" ").let {
            count(
                "${it.first()}?".repeat(5).dropLast(1),
                "${it[1]},".repeat(5).split(",").filter { it.isNotBlank() }.map(String::toInt)
            )
        }}
        println("Solution: $result")
    }

}