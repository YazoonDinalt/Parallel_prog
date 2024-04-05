package org.example

import kotlinx.atomicfu.atomic

class TreiberStackWithoutElimination<E>: TreiberStack<E>() {

    override val top = atomic<Node<E>?>(null)

    override fun pop(): E? {
        while (true) {
        val head = top.value
            if (top.compareAndSet(head, head?.next)) {
                return head?.x
            }
        }
    }

    override fun push(x: E) {
        while (true) {
            val head = top.value
            val newHead = Node(x, head)
            if (top.compareAndSet(head, newHead)) {
                return
            }
        }
    }

    override fun top(): E? {
        val head = top.value
        return head?.x
    }

}