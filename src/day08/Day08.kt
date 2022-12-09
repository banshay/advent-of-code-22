package day08

import readInput

fun main() {
    fun part1(input: List<String>): Int {
        val intInput = input.map { row -> row.map { it.toString().toInt() } }

        val forest = createForest(intInput)

        return forest.flatten().count { it.visible() }
    }

    fun part2(input: List<String>): Int {
        val intInput = input.map { row -> row.map { it.toString().toInt() } }

        val forest = createForest(intInput)

        return forest.flatten().maxOf { it.scenicScore() }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    val input = readInput("Day08")

    println("test part1: ${part1(testInput)}")
    println("result part1: ${part1(input)}")

    println("test part2: ${part2(testInput)}")
    println("result part2: ${part2(input)}")
}

fun createForest(input: List<List<Int>>): List<List<Tree>> {
    return List(input.size) { row ->
        List(input[0].size) { col ->
            val treeRow = input[row]
            val treeCol = input.map { it[col] }
            Tree(
                treeRow[col], mapOf(
                    Direction.UP to treeCol.slice(0 until row).reversed(),
                    Direction.DOWN to treeCol.slice(row + 1 until treeCol.size),
                    Direction.LEFT to treeRow.slice(0 until col).reversed(),
                    Direction.RIGHT to treeRow.slice(col + 1 until treeRow.size)
                )
            )
        }
    }
}

enum class Direction {
    UP, DOWN, LEFT, RIGHT
}

data class Tree(val size: Int, val directionMap: Map<Direction, List<Int>>) {
    fun visible(): Boolean {
        return directionMap.values.any { it.isEmpty() } || directionMap.values.any { list -> list.all { size > it } }
    }

    fun scenicScore(): Int {
        return directionMap.values.map { trees ->
            val view = trees.takeWhile { size > it }.count()
            if (view == trees.size) view else view + 1
        }
            .fold(1) { acc, it -> acc * it }
    }
}
