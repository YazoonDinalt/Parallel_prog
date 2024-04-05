package org.example

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.system.measureNanoTime
import kotlin.system.measureTimeMillis

fun main() = runBlocking{

    testStaticWithout()
    testStaticWith()

    testRandomWithout()
    testRandomWith()
}


suspend fun testStaticWithout() = coroutineScope{
    val stack = TreiberStackWithoutElimination<Int>()

    val executionTime = measureNanoTime {

        launch {
            repeat(1000) {
                stack.push(1)
            }
        }

        launch {
            repeat(1000) {
                stack.pop()
            }
        }

    }

    println("Время выполнения на статических данных без элиминации: $executionTime нс")

}

suspend fun testStaticWith() = coroutineScope{
    val stack = TreiberStackWithElimination<Int>()

    val executionTime = measureNanoTime {

        launch {
            repeat(1000) {
                stack.push(1)
            }
        }

        launch {
            repeat(1000) {
                stack.pop()
            }
        }

    }

    println("Время выполнения на статических данных с элиминацей: $executionTime нс")

}

suspend fun testRandomWithout() = coroutineScope{

    val stack = TreiberStackWithoutElimination<Int>()

    val executionTime = measureNanoTime {

        launch {
            repeat(1000) {
                stack.push((1..100).random())
            }
        }

        launch {
            repeat(1000) {
                stack.pop()
            }
        }

    }

    println("Время выполнения на случайных данных без элиминации: $executionTime нс")
}

suspend fun testRandomWith() = coroutineScope{

    val stack = TreiberStackWithElimination<Int>()

    val executionTime = measureNanoTime {

        launch {
            repeat(1000) {
                stack.push((1..100).random())
            }
        }

        launch {
            repeat(1000) {
                stack.pop()
            }
        }

    }

    println("Время выполнения на случайных данных с элиминацией: $executionTime нс")
}