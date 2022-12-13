package day12

import util.resAsText

fun findShortestPath(elevations: List<CharArray>, start: Pair<Int, Int>, end: Pair<Int, Int>): List<Pair<Int, Int>> {
    val queue = mutableListOf(start)
    val visited = mutableSetOf(start)
    val paths = mutableMapOf(start to listOf<Pair<Int, Int>>())

    // Find the shortest path using BFS
    while (queue.isNotEmpty()) {
        val current = queue.removeFirst()
        if (current == end) return paths[current]!!

        for (neighbor in getNeighbors(elevations, current)) {
            // Add the unvisited neighbors to the queue
            if (visited.add(neighbor)) {
                queue.add(neighbor)
                // Save the path taken to reach this neighbor
                paths[neighbor] = paths[current]!! + neighbor
            }
        }
    }

    // If we reach here, it means we couldn't find a path to the end square
    return emptyList()
}

fun getPosition(elevations: List<CharArray>, ch: Char): Pair<Int, Int> {
    for (i in elevations.indices) {
        for (j in elevations[i].indices) {
            if (elevations[i][j] == ch) {
                return i to j
            }
        }
    }
    throw IllegalArgumentException("Character not found in the grid")
}

fun getPositions(elevations: List<CharArray>, chars: List<Char>) : List<Pair<Int, Int>>{
    val positions = mutableListOf<Pair<Int, Int>>()
    for (i in elevations.indices) {
        for (j in elevations[i].indices) {
            if (chars.contains(elevations[i][j])) {
                positions.add(i to j)
            }
        }
    }
    return positions
}

fun getNeighbors(elevations: List<CharArray>, current: Pair<Int, Int>): List<Pair<Int, Int>> {
    val (i, j) = current
    val neighbors = mutableListOf<Pair<Int, Int>>()

    if (i > 0) {
        val neighbor = Pair(i - 1, j)
        if (isValidNeighbor(elevations, current, neighbor)) {
            neighbors.add(neighbor)
        }
    }
    if (i < elevations.size - 1) {
        val neighbor = Pair(i + 1, j)
        if (isValidNeighbor(elevations, current, neighbor)) {
            neighbors.add(neighbor)
        }
    }
    if (j > 0) {
        val neighbor = Pair(i, j - 1)
        if (isValidNeighbor(elevations, current, neighbor)) {
            neighbors.add(neighbor)
        }
    }
    if (j < elevations[i].size - 1) {
        val neighbor = Pair(i, j + 1)
        if (isValidNeighbor(elevations, current, neighbor)) {
            neighbors.add(neighbor)
        }
    }

    return neighbors
}

fun getElevation(elevations: List<CharArray>, point: Pair<Int, Int>) : Char {
    val (i, j) = point
    return when {
        elevations[i][j] == 'S' -> 'a'
        elevations[i][j] == 'E' -> 'z'
        else -> elevations[i][j]
    }
}

fun isValidNeighbor(elevations: List<CharArray>, current: Pair<Int, Int>, neighbor: Pair<Int, Int>): Boolean {
    return getElevation(elevations, neighbor) <= (getElevation(elevations, current) + 1)
}

fun part1(input: String) : Int {
    val elevations = input.lines().map { it.toCharArray() }
    val start = getPosition(elevations, 'S')
    val end = getPosition(elevations, 'E')
    return findShortestPath(elevations, start, end).size
}

fun part2(input: String) : Int {
    val elevations = input.lines().map { it.toCharArray() }
    val end = getPosition(elevations, 'E')
    val starts = getPositions(elevations, listOf('S', 'a'))
    // Get all paths, removing any empty ones as that means there wasn't a viable path from start to end
    val paths = starts.map { s -> findShortestPath(elevations, s, end) }.filter { it.isNotEmpty() }
    return paths.minByOrNull { it.size }!!.size
}

fun main() {
    val testInput = resAsText(12, "test.txt")
    val actualInput = resAsText(12, "input.txt")

    check(part1(testInput) == 31)
    check(part2(testInput) == 29)

    println(part1(actualInput))
    println(part2(actualInput))
}