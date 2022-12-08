package day8

import util.resAsText
import java.lang.Integer.max

typealias Grid = List<List<Int>>

fun asGrid(input: String): Grid =
    input.lines().map { l -> l.map { c -> c.digitToInt() } }

fun verticalSlice(grid: Grid, column: Int): List<Int> = grid.map { l -> l[column] }

fun findVisible(grid: Grid): Set<Pair<Int, Int>> {
    var visible = mutableSetOf<Pair<Int, Int>>()
    for (y in grid.indices) {
        for (x in grid[y].indices) {
            val current = grid[y][x]
            val row = grid[y]
            val column = verticalSlice(grid, x)

            val north = column.subList(0,y)
            val south = column.subList(y+1, column.size)
            val east = row.subList(0, x)
            val west = row.subList(x+1, row.size)

            val isVisible =
                north.isEmpty() || north.all { current > it } ||
                south.isEmpty() || south.all { current > it } ||
                east.isEmpty() || east.all { current > it } ||
                west.isEmpty() || west.all { current > it }
            if (isVisible) {
                visible.add(x to y)
            }
        }
    }
    return visible
}

fun <T> Iterable<T>.takeUntil(predicate: (T) -> Boolean) : List<T> {
    val list = ArrayList<T>()
    for (item in this) {
        list.add(item)
        if (!predicate(item))
            break
    }
    return list
}

fun findMostScenic(grid: Grid): Int {
    var best = 0;
    for (y in grid.indices) {
        for (x in grid[y].indices) {
            val current = grid[y][x]
            val row = grid[y]
            val column = verticalSlice(grid, x)

            val north = column.subList(0,y).reversed().takeUntil { current > it }
            val south = column.subList(y+1, column.size).takeUntil { current > it }
            val east = row.subList(0, x).reversed().takeUntil { current > it }
            val west = row.subList(x+1, row.size).takeUntil { current > it }

            val scenic = north.size * south.size * east.size * west.size
            best = max(best, scenic)
        }
    }
    return best
}

fun main() {
    val testInput = asGrid(resAsText(8, "test.txt"))
    val actualInput = asGrid(resAsText(8, "input.txt"))

    check(findVisible(testInput).size == 21)
    check(findMostScenic(testInput) == 8)

    println(findVisible(actualInput).size)
    println(findMostScenic(actualInput))

}
