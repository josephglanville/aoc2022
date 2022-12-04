package day3

import util.resAsText

typealias Compartment = Set<Int>
typealias Rucksack = Pair<Compartment, Compartment>

fun asRucksack(contents: List<Int>): Rucksack =
    contents.subList(0, contents.size/2).toSet() to contents.subList(contents.size/2, contents.size).toSet()

fun asRucksacks(input: String) : List<Rucksack> =
    input.lines().map { line -> asRucksack(line.map(::asPriority)) }

fun asPriority(item: Char) : Int = if (item.isLowerCase()) item.code-96 else item.code-38

fun prioritySum(rucksacks: List<Rucksack>) : Int = rucksacks.sumOf { (one, two) -> one.intersect(two).first() }

fun badgeSum(rucksacks: List<Rucksack>) = rucksacks.map { it.first + it.second }.chunked(3)
    .map { g -> g.reduce { acc, items -> acc.intersect(items)} }.flatten().sum()

fun main() {
    val testInput = asRucksacks(resAsText(3, "test.txt"))
    val actualInput = asRucksacks(resAsText(3, "input.txt"))

    val testResult = prioritySum(testInput)
    val actualResult = prioritySum(actualInput)

    check(testResult == 157)
    println(actualResult)

    val testGroups = badgeSum(testInput)
    val actualGroups = badgeSum(actualInput)

    check(testGroups == 70)
    println(actualGroups)

}