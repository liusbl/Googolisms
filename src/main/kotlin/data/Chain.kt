package data

import kotlin.math.pow

data class Chain(
    val tree: Tree
) {
    val numberList: List<Int>?
        get() {
            return if (tree.roots.isEmpty()) {
                tree.value.split("->").map { it.trim().toInt() }
            } else {
                null
            }
        }

    fun next(): Chain {
        val list = numberList
        return when (list?.size) {
            0, 1 -> this
            2 -> Chain(Tree.parse((list[0].toDouble().pow(list[1])).toInt().toString()))
            null -> {
                val firstLeaf = tree.getFirstLeaf()
                val firstLeafChain = Chain(firstLeaf)
                val firstLeafNextIteration = firstLeafChain.next()
                Chain(tree.set(firstLeaf.number, firstLeafNextIteration.tree.value))
            }
            else -> Chain(Tree.parse(list.takeWhile { it != 1 }.joinToString("->")))
        }
    }
}