package day04

import readInput

fun main() {
    fun part1(input: List<String>): Int {
        return input.map { it.toElfPair() }.count { it.doesEnvelop() }
    }

    fun part2(input: List<String>): Int {
        return input.map { it.toElfPair() }.count { it.doesOverlap() }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    val input = readInput("Day04")

    println("test part1: ${part1(testInput)}")
    println("result part1: ${part1(input)}")

    println("test part2: ${part2(testInput)}")
    println("result part2: ${part2(input)}")
}

fun String.toElfPair(): ElfPair {
    val first = substringBefore(",")
    val second = substringAfter(",")
    return ElfPair(
        Range(first.substringBefore("-").toInt(), first.substringAfter("-").toInt()),
        Range(second.substringBefore("-").toInt(), second.substringAfter("-").toInt())
    )
}

data class ElfPair(val first: Range, val second: Range) {
    fun doesEnvelop(): Boolean = first.envelops(second) || second.envelops(first)
    fun doesOverlap(): Boolean = first.overlaps(second)
}

data class Range(val lower: Int, val upper: Int) {
    fun envelops(other: Range): Boolean =
        lower <= other.lower && upper >= other.upper

    fun overlaps(other: Range): Boolean = lower <= other.upper && upper >= other.lower
}
