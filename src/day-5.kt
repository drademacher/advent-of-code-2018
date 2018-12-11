import java.io.File

fun main(args: Array<String>) {
    println("part 1: " + partOne())
    println("part 2: " + partTwo())
}

private fun partOne(): Int {
    val rawFile = File("res/day-5.txt").readText().filter { it.isLetter() }

    val polymer = rawFile.toMutableList()

    fullyReact(polymer)

    return polymer.size
}

private fun fullyReact(polymer: MutableList<Char>): MutableList<Char> {
    var repeat: Boolean
    do {
        repeat = false
        for (i in 0 until polymer.size - 1) {
            if (hasToBeDestroyed(polymer[i], polymer[i + 1])) {
                repeat = true
                polymer.removeAt(i + 1)
                polymer.removeAt(i)
                break
            }
        }
    } while (repeat)

    return polymer
}

private fun hasToBeDestroyed(a: Char, b: Char): Boolean {
    return a != b && a.toLowerCase() == b.toLowerCase()
}

private fun partTwo(): Int? {
    val rawFile = File("res/day-5.txt").readText().filter { it.isLetter() }

    val initialFullyReactedPolymer = fullyReact(rawFile.toMutableList())

    return ('a'..'z')
            .map { char -> initialFullyReactedPolymer.filter { it.toLowerCase() != char }.toMutableList() }
            .map { fullyReact(it) }
            .map { it.size }
            .min()
}

