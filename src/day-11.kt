fun main(args: Array<String>) {
    println("part 1: " + partOne())
    println("part 2: " + partTwo())
}

private fun partOne(): String {
    val serial = 7400
    val n = 300
    val field = Array(n) { IntArray(n) }

    computeAllPowerLevels(n, field, serial)

    var maxSquare = 0
    var xResult = -1
    var yResult = -1

    val size = 3
    for (i in 0 until n - size + 1) {
        for (j in 0 until n - size + 1) {
            val squareSum = computeSquare(field, i, j, 3)

            if (squareSum > maxSquare) {
                maxSquare = squareSum
                xResult = i
                yResult = j
            }
        }
    }

    return "$xResult,$yResult"
}

private fun computeSquare(field: Array<IntArray>, i: Int, j: Int, size: Int): Int {
    var squareSum = 0
    for (a in 0 until size) {
        for (b in 0 until size) {
            squareSum += field[i + a][j + b]
        }
    }
    return squareSum
}

private fun partTwo(): String {
    // TODO: this is very unoptimized, should run in under 1s
    val serial = 7400
    val n = 300
    val field = Array(n) { IntArray(n) }

    computeAllPowerLevels(n, field, serial)

    var maxSquare = 0
    var xResult = -1
    var yResult = -1
    var sizeResult = -1

    for (size in 1..100) {
        for (i in 0 until n - size + 1) {
            for (j in 0 until n - size + 1) {
                val squareSum = computeSquare(field, i, j, size)

                if (squareSum > maxSquare) {
                    maxSquare = squareSum
                    xResult = i
                    yResult = j
                    sizeResult = size
                }
            }
        }
    }

    return "$xResult,$yResult,$sizeResult"
}

private fun computeAllPowerLevels(n: Int, field: Array<IntArray>, serial: Int) {
    for (i in 0 until n) {
        for (j in 0 until n) {
            field[i][j] = powerLevel(i, j, serial)
        }
    }
}


private fun powerLevel(x: Int, y: Int, serial: Int): Int {
    val rackId = x + 10
    val powerLevel = (rackId * y + serial) * rackId
    val hundredDigit = (powerLevel % 1000) / 100 - 5
    return hundredDigit
}

private fun runTestSamples() {
    println("" + powerLevel(3, 5, 8) + " should equals " + 4)
    println("" + powerLevel(122, 79, 57) + " should equals " + -5)
    println("" + powerLevel(217, 196, 39) + " should equals " + 0)
    println("" + powerLevel(101, 153, 71) + " should equals " + 4)

    val serial = 18
    val n = 300
    val field = Array(n) { IntArray(n) }

    computeAllPowerLevels(n, field, serial)

    var maxSquare = 0
    var xResult = -1
    var yResult = -1
    var sizeResult = -1

    for (size in 1..20) {
        for (i in 0 until n - size + 1) {
            for (j in 0 until n - size + 1) {
                val squareSum = computeSquare(field, i, j, size)

                if (squareSum > maxSquare) {
                    maxSquare = squareSum
                    xResult = i
                    yResult = j
                    sizeResult = size
                }
            }
        }
    }

    println("$xResult,$yResult,$sizeResult should equal 90,269,16")
}