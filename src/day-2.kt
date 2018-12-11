import java.io.File

fun main(args: Array<String>) {
    println("part 1: " + partOne())
    println("part 2: " + partTwo())
}

private fun partOne(): Int {
    val rawFile = File("res/day-2.txt").readText()

    val charactersGroupedByLength = rawFile // returns 435
            .split("\n")
            .filter { it != "" }
            .map { box -> box.toCharArray() }
            .map { box -> box.groupBy { char -> box.filter { it == char }.size } }

    val boxesWithDuplicatedLetter = charactersGroupedByLength.filter { it.containsKey(2) }.size
    val boxesWithTripledLetter = charactersGroupedByLength.filter { it.containsKey(3) }.size

    return boxesWithDuplicatedLetter * boxesWithTripledLetter
}

private fun partTwo(): String {
    val rawFile = File("res/day-2.txt").readText()

    val boxes = rawFile // returns 435
            .split("\n")
            .filter { it != "" }
            .map { it.toCharArray() }


    val n = boxes.size

    for (i in 0 until n) {
        for (j in 0 until n) {
            if (i == j) {
                continue
            }

            val differences = boxes[i].zip(boxes[j])
                    .count { pair -> pair.first != pair.second }

            if (differences <= 1) {
                val commonLetters = boxes[i].zip(boxes[j])
                        .filter { pair -> pair.first == pair.second }
                        .map { pair -> pair.first }

                return commonLetters.joinToString(separator = "")
            }

        }
    }

    return ""
}