import java.io.File

fun main(args: Array<String>) {
    println("part 1: " + partOne())
    println("part 2: " + partTwo())
}

private data class Node(val children: List<Node>, val metadata: List<Int>)

private fun partOne(): Int {
    val rawFile = File("res/day-8.txt").readText()
    val numbers = rawFile
            .filter { it != '\n' }
            .split(" ")
            .filter { it != "" }
            .map { it.toInt() }

    val (root, _) = parseToTree(numbers, 0)

    return sumOfMetadata(root)

}

private fun partTwo(): Int {
    val rawFile = File("res/day-8.txt").readText()
    val numbers = rawFile
            .filter { it != '\n' }
            .split(" ")
            .filter { it != "" }
            .map { it.toInt() }

    val (root, _) = parseToTree(numbers, 0)

    return puzzleSum(root)
}

private fun parseToTree(numbers: List<Int>, offset: Int): Pair<Node, Int> {
    val numberOfChildren = numbers[offset + 0]
    val numberOfMetaData = numbers[offset + 1]
    val children = mutableListOf<Node>()

    var currentOffset = offset + 2

    for (i in 1..numberOfChildren) {
        val pair = parseToTree(numbers, currentOffset)
        children.add(pair.first)
        currentOffset = pair.second
    }

    val metadata = numbers.slice(currentOffset until currentOffset + numberOfMetaData)
    currentOffset += numberOfMetaData

    return Pair(Node(children, metadata), currentOffset)
}

private fun sumOfMetadata(node: Node): Int {
    return node.metadata.sum() + node.children.map { sumOfMetadata(it) }.sum()
}

private fun puzzleSum(node: Node): Int {
    if (node.children.isEmpty()) {
        return node.metadata.sum()
    }

    return node.metadata
            .filter { node.children.size >= it }
            .map { node.children[it - 1] }
            .map { puzzleSum(it) }
            .sum()
}