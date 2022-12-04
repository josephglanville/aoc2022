package day1

import util.resAsText

fun elvesFromInput(input: String) = input.split("\n\n").map { elf ->
    elf.lines().map { it.toInt() }
}

fun getBeefiestElf(elves: List<List<Int>>) = elves.maxOfOrNull { snacks -> snacks.sum() }
fun getTopNBeefiestElves(elves: List<List<Int>>, n: Int) = elves.map { it.sum() }.sortedBy { -it }.slice(0 until n).sum()

fun main() {
    val testElves = elvesFromInput(resAsText(1, "test.txt"))
    val actualElves = elvesFromInput(resAsText(1, "input.txt"))

    check(getBeefiestElf(testElves) == 24000)
    check(getTopNBeefiestElves(testElves, 3) == 45000)

    println(getBeefiestElf(actualElves))
    println(getTopNBeefiestElves(actualElves, 3))

}