import java.io.File

fun main(args: Array<String>) {
    println("part 1: " + simulateFast(20))
    println("part 2: " + simulateFast(50000000000))
}

// REMARK: the cycle becomes stable after around 200 generations, then adding 34 plants each cycle
private fun simulateFast(generations: Long): Long {
    if (generations <= 200) {
        return simulateExactNumber(generations.toInt())
    }

    val firstTwoHundredGenerations = simulateExactNumber(200)
    return firstTwoHundredGenerations + (generations - 200) * 34
}

private fun simulateExactNumber(n: Int): Long {
    val offsetOfEmptyPlants = 2 * n
    val rawFile = File("res/day-12.txt").readText()
            .split("\n")
            .filter { it != "" }

    var (plants, rules) = parseFile(rawFile, offsetOfEmptyPlants)

    for (generation in 0 until n) {
        var nextGeneration = ".."
        for (i in 0..plants.length - 5) {
            nextGeneration += when {
                plants.slice(i..i + 4) in rules -> "#"
                else -> "."
            }
        }
        nextGeneration += ".."
        plants = nextGeneration
    }

    return calcNumberOfLivingsPlants(plants, offsetOfEmptyPlants)
}

private fun calcNumberOfLivingsPlants(plants: String, offsetOfEmptyPlants: Int) =
        plants
                .withIndex()
                .filter { it.value == '#' }
                .map { it.index - offsetOfEmptyPlants }
                .map(Int::toLong)
                .sum()

private fun parseFile(rawFile: List<String>, offsetOfEmptyPlants: Int): Pair<String, Set<String>> {
    val plants = ".".repeat(offsetOfEmptyPlants) + rawFile[0].drop(15) + ".".repeat(offsetOfEmptyPlants)
    val rules = rawFile
            .drop(1)
            .filter { it.last() == '#' }
            .map { it.take(5) }
            .toSet()
    return Pair(plants, rules)
}
