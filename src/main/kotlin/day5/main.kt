package day5

import util.resAsText
import java.util.regex.Pattern

val WHITESPACE: Pattern = Pattern.compile("\\s+")

fun asStacks(input: List<String>) : List<List<Char>> {
    val reversed = input.asReversed().toMutableList()
    val stacks = reversed.removeAt(0).split(WHITESPACE).filterNot { it.isEmpty() }.map { mutableListOf<Char>() }
    reversed.forEach { line ->
        line.chunked(4).forEachIndexed { idx, column ->
            if (column[1] != ' ') {
                stacks[idx].add(column[1])
            }
        }
    }
    return stacks
}

data class Move(val num: Int, val src: Int, val dest: Int)

fun asMoves(input: List<String>) : List<Move> =
    input.map { l -> l.split(" ").let { m -> Move(m[1].toInt(), m[3].toInt(), m[5].toInt()) } }

fun computeTopCrates9000(input: List<String>) : String {
    val stacks = asStacks(input[0].lines())
    val moves = asMoves(input[1].lines())
    val finished = applyMoves9000(stacks, moves)
    return finished.map { it.last() }.joinToString("")
}

fun computeTopCrates9001(input: List<String>) : String {
    val stacks = asStacks(input[0].lines())
    val moves = asMoves(input[1].lines())
    val finished = applyMoves9001(stacks, moves)
    return finished.map { it.last() }.joinToString("")
}

fun main() {
    val testInput = resAsText(5, "test.txt").split("\n\n", ignoreCase = true, limit = 2)
    val actualInput = resAsText(5, "input.txt").split("\n\n", ignoreCase = true, limit = 2)

    check(computeTopCrates9000(testInput) == "CMZ")
    println(computeTopCrates9000(actualInput))

    check(computeTopCrates9001(testInput) == "MCD")
    println(computeTopCrates9001(actualInput))
}

fun applyMoves9000(stacks: List<List<Char>>, moves: List<Move>): List<List<Char>> {
    val output = stacks.map { it.toMutableList() }
    moves.forEach { move ->
        repeat(move.num) { output[move.dest-1].add(output[move.src-1].removeLast()) }
    }
    return output
}

fun applyMoves9001(stacks: List<List<Char>>, moves: List<Move>): List<List<Char>> {
    val output = stacks.map { it.toMutableList() }.toMutableList()
    moves.forEach { move ->
        val src = output[move.src - 1]
        output[move.dest - 1].addAll(src.subList(src.size - move.num, src.size))
        output[move.src - 1] = src.subList(0, src.size - move.num)
    }
    return output
}
