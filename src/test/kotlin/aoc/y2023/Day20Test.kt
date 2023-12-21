package aoc.y2023

import aoc.utils.Utils
import aoc.utils.lcm
import org.junit.jupiter.api.Test
import java.util.LinkedList

class Day20Test {

    companion object {
        var highPulses = 0
        var lowPulses = 0
    }

    abstract class Module(
        open val id: String,
        open var receivers: List<String>
    ) {
        abstract fun receiveMessage(high: Boolean, sender: String, queue: LinkedList<Triple<String,Boolean, String>>)
    }

    data class OutputModule(
        override val id: String,
        override var receivers: List<String>
    ): Module(id, receivers) {
        override fun receiveMessage(high: Boolean, sender: String, queue: LinkedList<Triple<String, Boolean, String>>) {

        }
    }

    data class FlipFlopModule(
        override val id: String,
        var state: Boolean,
        override var receivers: List<String>
    ): Module(id, receivers) {
        override fun receiveMessage(high: Boolean, sender: String, queue: LinkedList<Triple<String,Boolean, String>>) {
            if (!high) {
                state = !state
                if (state) {
                    receivers.forEach {
                        highPulses++
                        queue.add(Triple(id, true, it))
                    }
                } else {
                    receivers.forEach {
                        lowPulses++
                        queue.add(Triple(id, false, it))
                    }
                }

            }
        }
    }

    data class ConjunctionModule(
        override val id: String,
        var receiverStates: MutableMap<String, Boolean>,
        override var receivers: List<String>
    ): Module(id, receivers) {
        override fun receiveMessage(high: Boolean, sender: String, queue: LinkedList<Triple<String,Boolean, String>>) {
            receiverStates[sender] = high

            if (receiverStates.values.all { it }) {
                receivers.forEach {
                    lowPulses++
                    queue.add(Triple(id, false, it))
                }
            } else {
                receivers.forEach {
                    highPulses++
                    queue.add(Triple(id, true, it))
                }
            }
        }
    }

    data class Broadcaster(
        override val id: String,
        override var receivers: List<String>
    ): Module(id, receivers) {
        override fun receiveMessage(high: Boolean, sender: String, queue: LinkedList<Triple<String,Boolean, String>>) {
            receivers.forEach {
                if (high) {
                    highPulses++
                } else {
                    lowPulses++
                }
                queue.add(Triple(id, high, it))
            }
        }
    }

    private fun parseInput(input: List<String>): List<Module> {
        val modules = input.map {
            val id = it.substringBefore(" ->")
            if (id.contains("%")) {
                FlipFlopModule(id.substringAfter("%"), state = false, emptyList())
            } else if (id.contains("&")) {
                ConjunctionModule(id.substringAfter("&"), mutableMapOf(), emptyList())
            } else {
                Broadcaster(id, emptyList())
            }
        }.toMutableList()


        input.map { line ->
            val module = modules.firstOrNull { it.id == line.substringBefore(" ->").drop(1) } ?: modules.first { it.id == "broadcaster" }
            module.receivers = line.substringAfter("-> ").split(", ")
        }

        modules.filterIsInstance<ConjunctionModule>().map { conMod ->
            conMod.receiverStates = modules.filter { it.receivers.contains(conMod.id) }
                .associate { it.id to false }.toMutableMap()
        }


        val distinctReceivers = modules.map { it.receivers }.flatten().distinct()
        val receiversWithoutModule = distinctReceivers.filter { receiver -> !modules.map { module -> module.id }.contains(receiver) }
        receiversWithoutModule.forEach {
            modules.add(OutputModule(it, emptyList()))
        }

        return modules
    }

    @Test
    fun solution() {
        val input = Utils().readInputsFromResources("y2023", "day20", "txt")!!
        val queue = LinkedList<Triple<String, Boolean, String>>()
        val objects = parseInput(input!!.split("\n"))

        val broadcaster = objects.first { it.id == "broadcaster" }
        repeat(1000) {
            broadcaster.receiveMessage(false, "", queue)
            lowPulses++
            while(queue.isNotEmpty()) {
                val (sender, signal, receiver) = queue.pop()
                objects.first { it.id == receiver }.receiveMessage(signal, sender, queue)
            }
        }

        println("High pulses: $highPulses")
        println("Low pulses: $lowPulses")
        println("Result: ${highPulses * lowPulses}")
    }

    @Test
    fun solution2() {
        val input = Utils().readInputsFromResources("y2023", "day20", "txt")!!
        val queue = LinkedList<Triple<String, Boolean, String>>()
        val objects = parseInput(input!!.split("\n"))

        val broadcaster = objects.first { it.id == "broadcaster" }
        var buttonPresses = 0
        var sg = 0L
        var lm = 0L
        var dh = 0L
        var db = 0L

        while (sg == 0L || lm == 0L || dh == 0L || db == 0L) {
            buttonPresses++
            broadcaster.receiveMessage(false, "", queue)
            lowPulses++
            while (queue.isNotEmpty()) {
                val (sender, signal, receiver) = queue.pop()
                if (receiver == "sg" && !signal && sg == 0L) {
                    sg = buttonPresses.toLong()
                }
                if (receiver == "lm" && !signal && lm == 0L) {
                    lm = buttonPresses.toLong()
                }
                if (receiver == "dh" && !signal && dh == 0L) {
                    dh = buttonPresses.toLong()
                }
                if (receiver == "db" && !signal && db == 0L) {
                    db = buttonPresses.toLong()
                }
                objects.first { it.id == receiver }.receiveMessage(signal, sender, queue)
            }
        }

        val solution = listOf(sg, lm, dh, db).lcm()

        println("sg: $sg")
        println("lm: $lm")
        println("dh: $dh")
        println("db: $db")
        println("Solution: $solution")
    }
}