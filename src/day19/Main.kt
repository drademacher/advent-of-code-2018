package day19

import java.io.File


fun main(args: Array<String>) {
    println("part 1: " + partOne())
    println("part 2: " + partTwo())
}

private fun partOne(): Int {
    val (instructionPointer, instructions) = parseFile()

    var register = Register(IntArray(6), instructionPointer)

    while (register.getInstructionNumber() in 0 until instructions.size) {
        register = instructions[register.getInstructionNumber()](register)
        register.incrementInstructionNumber()
    }

    return register.getByRegister(0)
}

private fun partTwo(): Int {
    val (instructionPointer, instructions) = parseFile()

    var register = Register(IntArray(6), instructionPointer)
    register.setByRegister(0, 1)


    while (register.getInstructionNumber() in 0 until instructions.size) {
        register = instructions[register.getInstructionNumber()](register)
        register.incrementInstructionNumber()
    }

    return register.getByRegister(0)
}


private fun parseFile(): Pair<Int, List<(Register) -> Register>> {
    val rawFile = File("res/day-19.txt").readText()
            .split("\n")
            .filter { it != "" }

    val instructionPointer = Character.getNumericValue(rawFile[0][4])
    val instructions = rawFile
            .drop(1)
            .map { it.split(" ") }
            .map { { input: Register -> mapStringToRegisterFunction(it[0])(input, it[1].toInt(), it[2].toInt(), it[3].toInt()) } }
    return Pair(instructionPointer, instructions)
}
