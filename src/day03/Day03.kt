package day03

import readInput

fun main() {
    fun part1(input: List<String>): Int {
        return input.sumOf { rucksackInput ->
            val priority = priorityMap[rucksackInput.toRucksack().getMisplacedType()]
            priority ?: 0
        }
    }

    fun part2(input: List<String>): Int {
        return input.chunked(3)
            .sumOf { group ->
                val sharedType = group.map { rucksackInput -> rucksackInput.toRucksack().toSet() }
                    .fold(mutableSetOf<Char>()) { acc, rucksack ->
                        if (acc.isEmpty()) {
                            acc.addAll(rucksack)
                        }
                        acc.apply { retainAll(rucksack) }
                    }
                priorityMap[sharedType.first()] ?: 0
            }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")

    println("test part1: ${part1(testInput)}")
    println("test part2: ${part2(testInput)}")

    val input = readInput("Day03")
    println("result part1: ${part1(input)}")
    println("result part2: ${part2(input)}")
}

fun String.toRucksack() =
    Rucksack(substring(0, length / 2).toCharArray().toList(), substring(length / 2).toCharArray().toList())


typealias Compartment = List<Char>

data class Rucksack(val leftCompartment: Compartment, val rightCompartment: Compartment) {
    fun getMisplacedType(): Char {
        return leftCompartment.intersect(rightCompartment).first()
    }

    fun toSet(): Set<Char> = (leftCompartment + rightCompartment).toMutableSet()
}


private val alphabet = listOf(
    'a',
    'b',
    'c',
    'd',
    'e',
    'f',
    'g',
    'h',
    'i',
    'j',
    'k',
    'l',
    'm',
    'n',
    'o',
    'p',
    'q',
    'r',
    's',
    't',
    'u',
    'v',
    'w',
    'x',
    'y',
    'z'
)
private val upperAlphabet = alphabet.map { it.uppercase().toCharArray().first() }

private val priorityMap = (alphabet + upperAlphabet).mapIndexed { index, char -> char to index + 1 }.toMap()
