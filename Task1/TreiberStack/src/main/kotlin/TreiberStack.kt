package org.example

import kotlinx.atomicfu.AtomicRef

abstract class TreiberStack<E> {

    abstract val top: AtomicRef<Node<E>?>

    abstract fun pop(): E?

    abstract fun push(x: E)

    abstract fun top(): E?

    fun printStack(){
        var node = top.value
        var val1 = node?.x
        while (val1 != null) {
            print("$val1 ")
            node = node?.next
            val1 = node?.x
        }
        println()
    }

}