package data


fun Tree.graphString(): String {
    return mapRoots(this, 0, roots)
}

fun Tree.updateTree(onRoot: (Tree) -> Tree): Tree {
    val newTree = onRoot(this)
    val newRoots = roots.map { root ->
        onRoot(root)
        root.updateTree(onRoot)
    }
    return Tree(newTree.index, newTree.value, newTree.isLeaf, newRoots)
}

private fun mapRoots(tree: Tree, depth: Int, roots: List<Tree>): String =
    (listOf("  ".repeat(depth) + "${tree.value} [index:${tree.index}, isLeaf:${tree.isLeaf}]") + roots.map { root ->
        mapRoots(root, depth + 1, root.roots)
    }.filter { it.isNotBlank() })
        .joinToString("\n")


fun Tree.find(predicate: (Tree) -> Boolean): Tree? {
    if (predicate(this)) {
        return this
    } else {
        roots.forEach { tree ->
            return if (predicate(tree)) {
                tree
            } else {
                tree.find(predicate)
            }
        }
    }
    return this
}

fun Tree.getFirstLeaf(): Tree = find { it.isLeaf } ?: this

// TODO does not work properly.
fun Tree.set(index: Int, newTree: Tree): Tree {
    val list = mutableListOf<Tree>()
    if (this.index == index) {
        list.add(Tree(index, newTree.value, newTree.isLeaf, newTree.roots))
    } else {

    }
    return find { it.roots.isEmpty() } ?: this
}

// TODO adding parentTree to this could help solve issues with root replacement
data class Tree(
    val index: Int,
    val value: String,
    val isLeaf: Boolean,
    val roots: List<Tree>
) {
    companion object {
        fun parse(input: String): Tree {
            var additionalIndex = 0

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

                                if (substring.isBlank()) {
                                    // tree
                                } else {
                                    treeList.add(
                                        simplify(
                                            Tree(
                                                index = ++additionalIndex,
                                                value = substring,
                                                isLeaf = false,
                                                roots = emptyList()
                                            )
                                        )
                                    )
                                }
                            } else {
                                // tree
                            }
                        }
                    }
                }
                return Tree(
                    index = tree.index,
                    value = tree.value,
                    isLeaf = false,
                    roots = treeList
                ).let { innerTree ->
                    innerTree.updateTree { it.copy(isLeaf = it.roots.isEmpty()) }
                }
            }

            return simplify(
                Tree(
                    index = 0,
                    value = input,
                    isLeaf = false,
                    roots = emptyList()
                )
            )
        }
    }
}

fun main() {
    test("1 -> 2")
    test("1 -> (2 -> 3) -> 4")
    test("1 -> ((2 -> 3) -> 4) -> 5")
    test("1 -> ((2 -> 3) -> 4) -> (5 -> 6)")
    test("1 -> ((2 -> 3) -> (4 -> (5 -> 6))) -> (7 -> 8 -> 9)")
}

fun test(input: String) {
    val tree = Tree.parse(input)
    println(tree)
    println(tree.graphString())
    val firstLeaf = tree.getFirstLeaf()
    println("First leaf: ${firstLeaf.graphString()}")
    println()
}