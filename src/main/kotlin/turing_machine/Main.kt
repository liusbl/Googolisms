package turing_machine

// TODO better way to provide a table for turing machine. Perhaps via some txt file
// TODO validate provided turing machine. For example you could write anything now into the tapeSymbol or writeSymbol, or nextStateSymbol.
fun main() {
    println("Hello world")

    val stateTable = StateTable(
        stateList = listOf(
            State(
                stateName = "A",
                rowList = listOf(
                    StateRow(
                        tapeSymbol = "0",
                        writeSymbol = "1",
                        moveTape = Direction.Right,
                        nextStateName = "B"
                    ),
                    StateRow(
                        tapeSymbol = "1",
                        writeSymbol = "1",
                        moveTape = Direction.Left,
                        nextStateName = "C"
                    )
                )
            ),
            State(
                stateName = "B",
                rowList = listOf(
                    StateRow(
                        tapeSymbol = "0",
                        writeSymbol = "1",
                        moveTape = Direction.Left,
                        nextStateName = "A"
                    ),
                    StateRow(
                        tapeSymbol = "1",
                        writeSymbol = "1",
                        moveTape = Direction.Right,
                        nextStateName = "B"
                    )
                )
            ),
            State(
                stateName = "C",
                rowList = listOf(
                    StateRow(
                        tapeSymbol = "0",
                        writeSymbol = "1",
                        moveTape = Direction.Left,
                        nextStateName = "B"
                    ),
                    StateRow(
                        tapeSymbol = "1",
                        writeSymbol = "1",
                        moveTape = Direction.Right,
                        nextStateName = "HALT"
                    )
                )
            ),
            State(
                stateName = "HALT",
                rowList = emptyList()
            )
        )
    )

    var turingMachine = TuringMachine(
        stateTable = stateTable,
        tape = listOf("0"),
        headIndex = 0,
        headStateName = "A"
    )

    var iteration = 0
    while (true) {
        readlnOrNull()
        iteration++
        turingMachine = turingMachine.next()
        println(
            "iteration: $iteration, " +
                    "${turingMachine.toDisplayString()}"
        )
        if (turingMachine.headStateName == "HALT") {
            break
        }
    }
}

// TODO some of these are constant and others are dynamic. How to signify this.
data class TuringMachine( // TODO Or TuringMachineState?
    val stateTable: StateTable,
    val tape: List<String>, // TODO Theoretically infinite
    val headIndex: Int,
    val headStateName: String
) {
    fun toDisplayString(): String {
        val tapeSymbol = tape[headIndex]
        val headState = stateTable.stateList.find { it.stateName == headStateName }!!
//            ?: if (headStateName == "HALT") State(
//                "HALT",
//                emptyList()
//            ) else error("Invalid head state name: $headStateName")
        return "headIndex: $headIndex, " +
                "headStateName: $headStateName, " +
                "tapeSymbol: \"${tapeSymbol}\", " +
                "nextMove: ${headState.rowList.find { it.tapeSymbol == tapeSymbol }?.moveTape}, " +
                "tape: ${tape.mapIndexed { index, value -> if (index == headIndex) "->$value<-" else value }}"
    }
}

fun TuringMachine.next(): TuringMachine {
    val headState = stateTable.stateList.find { it.stateName == headStateName }!!
    val tapeSymbol = tape[headIndex]
    val stateRow = headState.rowList.find { it.tapeSymbol == tapeSymbol }!!

    val newTapeSymbol = stateRow.writeSymbol
    val newTape: List<String>
    val newHeadIndex: Int
    when (stateRow.moveTape) {
        Direction.Right -> {
            newHeadIndex = headIndex + 1
            newTape = if (newHeadIndex >= tape.size) {
                buildList {
                    addAll(tape.toMutableList().apply {
                        set(headIndex, newTapeSymbol) // TODO probably headIndex needs to be changed
                    })
                    add("0")
                }
            } else {
                tape.toMutableList().apply {
                    set(headIndex, newTapeSymbol) // TODO probably headIndex needs to be changed
                }
            }
        }

        Direction.Left -> {
            newHeadIndex = headIndex - 1
            newTape = if (newHeadIndex < 0) {
                buildList {
                    addAll(tape.toMutableList().apply {
                        set(headIndex, newTapeSymbol) // TODO probably headIndex needs to be changed
                    })
                    add(0, "0")
                }
            } else {
                tape.toMutableList().apply {
                    set(headIndex, newTapeSymbol) // TODO probably headIndex needs to be changed
                }
            }
        }
    }

    return this.copy(
        tape = newTape,
        headIndex = newHeadIndex.coerceAtLeast(0),
        headStateName = stateRow.nextStateName
    )
}

data class StateTable(
    val stateList: List<State>
)

data class State(
    val stateName: String, // TODO this should be some kind of sealed class or custom enum maybe
    val rowList: List<StateRow>
)

data class StateRow(
    val tapeSymbol: String,
    val writeSymbol: String,
    val moveTape: Direction,
    val nextStateName: String // TODO should also somehow include HALT state
)

enum class Direction {
    Right,
    Left
}

/**
 * tape
 * head
 * state register
 * table
 *
 */