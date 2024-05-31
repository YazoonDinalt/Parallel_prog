package org.example.Node

import kotlinx.coroutines.sync.Mutex

class BSNode<E : Comparable<E>, T>(
    var currentNode: ValuePair<E, T>,
    var left: BSNode<E, T>? = null,
    var right: BSNode<E, T>? = null,
    var parent: BSNode<E, T>? = null
)  {
    private val mutex = Mutex()
    suspend fun lock() = mutex.lock()
    fun unlock() = mutex.unlock()
}
