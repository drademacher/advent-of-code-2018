import java.io.File

fun main(args: Array<String>) {
    println("part 1: " + partOne())
    println("part 2: " + partTwo())
}

private fun partOne(): Int {
    val rawFile = File("res/day-1.txt").readText()

    return rawFile // returns 435
            .split("\n")
            .filter { it != "" }
            .map { it.toInt() }
            .sum()
}

private fun partTwo(): Long? {
    val n = 1000000
    val rawFile = File("res/day-1.txt").readText()

    val numbers = rawFile
            .split("\n")
            .filter { it != "" }
            .map { it.toLong() }

    var current = 0L
    val occurrences = mutableSetOf<Long>(current)


    for (i in 0 until n) {
        current += numbers[i % numbers.size]

        val hasAlreadyOccured = occurrences.contains(current)
        if (hasAlreadyOccured) {
            return current
        }
        occurrences.add(current)
    }

    return null
}
