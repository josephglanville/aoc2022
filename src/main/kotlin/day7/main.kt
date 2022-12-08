package day7

import util.resAsText

data class Directory(val parent: Directory?, val files: MutableMap<String, Int> = mutableMapOf(), val children: MutableMap<String, Directory> = mutableMapOf()) {
    override fun toString() = "" // Just to prevent infinite recursion through parent.

    fun size() : Int = files.values.sum() + children.values.sumOf { it.size() }
    fun atMost(limit: Int) : List<Directory> = children.values.filter { it.size() <= limit } + children.values.flatMap { it.atMost(limit) }
    fun atLeast(min: Int) : List<Directory> = children.values.filter { it.size() >= min } + children.values.flatMap { it.atLeast(min) }
}

fun buildTree(input: String) : Directory {
    val root = Directory(null)
    var cwd = root
    val deque = ArrayDeque<String>().apply {
        addAll(input.lines())
    }

    while(deque.isNotEmpty()) {
        val line = deque.removeFirst()
        // Check if we have a command
        if (line.startsWith("$")) {
            val cmdLine = line.split(" ")
            when (val cmd = cmdLine[1]) {
                "ls" -> {
                    // Process all output until next command or end of deque is reached
                    while (deque.isNotEmpty() && !deque.first().startsWith("$")) {
                        val fileLine = deque.removeFirst().split(" ")
                        if (fileLine[0] == "dir") {
                            cwd.children.putIfAbsent(fileLine[1], Directory(cwd))
                        } else {
                            cwd.files.putIfAbsent(fileLine[1], fileLine[0].toInt())
                        }
                    }
                }
                "cd" -> {
                    cwd = when (val dir = cmdLine[2]) {
                        ".." -> cwd.parent!!
                        "/" -> root
                        else -> cwd.children[dir]!!
                    }
                }
                else -> throw IllegalArgumentException("Unknown command: $cmd")
            }
        }
    }

    return root
}

fun main() {
    val testInput = resAsText(7, "test.txt")
    val actualInput = resAsText(7, "input.txt")
    val testRoot = buildTree(testInput)
    val actualRoot = buildTree(actualInput)

    check(testRoot.size() == 48381165)
    check(testRoot.atMost(100000).sumOf { it.size() } == 95437)
    check(testRoot.atLeast(8381165).sumOf { it.size() } == 24933642)

    println(actualRoot.size())
    println(actualRoot.atMost(100000).sumOf { it.size() })
    println(actualRoot.atLeast(8381165).map { it.size() }.minOf { it })
}