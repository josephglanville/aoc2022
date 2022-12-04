package day4

import util.resAsText

fun asRanges(str: String) : List<IntRange> =
    str.split(",").map { range -> range.split("-").map { it.toInt() } }.map { IntRange(it[0], it[1]) }

fun containedRanges(pairs: List<List<IntRange>>) =
    pairs.filter { it[0].containedIn(it[1]) || it[1].containedIn(it[0]) }

fun ClosedRange<Int>.containedIn(other: ClosedRange<Int>) : Boolean =
    other.start >= start && other.endInclusive <= endInclusive

fun overlappingRanges(pairs: List<List<IntRange>>) =
    pairs.filter { it[0].overlaps(it[1]) }

fun ClosedRange<Int>.overlaps(other: ClosedRange<Int>) : Boolean =
    start <= other.endInclusive && endInclusive >= other.start

fun main() {
    val testPairs = resAsText(4, "test.txt").lines().map { line -> asRanges(line) }
    val actualPairs = resAsText(4, "input.txt").lines().map { line -> asRanges(line) }

    check(containedRanges(testPairs).count() == 2)
    println(containedRanges(actualPairs).count())

    check(overlappingRanges(testPairs).count() == 4)
    println(overlappingRanges(actualPairs).count())
}