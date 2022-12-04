package day2

import day2.Move.*
import day2.Outcome.*
import util.resAsText

enum class Move(val points: Int) {
    Rock(1),
    Paper(2),
    Scissors(3);

    companion object {
        fun fromTheirs(theirs: String): Move = when (theirs) {
            "A" -> Rock
            "B" -> Paper
            "C" -> Scissors
            else -> throw IllegalArgumentException()
        }

        fun fromOurs(ours: String): Move = when (ours) {
            "X" -> Rock
            "Y" -> Paper
            "Z" -> Scissors
            else -> throw IllegalArgumentException()
        }
    }
}

enum class Outcome(val points: Int) {
    Lose(0),
    Draw(3),
    Win(6);

    companion object {
        fun parse(o: String): Outcome = when (o) {
            "X" -> Lose
            "Y" -> Draw
            "Z" -> Win
            else -> throw IllegalArgumentException()
        }
    }
}

val beats = mapOf(
    Rock to Scissors,
    Scissors to Paper,
    Paper to Rock
)
val loses = beats.entries.associate{(k,v)-> v to k}

fun asMoves(input: List<String>): List<Pair<Move, Move>> = input.map {
    val s = it.split(" ")
    Move.fromTheirs(s[0]) to Move.fromOurs(s[1])
}

fun asPlays(input: List<String>): List<Pair<Move, Outcome>> = input.map {
    val s = it.split(" ")
    Move.fromTheirs((s[0])) to Outcome.parse(s[1])
}

fun main() {
    val testMoves = asMoves(resAsText(2, "test.txt").lines())
    val actualMoves = asMoves(resAsText(2, "input.txt").lines())
    val actualPlays = asPlays(resAsText(2, "input.txt").lines())

    check(calculateStrategyPart1(testMoves) == 15)
    println(calculateStrategyPart1(actualMoves))
    println(calculateStrategyPart2(actualPlays))
}

fun calculateStrategyPart1(moves: List<Pair<Move, Move>>): Int =
    moves.sumOf { (theirs, ours) -> calculateOutcome(theirs, ours) + ours.points }

fun calculateStrategyPart2(plays: List<Pair<Move, Outcome>>) : Int =
    plays.sumOf { (theirs, outcome) -> outcome.points + calculateOurMove(theirs, outcome).points }

fun calculateOurMove(theirs: Move, outcome: Outcome) : Move = when (outcome) {
    Lose -> beats[theirs]!!
    Draw -> theirs
    Win -> loses[theirs]!!
}

fun calculateOutcome(theirs: Move, ours: Move): Int = if (theirs == beats[ours]) 6 else if (ours == beats[theirs]) 0 else 3