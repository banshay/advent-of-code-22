package day02

import day02.FixedOutcome.*
import readInput

fun main() {
    fun part1(input: List<String>): Int {
        return input.sumOf {
            it.toRound().score()
        }
    }

    fun part2(input: List<String>): Int {
        return input.sumOf { it.toFixedRound().score() }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")

    println("test part1: ${part1(testInput)}")
    println("test part2: ${part2(testInput)}")

    val input = readInput("Day02")
    println("result part1: ${part1(input)}")
    println("result part2: ${part2(input)}")
}

interface Scoring {
    fun score(): Int
}

enum class OpponentChoice(val code: String) {
    ROCK("A") {
        override fun fixOutcome(fixedOutcome: FixedOutcome): PlayerChoice {
            return when (fixedOutcome) {
                LOSE -> PlayerChoice.SCISSOR
                DRAW -> PlayerChoice.ROCK
                WIN -> PlayerChoice.PAPER
            }
        }
    },
    PAPER("B") {
        override fun fixOutcome(fixedOutcome: FixedOutcome): PlayerChoice {
            return when (fixedOutcome) {
                LOSE -> PlayerChoice.ROCK
                DRAW -> PlayerChoice.PAPER
                WIN -> PlayerChoice.SCISSOR
            }
        }
    },
    SCISSOR("C") {
        override fun fixOutcome(fixedOutcome: FixedOutcome): PlayerChoice {
            return when (fixedOutcome) {
                LOSE -> PlayerChoice.PAPER
                DRAW -> PlayerChoice.SCISSOR
                WIN -> PlayerChoice.ROCK
            }
        }
    };

    abstract fun fixOutcome(fixedOutcome: FixedOutcome): PlayerChoice

    companion object {
        private val map = OpponentChoice.values().associateBy { it.code }
        infix fun from(code: String): OpponentChoice =
            map[code] ?: throw RuntimeException("OpponentChoice with value $code does not exist")
    }
}

enum class Outcome : Scoring {
    LOSS {
        override fun score() = 0
    },
    DRAW {
        override fun score() = 3
    },
    WIN {
        override fun score() = 6
    }
}

enum class FixedOutcome(val code: String) : Scoring {
    LOSE("X") {
        override fun score() = 0
    },
    DRAW("Y") {
        override fun score() = 3
    },
    WIN("Z") {
        override fun score() = 6
    };

    companion object {
        private val map = values().associateBy { it.code }
        infix fun from(code: String): FixedOutcome =
            map[code] ?: throw RuntimeException("FixedOutcome with value $code does not exist")
    }
}

enum class PlayerChoice(val code: String) : Scoring {
    ROCK("X") {
        override fun score() = 1
        override fun outcome(opponentChoice: OpponentChoice): Outcome {
            return when (opponentChoice) {
                OpponentChoice.ROCK -> Outcome.DRAW
                OpponentChoice.PAPER -> Outcome.LOSS
                OpponentChoice.SCISSOR -> Outcome.WIN
            }
        }
    },
    PAPER("Y") {
        override fun score() = 2
        override fun outcome(opponentChoice: OpponentChoice): Outcome {
            return when (opponentChoice) {
                OpponentChoice.ROCK -> Outcome.WIN
                OpponentChoice.PAPER -> Outcome.DRAW
                OpponentChoice.SCISSOR -> Outcome.LOSS
            }
        }
    },
    SCISSOR("Z") {
        override fun score() = 3
        override fun outcome(opponentChoice: OpponentChoice): Outcome {
            return when (opponentChoice) {
                OpponentChoice.ROCK -> Outcome.LOSS
                OpponentChoice.PAPER -> Outcome.WIN
                OpponentChoice.SCISSOR -> Outcome.DRAW
            }
        }
    };

    abstract fun outcome(opponentChoice: OpponentChoice): Outcome

    companion object {
        private val map = PlayerChoice.values().associateBy { it.code }
        infix fun from(code: String): PlayerChoice =
            map[code] ?: throw RuntimeException("PlayerChoice with value $code does not exist")
    }
}

data class Round(val opponentChoice: OpponentChoice, val playerChoice: PlayerChoice) : Scoring {
    override fun score(): Int {
        return playerChoice.score() + playerChoice.outcome(opponentChoice).score()
    }
}

data class FixedRound(val opponentChoice: OpponentChoice, val fixedOutcome: FixedOutcome) : Scoring {
    override fun score(): Int {
        return opponentChoice.fixOutcome(fixedOutcome).score() + fixedOutcome.score()
    }
}

fun String.toRound() = Round(OpponentChoice from substringBefore(" "), PlayerChoice from substringAfter(" "))
fun String.toFixedRound() = FixedRound(OpponentChoice from substringBefore(" "), FixedOutcome from substringAfter(" "))


