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

fun List<Long>.lcm(): Long =
    (1..<this.size).fold(this[0]) { acc, num -> findLCM(acc, this[num]) }

fun findLCM(a: Long, b: Long): Long {
    val larger = if (a > b) a else b
    val maxLcm: Long = a * b
    var lcm = larger
    while (lcm <= maxLcm) {
        if (lcm % a == 0L && lcm % b == 0L) {
            return lcm
        }
        lcm += larger
    }
    return maxLcm
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