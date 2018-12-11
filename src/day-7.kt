import java.io.File

fun main(args: Array<String>) {
    println("part 1: " + partOne())
    println("part 2: " + partTwo())
}

private fun partOne(): String {
    var result = ""
    val inDegreeGraph = parseFile()

    do {
        val verticesWithNoInDegree = getVerticesWithNoInDegree(inDegreeGraph)
        val vertexWithSmallestLetter = verticesWithNoInDegree.min()!!
        result += vertexWithSmallestLetter
        removeFromGraph(inDegreeGraph, vertexWithSmallestLetter)
    } while (inDegreeGraph.keys.isNotEmpty())

    return result
}

private fun partTwo(): Int {
    val NUMBER_OF_WORKER = 5

    var ticks = 0
    val inDegreeGraph = parseFile()
    val workers = IntArray(NUMBER_OF_WORKER)
    val processingVertexOfWorker = CharArray(NUMBER_OF_WORKER)


    while(inDegreeGraph.isNotEmpty()) {
        val verticesToBeProcess = getVerticesWithNoInDegree(inDegreeGraph)
                .subtract(processingVertexOfWorker.toList())
                .toMutableList()

        val freeWorker = getFreeWorkerIndices(workers).take(verticesToBeProcess.size)
        for ((vertexIndex, workerIndex) in freeWorker.withIndex()) {
            val vertexToProcess = verticesToBeProcess[vertexIndex]
            workers[workerIndex] = processingTime(vertexToProcess)
            processingVertexOfWorker[workerIndex] = vertexToProcess
        }

        ticks += 1
        for (workerIndex in 0 until NUMBER_OF_WORKER) {
            workers[workerIndex] -= 1
            if (workers[workerIndex] == 0) {
                val finishedVertex = processingVertexOfWorker[workerIndex]
                processingVertexOfWorker[workerIndex] = '-'
                removeFromGraph(inDegreeGraph, finishedVertex)
            }
        }
    }

    return ticks
}

private fun parseFile(): HashMap<Char, MutableList<Char>> {
    val rawFile = File("res/day-7.txt").readText()
            .split("\n")
            .filter { it != "" }

    val inDegreeGraph = hashMapOf<Char, MutableList<Char>>()
    ('A'..'Z').forEach { c -> inDegreeGraph[c] = mutableListOf() }

    for (line in rawFile) {
        inDegreeGraph[line[36]]!!.add(line[5])
    }
    return inDegreeGraph
}

private fun getVerticesWithNoInDegree(inDegreeGraph: HashMap<Char, MutableList<Char>>): List<Char> {
    return inDegreeGraph
            .filter { it.value.isEmpty() }
            .keys
            .toList()
            .sorted()
}

private fun removeFromGraph(graph: HashMap<Char, MutableList<Char>>, vertex: Char) {
    graph.remove(vertex)
    graph.forEach { key, value -> value.remove(vertex) }
}

private fun processingTime(vertex: Char): Int {
    return 61 + ('A'..'Z').indexOf(vertex)
}

private fun getFreeWorkerIndices(workers: IntArray): List<Int> {
    return workers.withIndex().filter { it.value <= 0 }.map { it.index }.sorted()
}