package day09

import readInput

fun main() {
    fun part1(input: List<String>): Int {
        val head = Head(0 to 0, null)
        val tail = createRopeSegment(0 to 0, 1, head)!!

        val visitedTailSpots = mutableSetOf<Pair<Int, Int>>()
        val moves = input.map { it.toMove() }
        val size = moves.maxOf { it.steps } + 1

        moves.forEach {
            for (i in 0 until it.steps) {
                head.move(it.direction)
                visitedTailSpots.add(tail.position)
//                visualize(size, head, tail, visitedTailSpots)
            }
        }
        return visitedTailSpots.count()
    }

    fun part2(input: List<String>): Int {
        val start = 11 to 5
        val head = Head(start, null)

        var tail = createRopeSegment(start, 9, head)!!
        while (tail.next != null) {
            tail = tail.next!!
        }

        val visitedTailSpots = mutableSetOf<Pair<Int, Int>>()
        val moves = input.map { it.toMove() }
        val size = moves.maxOf { it.steps } + 1

        moves.forEach {
            for (i in 0 until it.steps) {
                head.move(it.direction)
                visitedTailSpots.add(tail.position)
//                visualize(size, head, tail, visitedTailSpots)
            }
//            visualize(size, head, tail, visitedTailSpots)
        }
        return visitedTailSpots.count()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    val testInput2 = readInput("Day09_test2")
    val input = readInput("Day09")

    println("test part1: ${part1(testInput)}")
    println("result part1: ${part1(input)}")

    println("test part2: ${part2(testInput2)}")
    println("result part2: ${part2(input)}")
}

fun createRopeSegment(start: Pair<Int, Int>, segments: Int, prev: Rope, counter: Int = 1): Rope? {
    if (segments <= 0) {
        return null
    }
    val segment = Tail(counter, start, prev, null)
    prev.next = segment
    segment.next = createRopeSegment(start, segments - 1, segment, counter + 1)
    return segment
}

fun segmentToList(segment: Rope?): List<Rope> {
    if (segment != null) {
        return listOf(segment) + segmentToList(segment.next)
    }
    return listOf()
}

fun visualize(size: Int, head: Head, tail: Rope, visits: Set<Pair<Int, Int>>) {
    val segmentPositions =
        segmentToList(head)
            .drop(1)
            .dropLast(1)
            .mapIndexed { index, rope -> rope.position to index + 1 }.toMap()
    println(segmentPositions)
    val field = List(size) { row ->
        List(size) { col ->
            when {
                col to row == head.position -> "H"
                segmentPositions.containsKey(col to row) -> segmentPositions[col to row]
                col to row == tail.position -> "T"
                visits.contains(col to row) -> "#"
                else -> "."
            }
        }
    }.reversed()

    field.forEach { row -> println(row.joinToString("")) }
    println()
    println()
}

interface Rope {
    var position: Pair<Int, Int>
    var next: Rope?

    fun follow()
}

class Head(override var position: Pair<Int, Int>, override var next: Rope?) : Rope {

    fun move(direction: Direction) {
        position = when (direction) {
            Direction.UP -> position.first to position.second + 1
            Direction.DOWN -> position.first to position.second - 1
            Direction.LEFT -> position.first - 1 to position.second
            Direction.RIGHT -> position.first + 1 to position.second
        }
        next?.follow()
    }

    override fun follow() {
        TODO("Not yet implemented")
    }
}

class Tail(
    private val segment: Int,
    override var position: Pair<Int, Int>,
    private val ahead: Rope?,
    override var next: Rope?
) : Rope {

    override fun follow() {
        position = when (ahead?.position?.minus(position)) {
            0 to 2 -> position.first to position.second + 1 //UP
            2 to 0 -> position.first + 1 to position.second //RIGHT
            0 to -2 -> position.first to position.second - 1 //DOWN
            -2 to 0 -> position.first - 1 to position.second //LEFT
            1 to 2, 2 to 1, 2 to 2 -> position.first + 1 to position.second + 1 //UP_RIGHT
            1 to -2, 2 to -1, 2 to -2 -> position.first + 1 to position.second - 1 //DOWN_RIGHT
            -2 to -1, -1 to -2, -2 to -2 -> position.first - 1 to position.second - 1 //DOWN_LEFT
            -2 to 1, -1 to 2, -2 to 2 -> position.first - 1 to position.second + 1 //UP_LEFT
            else -> position
        }

        next?.follow()
    }
}

enum class Direction(val code: String) {
    UP("U"), DOWN("D"), LEFT("L"), RIGHT("R");

    companion object {
        private val map = Direction.values().associateBy { it.code }
        infix fun from(value: String) = map[value]!!
    }
}

data class Move(val direction: Direction, val steps: Int)

fun String.toMove() = Move(Direction.from(substringBefore(" ")), substringAfter(" ").toInt())
infix fun Pair<Int, Int>.minus(other: Pair<Int, Int>) = first - other.first to second - other.second
