fun main() {

    fun part1(input: Elves): Int {
        return input.maxOf { it.value.sum() }
    }

    fun part2(input: Elves): Int {
        val maxList = input.map { it.value.sum() }.sortedDescending()
        return maxList[0] + maxList[1] + maxList[2]
    }

    fun sanitizeInput(input: String): Elves {
        return input.split(System.lineSeparator() + System.lineSeparator())
            .foldIndexed(mutableMapOf()) { i, acc, item ->
                var inventory = item
                //remove trailing newline, prevent it from becoming an inventory position
                if (item.lastIndexOf(System.lineSeparator()) == item.lastIndex + 1 - System.lineSeparator().length) {
                    inventory = item.dropLast(System.lineSeparator().length)
                }
                acc[i] = inventory.split(System.lineSeparator()).map { it.toInt() }
                acc
            }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = sanitizeInput(readInputText("Day01_test"))

    println("test part1: ${part1(testInput)}")
    println("test part2: ${part2(testInput)}")

    val input = sanitizeInput(readInputText("Day01"))


    println("result part1: ${part1(input)}")
    println("result part2: ${part2(input)}")
}

typealias Elves = Map<Int, List<Int>>
