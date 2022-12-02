fun main() {
    fun part1(input: List<String>): Int {
        return input.size
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(testInput.size == 1)

    println("test part1: ${part1(testInput)}")
    println("test part2: ${part2(testInput)}")

    val input = readInput("Day01")
    println("result part1: ${part1(input)}")
    println("result part2: ${part2(input)}")
}
