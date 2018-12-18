import Ground.*
import java.io.File

fun main(args: Array<String>) {
    println("part 1: " + partOne())
    println("part 2: " + partTwo())
}

private enum class Ground { OPEN, TREE, LUMBERYARD }
private data class Area(val area: Array<Array<Ground>>) {
    fun getHeight() = area.size
    fun getWidth() = area[0].size

    fun deepCopy() = Area(Array(getWidth()) { it -> area[it].copyOf() })

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Area

        if (!area.contentDeepEquals(other.area)) return false

        return true
    }

    override fun hashCode(): Int {
        return area.contentDeepHashCode()
    }
}

private fun partOne(): Int {
    var area = parseInputFile()

    val generations = 10
    area = simulate(area, generations)

    return calculateAnswerFromArea(area)
}

private fun partTwo(): Int {
    var area = parseInputFile()

    val generations = 1000000000
    area = simulateWithCycleDetection(area, generations)

    return calculateAnswerFromArea(area)
}

private fun calculateAnswerFromArea(area: Area): Int {
    val trees = area.area.map { it.count { ground -> ground == TREE } }.sum()
    val lumberyards = area.area.map { it.count { ground -> ground == LUMBERYARD } }.sum()
    return trees * lumberyards
}

private fun simulateWithCycleDetection(area: Area, generations: Int): Area {
    var newArea = area
    val previousGenerations = mutableListOf(area)
    var foundCycleAt = 0

    for (i in 0 until generations) {
        newArea = simulateStep(newArea)
        if (newArea in previousGenerations) {
            foundCycleAt = i + 1
            break
        }
        previousGenerations.add(newArea)
    }

    val cycleLength = foundCycleAt - previousGenerations.indexOf(newArea)
    val fastForward = (generations - foundCycleAt) % cycleLength
    
    return simulate(newArea, fastForward)
}


private fun simulate(area: Area, generations: Int): Area {
    var newArea = area
    for (i in 0 until generations) {
        newArea = simulateStep(newArea)
    }
    return newArea
}

private fun simulateStep(area: Area): Area {
    val nextArea = area.deepCopy()
    for (x in 0 until area.getWidth()) {
        for (y in 0 until area.getHeight()) {
            nextArea.area[y][x] = when (area.area[y][x]) {
                OPEN -> if (countSurroundingGround(area, x, y, TREE) >= 3) TREE else OPEN
                TREE -> if (countSurroundingGround(area, x, y, LUMBERYARD) >= 3) LUMBERYARD else TREE
                LUMBERYARD -> if (countSurroundingGround(area, x, y, LUMBERYARD) >= 1 && countSurroundingGround(area, x, y, TREE) >= 1) LUMBERYARD else OPEN
            }
        }
    }
    return nextArea
}

private fun countSurroundingGround(area: Area, x: Int, y: Int, whatToCount: Ground): Int {
    var res = 0

    for (curX in x - 1..x + 1) {
        for (curY in y - 1..y + 1) {
            val validArrayIndex = curX in 0 until area.getWidth() && curY in 0 until area.getHeight()
            val isNotCenter = curX != x || curY != y
            if (validArrayIndex && isNotCenter && area.area[curY][curX] == whatToCount) {
                res += 1
            }
        }
    }
    return res
}

private fun printArea(area: Area) {
    area.area.forEach {
         println(it.map(::mapToChar).joinToString(separator = ""))
    }
}

private fun parseInputFile(): Area {
    val rawFile = File("res/day-18.txt").readText()
    return Area(
            rawFile
                    .split("\n")
                    .filter { it != "" }
                    .map { it.map(::mapToGround).toTypedArray() }
                    .toTypedArray()
    )
}

private fun mapToGround(char: Char): Ground {
    return when (char) {
        '.' -> OPEN
        '|' -> TREE
        '#' -> LUMBERYARD
        else -> throw Exception("invalid symbol at parsing input")
    }
}

private fun mapToChar(ground: Ground): Char {
    return when (ground) {
        OPEN -> '.'
        TREE -> '|'
        LUMBERYARD -> '#'
    }
}
