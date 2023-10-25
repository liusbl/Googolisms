package ycombinator

fun main() {
    val factorial = y { factorial: (n: Int) -> Int ->
        { n ->
            if (n == 0) {
                1
            } else {
                n * factorial(n - 1)
            }
        }
    }

    println(factorial(5))
}

/**
 * Source: https://gist.github.com/jubinjacob19/99989899ddb2d8e1d1055730e6ecace4
 */
fun <In, Out> y(f: (_: (n: In) -> Out) -> (n: In) -> Out): (n: In) -> Out {
    return { p -> f(y(f))(p) }
}