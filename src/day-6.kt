import java.io.File
import kotlin.math.abs

fun main(args: Array<String>) {
    println("part 1: " + partOne())
    println("part 2: " + partTwo())
}

private data class Point(val x: Int, val y: Int) {
    fun distance(other: Point): Int = abs(x - other.x) + abs(y - other.y)
}

private fun partOne(): Int? {
    val points = parseFile()
    val areaOfPoint = HashMap<Point, Int>()

    val minX = points.map { it.x }.min()!!
    val maxX = points.map { it.x }.max()!!
    val minY = points.map { it.y }.min()!!
    val maxY = points.map { it.y }.max()!!

    val pointsToIgnore = computerOuterPoints(minX, maxX, minY, maxY, points)


    for (x in minX..maxX) {
        for (y in minY..maxY) {
            val closest = points.map { Pair(it, Point(x, y).distance(it)) }.sortedBy { it.second }
            val (p, d) = closest[0]

            if (d == closest[1].second || p in pointsToIgnore) {
                continue
            }

            val count = areaOfPoint.getOrDefault(p, 0)
            areaOfPoint[p] = count + 1

        }
    }

    return areaOfPoint.values.max()
}

private fun partTwo(): Int {
    val points = parseFile()
    var numberOfValidPoints = 0

    val minX = points.map { it.x }.min()!!
    val maxX = points.map { it.x }.max()!!
    val minY = points.map { it.y }.min()!!
    val maxY = points.map { it.y }.max()!!

    for (x in minX..maxX) {
        for (y in minY..maxY) {
            val sumOfDistances = points.map { Point(x, y).distance(it) }.sum()
            if (sumOfDistances < 10000) {
                numberOfValidPoints += 1
            }
        }
    }

    return numberOfValidPoints
}

private fun parseFile(): List<Point> {
    val rawFile = File("res/day-6.txt").readText()
    val points = rawFile
            .split("\n")
            .filter { it != "" }
            .map { it.split(", ").map(String::toInt) }
            .map { Point(it[0], it[1]) }
    return points
}

private fun computerOuterPoints(minX: Int, maxX: Int, minY: Int, maxY: Int, points: List<Point>): MutableSet<Point> {
    val pointsToIgnore = mutableSetOf<Point>()
    for (x in listOf(minX, maxX)) {
        for (y in minY..maxY) {
            val closest = points.minBy { Point(x, y).distance(it) }!!
            pointsToIgnore.add(closest)
        }
    }

    for (x in minX..maxX) {
        for (y in listOf(minY, maxY)) {
            val closest = points.minBy { Point(x, y).distance(it) }!!
            pointsToIgnore.add(closest)
        }
    }
    return pointsToIgnore
}
