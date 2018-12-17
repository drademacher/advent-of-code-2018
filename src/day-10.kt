import java.io.File

fun main(args: Array<String>) {
    runFastForwardWriter()
}

private data class MovingPoint(val xInitial: Int, val yInitial: Int, val xChange: Int, val yChange: Int) {
    fun getXAtTime(t: Int) = xInitial + t * xChange
    fun getYAtTime(t: Int) = yInitial + t * yChange
}

private data class Field(val height: Int, val width: Int, val heightOffset: Int, val widthOffset: Int) {
    val field = Array(height) { BooleanArray(width) }
}

private fun runFastForwardWriter() {
    val rawFile = File("res/day-10.txt").readText()
    val points = rawFile
            .split("\n")
            .filter { it != "" }
            .map(::parseLine)

    val m = 12
    val n = 80

    for (t in 0..100000) {
        val (xOffset, yOffset) = computeOffset(points, t, m, n) ?: continue

        val field = Field(m, n, yOffset, xOffset)

        field.fill(points, t)

        println("time elapsed = $t")
        field.print()
        println()
    }
}

private fun computeOffset(points: List<MovingPoint>, t: Int, m: Int, n: Int): Pair<Int, Int>? {
    var xMin = Integer.MAX_VALUE
    var xMax = Integer.MIN_VALUE
    var yMin = Integer.MAX_VALUE
    var yMax = Integer.MIN_VALUE

    for (point in points) {
        val x = point.getXAtTime(t)
        xMin = minOf(xMin, x)
        xMax = maxOf(xMax, x)

        val y = point.getYAtTime(t)
        yMin = minOf(yMin, y)
        yMax = maxOf(yMax, y)
    }

    if (yMax - yMin > m || xMax - xMin > n) {
        return null
    }
    return Pair(-xMin, -yMin)
}

private fun Field.print() {
    for (y in 0 until this.height) {
        for (x in 0 until this.width) {
            print(
                    if (this.field[y][x])
                        "#"
                    else
                        "."
            )
        }
        println()
    }
}

private fun Field.fill(points: List<MovingPoint>, t: Int) {
    for (point in points) {
        val y = point.getYAtTime(t) + this.heightOffset
        val x = point.getXAtTime(t) + this.widthOffset

        if (y in 0 until this.height && x in 0 until this.width)
            field[y][x] = true
    }
}

private fun parseLine(it: String): MovingPoint {
    val listOfFourInts = it
            .replace(Regex(">.*<"), ",")
            .filter { it.isDigit() || it == ',' || it == '-' }
            .split(",")
            .map(String::toInt)
    return MovingPoint(listOfFourInts[0], listOfFourInts[1], listOfFourInts[2], listOfFourInts[3])
}
