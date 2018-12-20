package day19

import kotlin.reflect.KFunction4

data class Register(val memory: IntArray, val instructionPointer: Int) {
    fun getByRegister(register: Int): Int {
        return memory[register]
    }

    fun setByRegister(register: Int, value: Int): Register {
        memory[register] = value
        return this
    }

    fun getInstructionNumber() = memory[instructionPointer]
    fun incrementInstructionNumber() {
        memory[instructionPointer] += 1
    }
}

fun Register.addr(x: Int, y: Int, z: Int): Register {
    val value = getByRegister(x) + getByRegister(y)
    return this.copy().setByRegister(z, value)
}

fun Register.addi(x: Int, y: Int, z: Int): Register {
    val value = getByRegister(x) + y
    return this.copy().setByRegister(z, value)
}

fun Register.mulr(x: Int, y: Int, z: Int): Register {
    val value = getByRegister(x) * getByRegister(y)
    return this.copy().setByRegister(z, value)
}

fun Register.muli(x: Int, y: Int, z: Int): Register {
    val value = getByRegister(x) * y
    return this.copy().setByRegister(z, value)
}

fun Register.banr(x: Int, y: Int, z: Int): Register {
    val value = getByRegister(x).and(getByRegister(y))
    return this.copy().setByRegister(z, value)
}

fun Register.bani(x: Int, y: Int, z: Int): Register {
    val value = getByRegister(x).and(y)
    return this.copy().setByRegister(z, value)
}

fun Register.borr(x: Int, y: Int, z: Int): Register {
    val value = getByRegister(x).or(getByRegister(y))
    return this.copy().setByRegister(z, value)
}

fun Register.bori(x: Int, y: Int, z: Int): Register {
    val value = getByRegister(x).or(y)
    return this.copy().setByRegister(z, value)
}

fun Register.setr(x: Int, ignore: Int, z: Int): Register {
    return this.copy().setByRegister(z, getByRegister(x))
}

fun Register.seti(x: Int, ignore: Int, z: Int): Register {
    return this.copy().setByRegister(z, x)
}

fun Register.gtir(x: Int, y: Int, z: Int): Register {
    val value = if (x > getByRegister(y)) 1 else 0
    return this.copy().setByRegister(z, value)
}

fun Register.gtri(x: Int, y: Int, z: Int): Register {
    val value = if (getByRegister(x) > y) 1 else 0
    return this.copy().setByRegister(z, value)
}

fun Register.gtrr(x: Int, y: Int, z: Int): Register {
    val value = if (getByRegister(x) > getByRegister(y)) 1 else 0
    return this.copy().setByRegister(z, value)
}

fun Register.eqir(x: Int, y: Int, z: Int): Register {
    val value = if (x == getByRegister(y)) 1 else 0
    return this.copy().setByRegister(z, value)
}

fun Register.eqri(x: Int, y: Int, z: Int): Register {
    val value = if (getByRegister(x) == y) 1 else 0
    return this.copy().setByRegister(z, value)
}

fun Register.eqrr(x: Int, y: Int, z: Int): Register {
    val value = if (getByRegister(x) == getByRegister(y)) 1 else 0
    return this.copy().setByRegister(z, value)
}

fun mapStringToRegisterFunction(name: String): KFunction4<Register, Int, Int, Int, Register> {
    return when (name) {
        "addr" -> Register::addr
        "addi" -> Register::addi
        "mulr" -> Register::mulr
        "muli" -> Register::muli
        "banr" -> Register::banr
        "bani" -> Register::bani
        "borr" -> Register::borr
        "bori" -> Register::bori
        "setr" -> Register::setr
        "seti" -> Register::seti
        "gtir" -> Register::gtir
        "gtri" -> Register::gtri
        "gtrr" -> Register::gtrr
        "eqir" -> Register::eqir
        "eqri" -> Register::eqri
        "eqrr" -> Register::eqrr
        else -> throw Exception("invalid function name")
    }
}