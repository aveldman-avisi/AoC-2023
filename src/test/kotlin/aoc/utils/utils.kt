package aoc.utils

class Utils {
    fun readInputsFromResources(year: String, day: String, extension: String) =
        this::class.java.getResource("/inputs/${year}/${day}.${extension}")?.readText()
}
