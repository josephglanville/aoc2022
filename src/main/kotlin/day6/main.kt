package day6

import util.resAsText

fun findMarker(input: String, markerLength: Int) =
    input.windowed(markerLength).indexOfFirst { w -> w.toSet().size == w.length } + markerLength

fun main() {
    val testInput = resAsText(6, "test.txt").lines()
    val actualInput = resAsText(6, "input.txt")
    check(testInput.map { findMarker(it, 4) }.toTypedArray().contentEquals(arrayOf(7, 5, 6, 10, 11)))
    check(testInput.map { findMarker(it, 14) }.toTypedArray().contentEquals(arrayOf(19, 23, 23, 29, 26)))

    println(findMarker(actualInput, 4))
    println(findMarker(actualInput, 14))
}