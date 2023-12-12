package aoc.utils

class Utils {
    fun readInputsFromResources(year: String, day: String, extension: String) =
        this::class.java.getResource("/inputs/${year}/${day}.${extension}")?.readText()
}

fun <T> List<T>.getIndicesOf(element: T) =
    this.mapIndexedNotNull { index, item ->
        if (item == element) {
            index
        } else null
    }