package data

import operate


fun Tree.graphString(): String {
    return mapRoots(this, 0, roots)
//        return "value"
}

private fun mapRoots(tree: Tree, depth: Int, roots: List<Tree>): String =
    (listOf("  ".repeat(depth) + tree.value) + roots.mapNotNull { root ->
        mapRoots(root, depth + 1, root.roots)
    }.filter { it.isNotBlank() })
        .joinToString("\n")


data class Tree(
    val value: String,
    val roots: List<Tree>
) {
    companion object {
        fun parse(input: String): Tree = simplify(Tree(value = input, emptyList()))

        fun simplify(tree: Tree): Tree {
            val input = tree.value
            var startBracketIndex: Int? = null
            var endBracketIndex: Int? = null
            var startBracketCount = 0
            val treeList = mutableListOf<Tree>()
            input.forEachIndexed { index, char ->
                if (char == '(') {
                    startBracketCount++
                    if (startBracketCount == 1) {
                        startBracketIndex = index
                    }
                } else if (char == ')') {
                    startBracketCount--
                    if (startBracketCount <= 0) {
                        endBracketIndex = index

                        // Now the magic here
                        if (startBracketIndex != null && endBracketIndex != null) {
                            val substring = input.substring(startBracketIndex!! + 1, endBracketIndex!!)

                            // Reset start/end indexes to be able to continue loop .
                            startBracketIndex = null
                            endBracketIndex = null

                            if (substring.isEmpty()) {
                                // tree
                            } else {
                                treeList.add(simplify(Tree(substring, emptyList())))
                            }
                        } else {
                            // tree
                        }
                    }
                }
            }
            return Tree(tree.value, treeList)
        }
    }
}

fun main() {
    val tree0 = Tree.simplify(Tree("1 -> 2", emptyList()))
    println(tree0)
    println(tree0.graphString())
    println()
    val tree1 = Tree.simplify(Tree("1 -> (2 -> 3) -> 4", emptyList()))
    println(tree1)
    println(tree1.graphString())
    println()
    val tree2 = Tree.simplify(Tree("1 -> ((2 -> 3) -> 4) -> 5", emptyList()))
    println(tree2)
    println(tree2.graphString())
    println()
    val tree3 = Tree.simplify(Tree("1 -> ((2 -> 3) -> 4) -> (5 -> 6)", emptyList()))
    println(tree3)
    println(tree3.graphString())
    println()
    val tree4 = Tree.simplify(Tree("1 -> ((2 -> 3) -> (4 -> (5 -> 6))) -> (7 -> 8 -> 9)", emptyList()))
    println(tree4)
    println(tree4.graphString())
    println()
}