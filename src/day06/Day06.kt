package day06

import readInput

fun main() {
    fun part1(input: List<String>): Int {
        return input.map { it.toSignal().indexOfStartSignal() }[0]
    }

    fun part2(input: List<String>): Int {
        return input.map { it.toSignal().indexOfMessageStart() }.also { println(it) }[0]
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    val input = readInput("Day06")

    println("test part1: ${part1(testInput)}")
    println("result part1: ${part1(input)}")

    println("test part2: ${part2(testInput)}")
    println("result part2: ${part2(input)}")
}

fun String.toSignal(): Signal = Signal(this)

data class Signal(val signal: String) {

    fun indexOfStartSignal(): Int {
        val startSignal = signal.windowed(size = 4, step = 1)
            .map { it.toSet() }
            .first { it.size == 4 }
            .joinToString("")
        return signal.indexOf(startSignal) + 4
    }

    fun indexOfMessageStart(): Int {
        val messageSignal = signal.windowed(14)
            .map { it.toSet() }
            .first { it.size == 14 }
            .joinToString("")
        return signal.indexOf(messageSignal) + 14
    }
}
