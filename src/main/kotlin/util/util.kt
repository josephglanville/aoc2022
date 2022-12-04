package util

import java.io.File

fun resAsText(day: Int, input: String) = File("src/main/resources/day${day}/${input}").readText()