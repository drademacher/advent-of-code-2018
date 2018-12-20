import java.io.File
import kotlin.reflect.KFunction4

data class RegisterState(var a: Int, var b: Int, var c: Int, var d: Int) {
    fun getByPointer(register: Int): Int {
        return when (register) {
            0 -> this.a
            1 -> this.b
            2 -> this.c
            3 -> this.d
            else -> throw Exception("irregular register number")
        }
    }

    fun setByPointer(register: Int, value: Int): RegisterState {
        when (register) {
            0 -> this.a = value
            1 -> this.b = value
            2 -> this.c = value
            3 -> this.d = value
            else -> throw Exception("irregular register number")
        }
        return this
    }
}

fun main(args: Array<String>) {
    // runTests()
    println("part 1: " + partOne())
    println("part 2: " + partTwo())
}

private fun partOne(): Int {
    val rawFile = File("res/day-16a.txt").readText()
            .split("\n")
            .filter { it != "" }

    var result = 0
    for (i in 0 until rawFile.size step 3) {

        val fstNumbers = rawFile[i].drop(9).dropLast(1).split(", ").map(String::toInt)
        val initialState = RegisterState(fstNumbers[0], fstNumbers[1], fstNumbers[2], fstNumbers[3])
        val instruction = rawFile[i + 1].split(" ").map(String::toInt)
        val sndNumbers = rawFile[i + 2].drop(9).dropLast(1).split(", ").map(String::toInt)
        val resultingState = RegisterState(sndNumbers[0], sndNumbers[1], sndNumbers[2], sndNumbers[3])

        val numberOfPossibleFunctions = getAllFunctions()
                .map { func -> func(initialState, instruction[1], instruction[2], instruction[3]) }
                .filter { it == resultingState }
                .size

        if (numberOfPossibleFunctions >= 3) {
            result += 1
        }
    }

    return result
}

private fun partTwo(): Int {
    val rawFileA = File("res/day-16a.txt").readText()
            .split("\n")
            .filter { it != "" }

    val mapping = HashMap<Int, KFunction4<RegisterState, Int, Int, Int, RegisterState>>()

    val unknownFunctions = getAllFunctions().toMutableList()
    val knownInstructions = mutableSetOf<Int>()


    for (k in 0..16) {
        for (i in 0 until rawFileA.size step 3) {
            val fstNumbers = rawFileA[i].drop(9).dropLast(1).split(", ").map(String::toInt)
            val initialState = RegisterState(fstNumbers[0], fstNumbers[1], fstNumbers[2], fstNumbers[3])
            val instruction = rawFileA[i + 1].split(" ").map(String::toInt)
            val sndNumbers = rawFileA[i + 2].drop(9).dropLast(1).split(", ").map(String::toInt)
            val resultingState = RegisterState(sndNumbers[0], sndNumbers[1], sndNumbers[2], sndNumbers[3])

            if (instruction[0] in knownInstructions) {
                continue
            }

            val numberOfPossibleFunctions = unknownFunctions
                    .map { func -> func.call(initialState, instruction[1], instruction[2], instruction[3]) }
                    .filter { it == resultingState }
                    .size
            if (numberOfPossibleFunctions == 1) {
                val function = unknownFunctions
                        .first { func -> func.call(initialState, instruction[1], instruction[2], instruction[3]) == resultingState }

                mapping[instruction[0]] = function
                unknownFunctions.remove(function)
                knownInstructions.add(instruction[0])
            }
        }
    }


    val rawFileB = File("res/day-16b.txt").readText()
            .split("\n")
            .filter { it != "" }

    var currentState = RegisterState(0, 0, 0, 0)

    for (line in rawFileB) {
        val instruction = line.split(" ").map(String::toInt)
        currentState = mapping[instruction[0]]!!.call(currentState, instruction[1], instruction[2], instruction[3])
    }

    return currentState.getByPointer(0)

}

