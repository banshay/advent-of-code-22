package day07

import readInput

fun main() {
    fun part1(input: List<String>): Long {
        val filesystem = Filesystem()
        input.map { it.toStatement() }
            .forEach { it.accept(filesystem) }

        val threshold: Long = 100_000
        return filesystem.root.flatten().filter { it.size() <= threshold }.fold(0L) { acc, it -> acc + it.size() }
    }

    fun part2(input: List<String>): Long {
        val filesystem = Filesystem()
        input.map { it.toStatement() }
            .forEach { it.accept(filesystem) }

        val freeSpace = 70_000_000L - filesystem.root.size()
        val requiredSpace = 30_000_000L - freeSpace

        return filesystem.root.flatten().map { it.size() }.reversed().first { it >= requiredSpace }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    val input = readInput("Day07")

    println("test part1: ${part1(testInput)}")
    println("result part1: ${part1(input)}")

    println("test part2: ${part2(testInput)}")
    println("result part2: ${part2(input)}")
}

fun String.toStatement() = if (startsWith("$")) Input(this) else Output(this)

interface Statement {
    val instruction: String
    fun accept(visitor: FilesystemVisitor)
}

class Input(override val instruction: String) : Statement {
    enum class Command {
        CD,
        LS
    }

    fun type(): Command {
        return when {
            instruction.startsWith("$ cd") -> Command.CD
            instruction.startsWith("$ ls") -> Command.LS
            else -> throw IllegalArgumentException("Unknown command in $instruction")
        }
    }

    fun getDir(): String = instruction.substringAfter("$ cd ")

    override fun accept(visitor: FilesystemVisitor) {
        visitor.visit(this)
    }
}

class Output(override val instruction: String) : Statement {

    fun toContent(currentDir: Directory): Content {
        return when {
            instruction.startsWith("dir ") -> Directory(currentDir, instruction.substringAfter("dir "), mutableListOf())
            else -> File(currentDir, instruction.substringAfter(" "), instruction.substringBefore(" ").toLong())
        }
    }

    override fun accept(visitor: FilesystemVisitor) {
        visitor.visit(this)
    }
}

interface FilesystemVisitor {
    fun visit(statement: Input)
    fun visit(statement: Output)
}

interface Content {
    val parent: Content?
    val name: String
    fun size(): Long
}

data class Directory(override val parent: Directory?, override val name: String, val contents: MutableList<Content>) :
    Content {

    override fun size(): Long {
        return contents.fold(0) { acc, it -> acc + it.size() }
    }

    fun flatten(): List<Directory> {
        val directories = mutableListOf<Directory>()
        this.flattenTo(directories)
        return directories
    }

    private fun flattenTo(destination: MutableCollection<Directory>) {
        destination.add(this)
        contents.filterIsInstance<Directory>().forEach { it.flattenTo(destination) }
    }

    override fun toString(): String {
        return "$name (${size()})"
    }
}

data class File(override val parent: Content?, override val name: String, val size: Long) : Content {
    override fun size(): Long {
        return size
    }

    override fun toString(): String {
        return "$name ($size)"
    }
}

class Filesystem : FilesystemVisitor {
    private val _root = Directory(null, "/", mutableListOf())
    private var currentDir = _root

    val root get() = _root

    override fun visit(statement: Input) {
        when (statement.type()) {
            Input.Command.CD -> currentDir = when (statement.getDir()) {
                "/" -> _root
                ".." -> currentDir.parent ?: currentDir
                else -> currentDir.contents.find { it.name == statement.getDir() } as Directory
            }

            else -> {
            }
        }
    }

    override fun visit(statement: Output) {
        currentDir.contents.add(statement.toContent(currentDir))
    }

    override fun toString(): String {
        return _root.toString()
    }
}
