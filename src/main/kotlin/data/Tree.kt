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
    return Tree(newTree.number, newTree.parentIndex, newTree.value, newTree.isLeaf, newRoots)
}

private fun mapRoots(tree: Tree, depth: Int, roots: List<Tree>): String =
    (listOf(
        "  ".repeat(depth) + "${tree.value} " +
                "[number:${tree.number}, " +
                "isLeaf:${tree.isLeaf}, " +
                "parentIndex: ${tree.parentIndex?.let { "${it.startIndex}|${it.endIndex}" }}" +
                "]"
    ) + roots.map { root ->
        mapRoots(root, depth + 1, root.roots)
    }.filter { it.isNotBlank() })
        .joinToString("\n")


fun Tree.find(predicate: (Tree) -> Boolean): Tree? {
    val list = mutableListOf<Tree>()

    fun thing(root: Tree) {
        list.add(root)
        root.roots.forEach { thing(it) }
    }

    thing(this)

    return list.find { predicate(it) }
}

fun Tree.getFirstLeaf(): Tree = find { it.isLeaf } ?: this

// TODO does not work properly.
fun Tree.set(newTreeNumber: Int, newValue: String): Tree {
    val root = find { it.number == newTreeNumber }
    if (root == null) {
        System.err.println("whot")
    } else {
        val parentIndex = root.parentIndex!!
        val anyRootContainsNumber: (Tree) -> Boolean = { it.roots.any { it.number == newTreeNumber } }
        val parentTree = find(anyRootContainsNumber)!!
        val newParentValue = if (newValue.contains("->")) {
            // TODO NOT SURE ABOUT THIS CHECK THIS AGAIN
            parentTree.value.replaceRange(parentIndex.startIndex, parentIndex.endIndex, newValue)
        } else {
            parentTree.value.replaceRange(parentIndex.startIndex - 1, parentIndex.endIndex + 1, newValue)
        }
        val updatedTree = this.updateTree {
            if (anyRootContainsNumber(it)) {
                it.copy(value = newParentValue)
            } else {
                it
            }
        }
        if (parentTree.number == 0) {
            return Tree.parse(updatedTree.value)
        } else {
            val newerNumber = parentTree.number
            return updatedTree.set(newerNumber, newParentValue)
        }
    }
    error("Should not happen")
}

data class ParentIndex(
    val startIndex: Int,
    val endIndex: Int
)

data class Tree(
    val number: Int,
    val parentIndex: ParentIndex?,
    val value: String,
    val isLeaf: Boolean,
    val roots: List<Tree>
) {
    companion object {
        fun parse(input: String): Tree {
            var number = 0

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

                                val parentIndex = ParentIndex(
                                    startIndex = startBracketIndex!! + 1,
                                    endIndex = endBracketIndex!!
                                )

                                // Reset start/end indexes to be able to continue loop .
                                startBracketIndex = null
                                endBracketIndex = null

                                if (substring.isBlank()) {
                                    // tree
                                } else {
                                    treeList.add(
                                        simplify(
                                            Tree(
                                                number = ++number,
                                                parentIndex = parentIndex,
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
                    number = tree.number,
                    parentIndex = tree.parentIndex,
                    value = tree.value,
                    isLeaf = false,
                    roots = treeList
                ).let { innerTree ->
                    innerTree.updateTree { it.copy(isLeaf = it.roots.isEmpty()) }
                }
            }

            return simplify(
                Tree(
                    number = 0,
                    parentIndex = null,
                    value = input,
                    isLeaf = false,
                    roots = emptyList()
                )
            )
        }
    }
}

fun main() {
//    test("1->2")
//    test("1->(2->3)->4")
//    test("1->((2->3)->4)->5")
//    test("1->((2->3)->4)->(5->6)")
    test("1->((2->3)->(4->(5->6)))->(7->8->9)")
}

fun test(input: String) {
    val tree = Tree.parse(input)
    println(tree)
    println(tree.graphString())
    val firstLeaf = tree.getFirstLeaf()
    println("First leaf: ${firstLeaf.graphString()}")
    val treeWithX = tree.set(4, "X")
    println(treeWithX.graphString())
    println()
    val treeWithXY = treeWithX.set(2, "Y")
    println(treeWithXY.graphString())
    println()
}