package day11

import day05.splitAtEmpty
import readInput

fun main() {
    fun part1(input: List<String>): Long {
        val monkeys = input.parseMonkeys()

        for (round in 0 until 20) {
            monkeys.mapValues { it.value.inspect() }
        }

        return monkeys.map { it.value.inspectCount }
            .sorted()
            .takeLast(2)
            .reduce { left, right -> left * right }
    }

    fun part2(input: List<String>): Long {
        val monkeys = input.parseMonkeys()

        val base = monkeys.values.fold(1L) { acc, it -> acc * it.testNumber }
        for (round in 1..10_000) {
            monkeys.mapValues {
                it.value.inspect2(base)
            }
        }

        return monkeys.map { it.value.inspectCount }
            .sorted()
            .takeLast(2)
            .reduce { left, right -> left * right }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test")
    val input = readInput("Day11")

    println("test part1: ${part1(testInput)}")
    println("result part1: ${part1(input)}")

    println("test part2: ${part2(testInput)}")
    println("result part2: ${part2(input)}")
}

fun Map<Int, Monkey>.prettyPrint(round: Int) {
    println("== After round $round ==")
    this.forEach { println("Monkey ${it.key}: ${it.value.inspectCount}") }
    println()
}

data class Monkey(val items: MutableList<Long>, val testNumber: Int, val operation: (Long) -> Long, val test: (Long) -> Monkey) {
    var inspectCount = 0L

    fun inspect() {
        inspectCount += items.size
        items.map { operation(it) }
            .map { it.floorDiv(3) }
            .forEach { test(it).items.add(it) }
        items.clear()
    }

    fun inspect2(base: Long) {
        inspectCount += items.size
        items.map { operation(it) }
            .map { it.mod(base) }
            .forEach { test(it).items.add(it) }
        items.clear()
    }
}

fun List<String>.parseMonkeys(): Map<Int, Monkey> {
    val monkeys = mutableMapOf<Int, Monkey>()
    this.splitAtEmpty()
        .map { monkeyDef ->
            val (key) = Regex("""(\d+)""").find(monkeyDef[0])!!.destructured
            val items = Regex("""(\d+)+""").findAll(monkeyDef[1]).map { match ->
                match.groupValues[1]
            }.toList().map { it.toLong() }.toMutableList()
            val (operand, rhs) = Regex("""Operation: new = old ([+\-*]) (\d+|old)""").find(monkeyDef[2])!!.destructured
            val test = Regex("""Test: divisible by (\d+)""").find(monkeyDef[3])!!.destructured.component1().toInt()
            val (truthyMonkey) = Regex("""If true: throw to monkey (\d+)""").find(monkeyDef[4])!!.destructured
            val (faultyMonkey) = Regex("""If false: throw to monkey (\d+)""").find(monkeyDef[5])!!.destructured

            key.toInt() to Monkey(items, test, {
                when (operand) {
                    "+" -> it + parseRightHandSide(it, rhs)
                    "-" -> it - parseRightHandSide(it, rhs)
                    else -> it * parseRightHandSide(it, rhs)
                }
            })
            {
                when (it % test) {
                    0L -> monkeys[truthyMonkey.toInt()]!!
                    else -> monkeys[faultyMonkey.toInt()]!!
                }
            }
        }
        .forEach { monkeys[it.first] = it.second }
    return monkeys
}

fun parseRightHandSide(old: Long, rhs: String) = when (rhs) {
    "old" -> old
    else -> rhs.toLong()
}
