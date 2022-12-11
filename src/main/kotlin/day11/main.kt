package day11

import util.resAsText

data class Monkey(val items: ArrayDeque<Long>, val op: (Long) -> Long, val test: Long, val trueOutcome: Int, val falseOutcome: Int, var inspections: Long)

fun constantPlus(constant: Long) : (Long) -> Long {
    return { old: Long -> old + constant }
}

fun constantMul(constant: Long) : (Long) -> Long {
    return { old: Long -> old * constant }
}

fun square() : (Long) -> Long {
    return { old: Long -> old * old}
}

fun runRound(monkeys: List<Monkey>, reliefFn: (Long) -> Long) {
    monkeys.forEach { monkey ->
        while (monkey.items.isNotEmpty()) {
            monkey.inspections++
            val item = monkey.items.removeFirst()
            val newItem = reliefFn(monkey.op(item))
            val newMonkeyIdx = if (newItem % monkey.test == 0L) monkey.trueOutcome else monkey.falseOutcome
            monkeys[newMonkeyIdx].items.add(newItem)
        }
    }
}

fun calculateMonkeyBusiness(monkeys: List<Monkey>) = monkeys.map { it.inspections }.sorted().reversed().subList(0, 2).reduce { acc, i -> acc * i}

fun asMonkeys(input: String) = input.split("\n\n").map { monkeySpec ->
    val lines = monkeySpec.lines()
    val items = ArrayDeque<Long>().apply {addAll(lines[1].split(":", limit = 2)[1].split(",").map { it.strip().toLong() }) }
    val opLine = lines[2].split(" = ", limit = 2)[1].split(" ")
    assert(opLine[0] == "old")
    val op = when (opLine[1]) {
        "+" -> constantPlus(opLine[2].toLong())
        "*" -> if (opLine[2] == "old") square() else constantMul(opLine[2].toLong())
        else -> throw IllegalArgumentException("Unknown operator ${opLine[2]}")
    }
    val test = lines[3].split(" by ", limit = 2)[1].toLong()
    val trueOutcome = lines[4].split(" monkey ", limit = 2)[1].toInt()
    val falseOutcome = lines[5].split(" monkey ", limit = 2)[1].toInt()
    Monkey(items, op, test, trueOutcome, falseOutcome, 0)
}

fun part1(monkeys: List<Monkey>) : Long {
    repeat(20) {
        runRound(monkeys) { w -> w / 3 }
    }
    return calculateMonkeyBusiness(monkeys)
}

fun part2(monkeys: List<Monkey>) : Long {
    val gcd = monkeys.map { it.test }.reduce { acc, t -> acc*t}
    repeat(10_000) {
        runRound(monkeys) { w -> w % gcd }
    }
    return calculateMonkeyBusiness(monkeys)
}

fun main() {
    val testInput = resAsText(11, "test.txt")
    val actualInput = resAsText(11, "input.txt")

    check(part1(asMonkeys(testInput)) == 10605L)
    println(part1(asMonkeys(actualInput)))

    println(part2(asMonkeys(testInput)))
    println(part2(asMonkeys(actualInput)))
}
