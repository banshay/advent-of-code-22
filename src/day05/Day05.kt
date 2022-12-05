package day05

import readInput
import java.util.*

fun main() {
    fun part1(input: List<String>): String {
        val (stacksUnparsed, instructionStrings) = input.splitAtEmpty()
        val instructions = instructionStrings.map { it.parseInstruction() }
        val cargoArea = stacksUnparsed.parseStacks()

        instructions.forEach { cargoArea.applyInstruction(it) }
        return cargoArea.topOfEachStack()
    }

    fun part2(input: List<String>): String {
        val (stacksUnparsed, instructionStrings) = input.splitAtEmpty()
        val instructions = instructionStrings.map { it.parseInstruction() }
        val cargoArea = stacksUnparsed.parseStacks9001()

        instructions.forEach { cargoArea.applyInstruction(it) }
        return cargoArea.topOfEachStack()
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    val input = readInput("Day05")

    println("test part1: ${part1(testInput)}")
    println("result part1: ${part1(input)}")

    println("test part2: ${part2(testInput)}")
    println("result part2: ${part2(input)}")
}

fun List<String>.splitAtEmpty(): List<List<String>> = this
    .flatMapIndexed { index, x ->
        when {
            index == 0 || index == this.lastIndex -> listOf(index)
            x.isEmpty() -> listOf(index - 1, index + 1)
            else -> emptyList()
        }
    }
    .windowed(size = 2, step = 2) { (from, to) -> this.slice(from..to) }

fun List<String>.parseStacks(): CargoArea = CargoArea(generateStacks(this))

fun List<String>.parseStacks9001(): CargoArea9001 = CargoArea9001(generateStacks(this))

private fun List<String>.generateStacks(strings: List<String>): Map<Int, Stack<Char>> {
    val reversedList = strings.reversed().toMutableList()

    //stack numbers
    val stackNumbersString = reversedList.removeAt(0)
    val matches = Regex("""(\d+)+""").findAll(stackNumbersString)

    val stacks = matches.flatMap { it.destructured.toList() }
        .map { it.toInt() to Stack<Char>() }
        .toMap()
    val stackIndexMap = stacks.keys.associateWith { stackNumbersString.indexOf(it.toString()) }

    reversedList.forEach { horizontalCargoLine ->
        stacks.forEach { stackEntry ->
            val stackIndex = stackIndexMap[stackEntry.key]!!
            if (stackIndex < horizontalCargoLine.length) {
                val stackElement: Char = horizontalCargoLine.elementAt(stackIndex)
                if (stackElement != ' ') {
                    stackEntry.value.push(stackElement)
                }
            }
        }
    }
    return stacks
}

fun String.parseInstruction(): Instruction {
    val (number, from, to) = Regex("""move (\d+) from (\d+) to (\d+)""").find(this)!!.destructured
    return Instruction(number.toInt(), from.toInt(), to.toInt())
}

data class Instruction(val number: Int, val source: Int, val target: Int)

open class CargoArea(open val stacks: Map<Int, Stack<Char>>) {
    open fun applyInstruction(instruction: Instruction) {
        val source = stacks[instruction.source]!!
        val target = stacks[instruction.target]!!
        for (i in 1..instruction.number) {
            target.push(source.pop())
        }
    }

    fun topOfEachStack(): String {
        return stacks.values
            .mapNotNull {
                try {
                    it.peek()
                } catch (e: EmptyStackException) {
                    null
                }
            }
            .joinToString("")
    }
}

class CargoArea9001(override val stacks: Map<Int, Stack<Char>>) : CargoArea(stacks) {
    override fun applyInstruction(instruction: Instruction) {
        val source = stacks[instruction.source]!!
        val target = stacks[instruction.target]!!
        val tempStack = Stack<Char>()
        for (i in 1..instruction.number) {
            tempStack.push(source.pop())
        }
        while (!tempStack.empty()) {
            target.push(tempStack.pop())
        }
    }
}
