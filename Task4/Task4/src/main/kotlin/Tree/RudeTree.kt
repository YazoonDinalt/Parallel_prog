package org.example.Tree

import kotlinx.coroutines.sync.Mutex
import org.example.Balancer.RudeBalancer
import org.example.Node.BSNode
import org.example.Node.ValuePair

class RudeTree<E : Comparable<E>, T> (
    private val balancer: RudeBalancer<E, T>,
) {
    private val rootMutex = Mutex()

    private var root: BSNode<E, T>? = null

    suspend fun add(value: ValuePair<E, T>) {
        rootMutex.lock()
        root = balancer.add(root, value)
        rootMutex.unlock()
    }

    suspend fun remove(key: E) {
        rootMutex.lock()
        root = balancer.remove(root, key)
        rootMutex.unlock()
    }

    suspend fun search (key: E): Boolean {
        rootMutex.lock()
        val isFind = balancer.search(root, key)
        rootMutex.unlock()
        return isFind
    }

}
