package data

import kotlin.math.pow

data class Chain(
    val tree: Tree
) {
    private val numberList: List<Int>? =
        if (tree.roots.isEmpty()) {
            tree.value.split("->").map { it.trim().toInt() }
        } else {
            null
        }

    fun next(): Chain {
        val list = numberList
        return when (list?.size) {
            0, 1 -> this
            2 -> Chain((list[0].toDouble().pow(list[1])).toInt().toString())
            null -> {
                val firstLeaf = tree.getFirstLeaf()
                val firstLeafChain = Chain(firstLeaf)
                val firstLeafNextIteration = firstLeafChain.next()
                Chain(tree.set(firstLeaf.number, firstLeafNextIteration.tree.value))
            }

            else -> {
                if (list.contains(1)) {
                    Chain(list.takeWhile { it != 1 }.joinToString("->"))
                } else {
                    val last = list.takeLast(2)
                    val leftover = list.dropLast(2).joinToString("->")
                    Chain("$leftover->($leftover->${last[0] - 1}->${last[1]})->${last[1] - 1}")
                }
            }
        }
    }
}

fun Chain(input: String): Chain = Chain(Tree.parse(input))