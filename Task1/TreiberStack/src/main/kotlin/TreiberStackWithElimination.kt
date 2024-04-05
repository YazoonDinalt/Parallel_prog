package org.example

import kotlinx.atomicfu.atomic
import kotlinx.atomicfu.atomicArrayOfNulls

@Suppress("UNCHECKED_CAST")
class TreiberStackWithElimination<E> : TreiberStack<E>() {
    override val top = atomic<Node<E>?>(null)
    private val eliminationArray = atomicArrayOfNulls<Any?>(8)

    private enum class Done {
        DONE
    }

    override fun push(x: E) {
        if (putInArrayAndWait(x)) {
            while (true) {
                val curTop: Node<E>? = top.value
                val newTop: Node<E> = Node(x, curTop)
                if (top.compareAndSet(curTop, newTop)) {
                    return
                }
            }
        }
    }

    override fun top(): E? =  top.value?.x

    override fun pop(): E? {
        for (i in 0..<eliminationArray.size) {
            val cur = eliminationArray[i].value
            if (cur == null || cur is Done) {
                continue
            }
            if (eliminationArray[i].compareAndSet(cur, Done.DONE)) {
                return (cur as Node<E>).x
            }
        }

        while (true) {
            val curTop: Node<E> = top.value ?: return null
            val newTop: Node<E>? = curTop.next
            if (top.compareAndSet(curTop, newTop)) {
                return curTop.x
            }
        }
    }

    private fun putInArrayAndWait(x: E): Boolean {
        val nodeToPut: Node<E> = Node(x, null)
        var index: Int = -1
        var waitingTime = 10
        for (i in 0..<eliminationArray.size) {
            if (eliminationArray[i].compareAndSet(null, nodeToPut)) {
                index = i
                break
            }
        }
        if (index == -1) {
            return true
        }
        while(waitingTime != 0) {
            if (eliminationArray[index].value == Done.DONE) {
                eliminationArray[index].value = null
                return false
            }
            waitingTime -= 1
        }
        if (eliminationArray[index].compareAndSet(nodeToPut, null)) {
            return true
        }
        eliminationArray[index].compareAndSet(Done.DONE, null)
        return false
    }
}
