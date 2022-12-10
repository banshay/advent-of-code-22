package day10

import readInput

fun main() {
    fun part1(input: List<String>): Int {
        val program = input.toCycleSignalStrengthMap()
        val relevantCycles = 20..220 step 40
        return relevantCycles.sumOf {
            val x = when (program[it]) {
                null -> program[it + 1]!!.during()
                else -> program[it]!!.during()
            }
            it * x
        }
    }

    fun part2(input: List<String>): Int {
        val cols = 40
        val rows = 6
        val program = input.toCycleSignalStrengthMap()

        var currentInstruction = program[0]!!
        val screen = List(rows) { row ->
            List(cols) { col ->
                val cycle = row * cols + col + 1
                currentInstruction = program[cycle] ?: currentInstruction
                val x = program[cycle]?.during() ?: currentInstruction.after()
                when (col) {
                    x - 1 -> "#"
                    x -> "#"
                    x + 1 -> "#"
                    else -> "."
                }
            }
        }

        screen.prettyPrint()
        return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    val input = readInput("Day10")

    println("test part1: ${part1(testInput)}")
    println("result part1: ${part1(input)}")

    println("test part2: ${part2(testInput)}")
    println("result part2: ${part2(input)}")
}

fun List<String>.toCycleSignalStrengthMap(): Map<Int, Instruction> =
    this.scan(0 to Instruction(1, 0)) { prev, rawInstruction ->
        val key = prev.first + when {
            rawInstruction.contains("noop") -> 1
            else -> 2
        }
        key to Instruction(
            prev.second.after(), when {
                rawInstruction.contains("noop") -> 0
                else -> {
                    val match = Regex("""addx (-?\d+)+""").find(rawInstruction)!!
                    val (number) = match.destructured
                    number.toInt()
                }
            }
        )
    }
        .toMap()

fun List<List<String>>.prettyPrint() {
    this.forEach {
        println(it.joinToString(""))
    }
}

data class Instruction(val X: Int, val change: Int) {
    fun during() = X
    fun after() = X + change
}
