import java.io.File

private data class Rectangle(val left: Int, val top: Int, val width: Int, val height: Int)

fun main(args: Array<String>) {
    println("part 1: " + partOne())
    println("part 2: " + partTwo())
}

private fun partOne(): Int {
    val rawFile = File("res/day-3.txt").readText()
    val field = Array(1000) { BooleanArray(1000) }
    val conflict = Array(1000) { BooleanArray(1000) }

    val commands = parseFile(rawFile)

    computeConflicts(commands, field, conflict)

    val numberOfConflicts = conflict.sumBy { it.count { innerBoolean -> innerBoolean } }
    return numberOfConflicts
}

private fun partTwo(): Int {
    val rawFile = File("res/day-3.txt").readText()
    val field = Array(1000) { BooleanArray(1000) }
    val conflict = Array(1000) { BooleanArray(1000) }

    val rectangles = parseFile(rawFile)

    computeConflicts(rectangles, field, conflict)

    for ((index, command) in rectangles.withIndex()) {

        var conflictFree = true
        for (y in command.left until command.left + command.width) {
            for (x in command.top until command.top + command.height) {

                if (conflict[x][y]) {
                    conflictFree = false
                }
            }
        }

        if (conflictFree) {
            return index + 1
        }
    }

    return -1
}

private fun parseFile(rawFile: String): List<Rectangle> {
    return rawFile
            .split("\n")
            .filter { it != "" }
            .map { it.substring(it.indexOf('@')) }
            .map { it.replace(Regex("[^0-9]"), ";") }
            .map { it.split(";").filter { it != "" }.map { it.toInt() } }
            .map { Rectangle(it[0], it[1], it[2], it[3]) }
}

private fun computeConflicts(rectangles: List<Rectangle>, field: Array<BooleanArray>, conflict: Array<BooleanArray>) {
    for (command in rectangles) {
        for (y in command.left until command.left + command.width) {
            for (x in command.top until command.top + command.height) {

                if (field[x][y]) {
                    conflict[x][y] = true
                } else {
                    field[x][y] = true
                }
            }
        }
    }
}