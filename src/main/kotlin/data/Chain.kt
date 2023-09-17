package data

import kotlin.math.pow

data class Chain(
    val value: String
) {
    val numberList: List<Int> = value.split("->").map { it.trim().toInt() }

    fun next(): Chain {
        return when (numberList.size) {
            0, 1 -> this
            2 -> Chain((numberList[0].toDouble().pow(numberList[1])).toString())
            else -> Chain(numberList.takeWhile { it != 1 }.joinToString("->"))
        }
    }
}