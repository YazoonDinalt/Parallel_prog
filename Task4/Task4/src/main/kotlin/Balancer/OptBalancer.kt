package org.example.Balancer

import org.example.Node.BSNode
import org.example.Node.ValuePair


class OptBalancer<E : Comparable<E>, T>: TreeBalancer<E, T>   {

    override suspend fun add(root: BSNode<E, T>?, currentNode: ValuePair<E, T>): BSNode<E, T>{
        TODO()
    }

    override suspend fun remove(root: BSNode<E, T>?, key: E): BSNode<E, T>? {
        TODO()
    }

    override suspend fun search(node: BSNode<E, T>?, key: E): Boolean {
        TODO()
    }


}