fun getAllFunctions(): List<KFunction4<RegisterState, Int, Int, Int, RegisterState>> {
    return listOf(
            RegisterState::addr,
            RegisterState::addi,
            RegisterState::mulr,
            RegisterState::muli,
            RegisterState::banr,
            RegisterState::bani,
            RegisterState::borr,
            RegisterState::bori,
            RegisterState::setr,
            RegisterState::seti,
            RegisterState::gtir,
            RegisterState::gtri,
            RegisterState::gtrr,
            RegisterState::eqir,
            RegisterState::eqri,
            RegisterState::eqrr
    )
}

fun RegisterState.addr(x: Int, y: Int, z: Int): RegisterState {
    val value = getByPointer(x) + getByPointer(y)
    return this.copy().setByPointer(z, value)
}

fun RegisterState.addi(x: Int, y: Int, z: Int): RegisterState {
    val value = getByPointer(x) + y
    return this.copy().setByPointer(z, value)
}

fun RegisterState.mulr(x: Int, y: Int, z: Int): RegisterState {
    val value = getByPointer(x) * getByPointer(y)
    return this.copy().setByPointer(z, value)
}

fun RegisterState.muli(x: Int, y: Int, z: Int): RegisterState {
    val value = getByPointer(x) * y
    return this.copy().setByPointer(z, value)
}

fun RegisterState.banr(x: Int, y: Int, z: Int): RegisterState {
    val value = getByPointer(x).and(getByPointer(y))
    return this.copy().setByPointer(z, value)
}

fun RegisterState.bani(x: Int, y: Int, z: Int): RegisterState {
    val value = getByPointer(x).and(y)
    return this.copy().setByPointer(z, value)
}

fun RegisterState.borr(x: Int, y: Int, z: Int): RegisterState {
    val value = getByPointer(x).or(getByPointer(y))
    return this.copy().setByPointer(z, value)
}

fun RegisterState.bori(x: Int, y: Int, z: Int): RegisterState {
    val value = getByPointer(x).or(y)
    return this.copy().setByPointer(z, value)
}

fun RegisterState.setr(x: Int, ignore: Int, z: Int): RegisterState {
    return this.copy().setByPointer(z, getByPointer(x))
}

fun RegisterState.seti(x: Int, ignore: Int, z: Int): RegisterState {
    return this.copy().setByPointer(z, x)
}

fun RegisterState.gtir(x: Int, y: Int, z: Int): RegisterState {
    val value = if (x > getByPointer(y)) 1 else 0
    return this.copy().setByPointer(z, value)
}

fun RegisterState.gtri(x: Int, y: Int, z: Int): RegisterState {
    val value = if (getByPointer(x) > y) 1 else 0
    return this.copy().setByPointer(z, value)
}

fun RegisterState.gtrr(x: Int, y: Int, z: Int): RegisterState {
    val value = if (getByPointer(x) > getByPointer(y)) 1 else 0
    return this.copy().setByPointer(z, value)
}

fun RegisterState.eqir(x: Int, y: Int, z: Int): RegisterState {
    val value = if (x == getByPointer(y)) 1 else 0
    return this.copy().setByPointer(z, value)
}

fun RegisterState.eqri(x: Int, y: Int, z: Int): RegisterState {
    val value = if (getByPointer(x) == y) 1 else 0
    return this.copy().setByPointer(z, value)
}

fun RegisterState.eqrr(x: Int, y: Int, z: Int): RegisterState {
    val value = if (getByPointer(x) == getByPointer(y)) 1 else 0
    return this.copy().setByPointer(z, value)
}

private fun runTests() {
    val state = RegisterState(3, 2, 1, 1)

    println(state.addi(2, 1, 2).c == 2)
    println(state.addr(2, 1, 2).c == 3)
    println(state.mulr(2, 1, 2).c == 2)
    println(state.muli(2, 1, 2).c == 1)
    println(state.seti(2, 1, 2).c == 2)
    println(state)


}