import java.io.File
import java.time.LocalDateTime

fun main(args: Array<String>) {
    println("part 1: " + partOne())
    println("part 2: " + partTwo())
}

private fun partOne(): Int {
    val rawFile = File("res/day-4.txt").readText()
    val datetimeAndActions = rawFile
            .split("\n")
            .filter { it != "" }
            .sorted()
            .map(::parseLine)

    val guardsSleepingTimes = hashMapOf<Int, IntArray>()
    var currentGuard = -1
    var fallAsleepTime = LocalDateTime.MIN

    for ((dateTime, action) in datetimeAndActions) {
        if (action[0] == 'G') {
            currentGuard = action.filter { it.isDigit() }.toInt()
            fallAsleepTime = dateTime
        }
        if (action == "falls asleep") {
            fallAsleepTime = dateTime
        } else {
            val sleepingMinutes = guardsSleepingTimes.getOrDefault(currentGuard, IntArray(60))
            (fallAsleepTime.minute until dateTime.minute).forEach { sleepingMinutes[it] += 1 }
            guardsSleepingTimes[currentGuard] = sleepingMinutes
        }
    }

    val sleepyGuard = guardsSleepingTimes.maxBy { it.value.sum() }!!
    val guardsMinuteWithMostSleep = sleepyGuard.value.withIndex().maxBy { it.value }!!.index

    return sleepyGuard.key * guardsMinuteWithMostSleep
}

private fun partTwo(): Int {
    val rawFile = File("res/day-4.txt").readText()
    val datetimeAndActions = rawFile
            .split("\n")
            .filter { it != "" }
            .sorted()
            .map(::parseLine)

    val guardsSleepingTimes = hashMapOf<Int, IntArray>()
    var currentGuard = -1
    var fallAsleepTime = LocalDateTime.MIN

    for ((dateTime, action) in datetimeAndActions) {
        if (action[0] == 'G') {
            currentGuard = action.filter { it.isDigit() }.toInt()
            fallAsleepTime = dateTime
        }
        if (action == "falls asleep") {
            fallAsleepTime = dateTime
        } else {
            val sleepingMinutes = guardsSleepingTimes.getOrDefault(currentGuard, IntArray(60))
            (fallAsleepTime.minute until dateTime.minute).forEach { sleepingMinutes[it] += 1 }
            guardsSleepingTimes[currentGuard] = sleepingMinutes
        }
    }

    val guardWithMostConsistentSleep = guardsSleepingTimes.maxBy { it.value.max()!! }!!
    val mostSleeptMinute = guardWithMostConsistentSleep.value.withIndex().maxBy { it.value }!!.index
    return guardWithMostConsistentSleep.key * mostSleeptMinute
}

private fun parseLine(line: String): Pair<LocalDateTime, String> {
    val splitLine = line.drop(1).split("]")

    val datetime = parseDateTime(splitLine)
    return Pair(datetime, splitLine[1].drop(1))
}

private fun parseDateTime(splitLine: List<String>): LocalDateTime {
    val datetimeString = splitLine[0]
            .replace(Regex("[^0-9]"), " ")
            .split(" ")
            .map { it.toInt() }
    val datetime = LocalDateTime.of(datetimeString[0], datetimeString[1], datetimeString[2], datetimeString[3], datetimeString[4])
    return datetime!!
}