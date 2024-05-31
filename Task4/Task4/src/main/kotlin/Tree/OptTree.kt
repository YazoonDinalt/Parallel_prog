package org.example.Tree

import org.example.Balancer.OptBalancer
import org.example.Node.BSNode
import org.example.Node.ValuePair


class OptTree<E : Comparable<E>, T> (
    private val balancer: OptBalancer<E, T>
) {

    var root: BSNode<E, T>? = null

    suspend fun add(value: ValuePair<E, T>) {
       root = balancer.add(root, value)
    }

    suspend fun delete(key: E) {
        root = balancer.remove(root, key)
    }

    suspend fun search(key: E): Boolean {
        return balancer.search(root, key)
    }

}