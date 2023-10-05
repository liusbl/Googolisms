package data

import kotlin.math.pow

/**
 * So, these are probably the rules (don't exactly remember)
 * 1. a -> b = a^b
 * 2. # -> 1 -> a = #
 * 3. # -> a -> b = # -> (# -> (a-1) -> b) -> (b-1)
 *
 * These can be implied:
 * 1. a -> b -> c = a TT..TT b, with C T's
 * 2. # -> 1 = #
 *
 * TODO: is there a generalization for this?
 */
fun main() {
    println("Hello. Input the chain:")
    var input = readln()
    println("Press Enter to advance the calculation.")
    while (true) {
        readln()
        val chain = Chain(input)
        input = chain.next().value
        println(input)
    }

//    testNoNumbers()
//    testOneNumber()
//    testTwoNumbers()
//    testChainWithOne()
//    testChainWithOneAndNumbers()
//    testtttttt()
}

fun operate(input: String): String {
    val tree = Tree.parse(input)
    val chain = Chain(tree)

    println(chain.tree.value)
    println()
    println(chain.next().tree.value)
    println()
    println(chain.next().next().tree.value)
    println()
    println(chain.next().next().next().tree.value)
    println()
    println(chain.next().next().next().next().tree.value)

    return "aaa"
}


fun testNoNumbers() {
    test(
        input = "",
        expected = "0"
    )
}

fun testOneNumber() {
    test(
        input = "4",
        expected = "4"
    )
}

fun testTwoNumbers() {
    test(
        input = "4->5",
        expected = "1024"
    )
}

fun testChainWithOne() {
    test(
        input = "2->4->5->1",
        expected = "2->4->5"
    )
}

fun testChainWithOneAndNumbers() {
    test(
        input = "2->4->5->1->6->7",
        expected = "2->4->5"
    )
}

fun testttt() {
    test(
        input = "1->((2->3)->(4->(5->6)))->(7->8->9)",
        expected = "2->4->5"
    )
}

fun testtttttt() {
    test(
        input = "3->5->7->9",
        expected = "2->4->5"
    )
}


fun test(input: String, expected: String) {
    val actual = operate(input)
    if (actual == expected) {
        println("Success. input: $input, result: $actual")
    } else {
        System.err.println("Failed. input: $input, expected: $actual, actual: $actual")
    }
}