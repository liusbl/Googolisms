package hyperoperation
//// TESTS
//fun main() {
////    val emptyIndexGraph = IndexGraph(
////        index = 0,
////        usualFen = "-",
////        fullFen = "--",
////        isLegal = true,
////        move = Move.WHITE,
////        checkState = CheckState.WHITE_IN_CHECK,
////        parentIndexList = mutableListOf(),
////        nextIndexList = 5
////    )
//}


// level 1 = addition (ignore
// level 2 = multiplication = base * height
// level 3 = exponentiation = base ^ height = (base * base + ... * base) height times
// level 4 = tetration = (base ^ (base ^ (base ^ ... ^ base)))) height times
//fun hyperOperation(base: Int, height: Int, level: Int): Int {
//    val multiply = { a: Int, n: Int -> a * n }
//}

fun main() {
//    val res = Hyperop(4).execute(3, 3) // 4 ^^ 5 = 1024
//    println(res)

    val resList = mutableListOf<String>()
//    (2..8L).forEach { base ->
//        (2..8L).forEach { height ->
//            (2..8L).forEach { level ->
//                val res = hyperoperate(base = base, height = height, level = level)
//                val message = "Base: $base, height: $height, level: $level. Result: ${if (res == -1L) "BigBoy" else res}"
//                println(message)
//                if (res != -1L) {
//                    resList.add(message)
//                }
//            }
//        }
//    }

    println("------- Valid Answers -------")
    resList.forEach { println(it) }

    val res = hyperoperate3(base = 3, height = 3, level = 4)
    println(res)
}

fun hyperoperate4(base: Long, height: Long, level: Long): Long =
    if (level == 0L) {
        base + 1
    } else {
        (1L..height).map { base }.reduce { acc, _ ->
            hyperoperate(base = base, height = acc, level = level - 1)
        }
    }

// a OP0 b = a + 1
// a OP1 b = a + b = a OP0 (a OP0 (a ... a), where number of a is b
// a OP2 b = a * b = a OP1 (a OP1 (a ... a), where number of a is b
fun hyperoperate3(base: Long, height: Long, level: Long): Long =
    if (level == 1L) {
        base + height
    } else {
        (1L..height).map { base }.reduce { acc, _ ->
            hyperoperate3(base = base, height = acc, level = level - 1)
        }
    }


// From https://www.geeksforgeeks.org/knuths-up-arrow-notation-for-exponentiation/
fun hyperoperate2(base: Long, height: Long, level: Long): Long =
    if (level == 1L) {
        Math.pow(base.toDouble(), height.toDouble()).toLong()
    } else {
        hyperoperate2(base, hyperoperate2(base, height - 1, level), level - 1)
    }

// My salution
fun hyperoperate(base: Long, height: Long, level: Long): Long =
    if (level == 2L) {
        base * height
    } else {
        (1L..height).map { base }.reduce { acc, _ ->
            hyperoperate(base = base, height = acc, level = level - 1)
        }
    }

data class Hyperop(val level: Long) {
    fun execute(base: Long, height: Long): Long =
        if (level == 2L) {
            base * height
        } else {
            val lesserOperation = this.decrement()
            (1L..height).map { base }.reduce { acc, _ ->
                lesserOperation.execute(base, acc)
            }
        }

    fun decrement(): Hyperop {
        return Hyperop(level = level - 1)
    }
}

/**
a op b = (a op-1 (a op-1 (a op-1 ... a), where "..." is b

decrementing "op" in the end becomes a*b
 */