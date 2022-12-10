package day10

import day10.Instruction.*
import util.resAsText

enum class Instruction(val cycles: Int) {
    NOOP(1),
    ADDX(2)
}

data class Op(val ins: Instruction, val arg: Int = 0)

fun asOps(input: String) = input.lines().map {
    if (it == "noop") Op(NOOP) else Op(ADDX, it.split(" ", limit = 2)[1].toInt())
}

data class Pixel(val x: Int, val y: Int)
typealias Display = Map<Pixel, Boolean>

fun runCPU(ops: List<Op>, callback: (cycleCounter: Int, x: Int) -> Unit) {
    var x = 1
    var cycleCounter = 1

    fun cycle() {
        callback(cycleCounter, x)
        cycleCounter += 1
    }

    for (op in ops) {
        when (op.ins) {
            NOOP -> repeat(op.ins.cycles) { cycle() }
            ADDX -> {
                repeat(op.ins.cycles) { cycle() }
                x += op.arg
            }
        }
    }
}

fun printDisplay(display: Display) {
    for (y in 0..6) { // For each line in the display
        for (x in 0..40) { // For each pixel in the line
            print(if (display[Pixel(x, y)] == true) "#" else ".")
        }
        println()
    }
}

fun signalStrengthSum(ops: List<Op>) : Int {
    var signalStrength = 0
    runCPU(ops) { cycleCounter, x ->
        if (cycleCounter % 40 == 20) {
            signalStrength += cycleCounter * x
        }
    }
    return signalStrength
}

fun generateDisplay(ops: List<Op>) : Display {
    val display = mutableMapOf<Pixel, Boolean>()
    runCPU(ops) { cycleCounter, x ->
        val px = (cycleCounter - 1) % 40
        if (px in (x - 1)..(x + 1)) {
            display[Pixel(px, (cycleCounter - 1) / 40)] = true
        }
    }
    return display
}

fun main() {
    val testOps = asOps(resAsText(10, "test.txt"))
    val actualOps = asOps(resAsText(10, "input.txt"))

    check(signalStrengthSum(testOps) == 13140)

    println(signalStrengthSum(actualOps))
    printDisplay(generateDisplay(actualOps))
}
