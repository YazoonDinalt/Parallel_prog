package org.example.Tree

import kotlinx.coroutines.sync.Mutex
import org.example.Balancer.ThinBalancer
import org.example.Node.BSNode
import org.example.Node.ValuePair

class ThinTree<E : Comparable<E>, T> (
    private val balancer: ThinBalancer<E, T>
) {

    private val mainMutex = Mutex()

    private var root: BSNode<E, T>? = null

    suspend fun add(value: ValuePair<E, T>) {

        mainMutex.lock()

        if (root != null) {
            root?.lock()
            mainMutex.unlock()
            root = balancer.add(root, value)
        } else {
            root = BSNode(value)
            mainMutex.unlock()
        }
    }

    suspend fun remove(key: E) {
        mainMutex.lock()

        if (root?.currentNode?.key == key) {
            root?.lock()
            root = balancer.remove(root, key)
            mainMutex.unlock()
        } else {
            root?.lock()
            mainMutex.unlock()
            root = balancer.remove(root, key)
        }

    }

    suspend fun search (key: E): Boolean {
        root?.lock()
        return balancer.search(root, key)
    }

}

