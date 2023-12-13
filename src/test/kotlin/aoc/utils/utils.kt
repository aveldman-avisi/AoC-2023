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

fun <T> List<List<T>>.transpose(): List<List<T>> {
    val cols = this.first().size
    val rows = this.size
    return List(cols) { j ->
        List(rows) { i ->
            this[i][j]
        }
    }
}