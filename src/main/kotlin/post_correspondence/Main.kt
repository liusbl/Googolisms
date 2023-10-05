package post_correspondence

fun main() {
    shouldWork2()

    // To generate all possible solution you need to permute everything
}

fun shouldWork() {
    val list1 = listOf("a", "ab", "bba")
    val list2 = listOf("baa", "aa", "bb")
    val sequence = listOf(2, 1, 2, 0)

    println(checkPostCorrespondence(list1, list2, sequence))
}

fun shouldWork2() {
    val list1 = listOf("bb", "ab", "c")
    val list2 = listOf("b", "ba", "bc")
    val sequence = listOf(0, 1, 1, 2)

    println(checkPostCorrespondence(list1, list2, sequence))
}

fun checkPostCorrespondence(list1: List<String>, list2: List<String>, sequence: List<Int>): Boolean {
    println("-----------")
    println("Start data. " +
            "\nlist1: $list1" +
            "\nlist2: $list2" +
            "\nsequence: $sequence"
    )
    val combined1 = sequence.joinToString("") { list1[it] }
    val combined2 = sequence.joinToString("") { list2[it] }
    println("Combined1: $combined1")
    println("Combined2: $combined2")
    return (combined1 == combined2).also {
        println("Result: $it")
        println("-----------")
    }
}