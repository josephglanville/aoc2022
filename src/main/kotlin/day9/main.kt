package day9

import util.resAsText
import java.lang.Integer.max
import kotlin.math.abs

data class Point(var x: Int, var y: Int)

fun calculateTailVisited(moves: List<Pair<String, Int>>, ropeLength: Int) : Set<Point> {
    val rope = (0 until ropeLength).map { Point(0, 0) }
    val visited = mutableSetOf<Point>()
    moves.forEach { (dir, num) ->
        repeat(num) {
            // Update the head position
            val head = rope.first()
            when (dir) {
                "U" -> head.y++
                "D" -> head.y--
                "R" -> head.x++
                "L" ->  head.x--
                else -> throw IllegalArgumentException("Unknown direction $dir")
            }

            // Calculate new positions for each knot
            (1 until ropeLength).forEach { i ->
                val leader = rope[i-1] // Get preceding knot
                val follower = rope[i]
                if (max(abs(leader.x - follower.x), abs(leader.y - follower.y)) > 1) {
                    follower.x += leader.x.compareTo(follower.x)
                    follower.y += leader.y.compareTo(follower.y)
                }
            }
            // Add to visited
            visited.add(rope.last().copy())
        }
    }
    return visited
}

fun asMoves(input: String) = input.lines().map {
    val (dir, num) = it.split(" ", limit = 2)
    dir to num.toInt()
}

fun main() {
    val testMoves = asMoves(resAsText(9, "test.txt"))
    val actualMoves = asMoves(resAsText(9, "input.txt"))

    val testVisited = calculateTailVisited(testMoves, 2)
    val actualVisited = calculateTailVisited(actualMoves, 2)
    val actualVisited2 = calculateTailVisited(actualMoves, 10)

    check(testVisited.size == 13)

    println(actualVisited.size)
    println(actualVisited2.size)
}
