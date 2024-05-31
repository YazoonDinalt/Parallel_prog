package org.example.Balancer

import org.example.Node.BSNode
import org.example.Node.ValuePair


interface TreeBalancer<E : Comparable<E>, T> {

    suspend fun add(root: BSNode<E, T>?, currentNode: ValuePair<E, T>): BSNode<E, T>

    suspend fun remove(root: BSNode<E, T>?, key: E): BSNode<E, T>?

    suspend fun search(node: BSNode<E, T>?, key: E): Boolean

}