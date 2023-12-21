package aoc.y2023

import aoc.utils.Utils
import org.junit.jupiter.api.Test

class Day19Test {

    data class Workflow(
        val id: String,
        val parts: List<String>,
        val operations: List<String>,
        val values: List<Int>,
        val destinations: List<String>
    )

    data class Ratings(
        var x: List<Int>,
        var m: List<Int>,
        var a: List<Int>,
        var s: List<Int>
    )

    @Test
    fun solution1() {
        val input = Utils().readInputsFromResources("y2023", "day19", "txt")!!
        val workflows = parseInputToWorkflows(input.substringBefore("\n\n").split("\n"))
        val partRating = parseInputToPartRatings(input.substringAfter("\n\n").split("\n"))

        val solution = partRating.map {
            if (putInWorkflow(it, workflows, "in") == "A") {
                it.sumOf { it.second }
            } else 0
        }.sum()

        println("Solution: $solution")
    }

    private fun backwardsSearch(ratings: Ratings, workflows: List<Workflow>, currentPos: String, previousPos: String) {
        val wf = workflows.first { it.id == currentPos }

        val indexOfDes = wf.destinations.indexOf(previousPos)
        (0..indexOfDes).map { index ->
            val des = wf.destinations[index]
            // lijsten inkrimpen totdat een van de eisen matched. Als niks matched ga je naar de laatste
            if (des == previousPos) {
                if (index < wf.parts.size) {
                    val part = wf.parts[index]
                    val value = wf.values[index]
                    val operation = wf.operations[index]

                    when (part) {
                        "x" -> ratings.x = ratings.x.filter {
                            when (operation) {
                                "<" -> it < value
                                ">" -> it > value
                                else -> throw Exception()
                            }
                        }.toMutableList()

                        "m" -> ratings.m = ratings.m.filter {
                            when (operation) {
                                "<" -> it < value
                                ">" -> it > value
                                else -> throw Exception()
                            }
                        }.toMutableList()

                        "a" -> ratings.a = ratings.a.filter {
                            when (operation) {
                                "<" -> it < value
                                ">" -> it > value
                                else -> throw Exception()
                            }
                        }.toMutableList()

                        "s" -> ratings.s = ratings.s.filter {
                            when (operation) {
                                "<" -> it < value
                                ">" -> it > value
                                else -> throw Exception()
                            }
                        }.toMutableList()

                        else -> throw Exception()
                    }
                }

                val nextWfs = workflows.filter { it.destinations.contains(wf.id) }
                nextWfs.map { backwardsSearch(ratings, workflows, it.id, wf.id) }
            } else {
                val part = wf.parts[index]
                val value = wf.values[index]
                val operation = wf.operations[index]

                when (part) {
                    "x" -> ratings.x = ratings.x.filter {
                        when (operation) {
                            "<" -> it > value
                            ">" -> it < value
                            else -> throw Exception()
                        }
                    }.toMutableList()

                    "m" -> ratings.m = ratings.m.filter {
                        when (operation) {
                            "<" -> it > value
                            ">" -> it < value
                            else -> throw Exception()
                        }
                    }.toMutableList()

                    "a" -> ratings.a = ratings.a.filter {
                        when (operation) {
                            "<" -> it > value
                            ">" -> it < value
                            else -> throw Exception()
                        }
                    }.toMutableList()

                    "s" -> ratings.s = ratings.s.filter {
                        when (operation) {
                            "<" -> it > value
                            ">" -> it < value
                            else -> throw Exception()
                        }
                    }.toMutableList()

                    else -> throw Exception()
                }
            }
        }
    }


    @Test
    fun solution3() {
        val input = Utils().readInputsFromResources("y2023", "day19", "txt")!!
        val workflows = parseInputToWorkflows(input.substringBefore("\n\n").split("\n"))


        val endings = workflows.filter { it.destinations.contains("A") }
        val solution = endings.map {
            val (x,m,a,s) = (0..3).map { (1..4000).toMutableList() }
            val ratings = Ratings(x,m,a,s)
            backwardsSearch(ratings, workflows, it.id, "A")
            ratings.x.size.toLong() * ratings.m.size * ratings.a.size * ratings.s.size
        }

        println(solution.sum())
//        println(solution.sumOf { it.x.size.toLong() * it.m.size * it.a.size * it.s.size })
//        Expected 167409079868000
//        Actual   150488399449662
    }

    private fun putInWorkflow(partRatings: List<Pair<String, Int>>, workflows: List<Workflow>, currentPos: String): String {
        val wf = workflows.first { it.id == currentPos }

        val next = wf.parts.indices.map { index ->
            val part = wf.parts[index]
            val value = wf.values[index]
            val operation = wf.operations[index]

            val partRating = partRatings.first { it.first == part }

            val passed = when (operation) {
                ">" -> partRating.second > value
                "<" -> partRating.second < value
                else -> throw Exception()
            }

            if (passed) wf.destinations[index] else null
        }.firstNotNullOfOrNull { it } ?: wf.destinations.last()

        return when (next) {
            "A" -> "A"
            "R" -> "R"
            else -> putInWorkflow(partRatings, workflows, next)
        }
    }


    private fun parseInputToWorkflows(input: List<String>) =
        input.map {
            val id = it.substringBefore("{")
            val operation = it.substringAfter("{").removeSuffix("}")
            val operationParts = operation.split(",")

            val parts = mutableListOf<String>()
            val operations = mutableListOf<String>()
            val values = mutableListOf<Int>()
            val destinations = mutableListOf<String>()

            operationParts.forEach { operation ->
                if (operation.contains(":")) {
                    if (operation.contains("<")) {
                        parts.add(operation.substringBefore("<"))
                        values.add(operation.substringAfter("<").substringBefore(":").toInt())
                        operations.add("<")
                        destinations.add(operation.substringAfter(":"))
                    } else {
                        parts.add(operation.substringBefore(">"))
                        values.add(operation.substringAfter(">").substringBefore(":").toInt())
                        operations.add(">")
                        destinations.add(operation.substringAfter(":"))
                    }
                } else {
                    destinations.add(operation)
                }
            }

            Workflow(id, parts, operations, values, destinations)
        }

    private fun parseInputToPartRatings(input: List<String>): List<List<Pair<String, Int>>> =
        input.map {
            it.drop(1).dropLast(1).split(",").map { partRating ->
                val part = partRating[0].toString()
                val rating = partRating.substringAfter("=").toInt()
                part to rating
            }
        }
}