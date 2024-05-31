package org.example.Tree

import org.example.Balancer.OptBalancer
import org.example.Node.BSNode
import org.example.Node.ValuePair


class OptTree<E : Comparable<E>, T> (
    private val balancer: OptBalancer<E, T>
) {

    var root: BSNode<E, T>? = null

    suspend fun add(currentNode: ValuePair<E, T>) {
        root = add(root, currentNode, null)
    }

    suspend fun add(localRoot: BSNode<E, T>?, currentNode: ValuePair<E, T>, parent: BSNode<E, T>?): BSNode<E, T> {
        return if (localRoot == null) {
            BSNode(currentNode, parent = parent)
        } else {
            when {
                currentNode.key < localRoot.currentNode.key -> {
                    if (localRoot.left == null) {
                        localRoot.lock()
                        localRoot.parent?.lock()
                        val newNode = doubleSearch(localRoot, localRoot.currentNode.key)
                        if (newNode == localRoot) {
                            localRoot.left = BSNode(currentNode, parent = parent)
                            localRoot.unlock()
                            localRoot.parent?.unlock()

                        } else {
                            add(localRoot, currentNode, null)
                        }
                    } else {
                        localRoot.left = add(localRoot.left, currentNode, null)
                    }
                    localRoot
                }

                currentNode.key > localRoot.currentNode.key -> {
                    if (localRoot.right == null) {
                        localRoot.lock()
                        localRoot.parent?.lock()
                        val newNode = doubleSearch(localRoot, localRoot.currentNode.key)
                        if (newNode == localRoot) {
                            localRoot.right = BSNode(currentNode, parent = parent)
                            localRoot.unlock()
                            localRoot.parent?.unlock()
                        } else {
                            root = add(localRoot, currentNode, null)
                        }
                    } else {
                        localRoot.right = add(localRoot.right, currentNode, localRoot)
                    }
                    localRoot
                }

                else -> localRoot
            }
        }
    }

    suspend fun remove(key: E) {
        root = remove(root, key)
    }

    suspend fun search(key: E): Boolean {
        return search(root, key)
    }

    private suspend fun search(node: BSNode<E, T>?, key: E): Boolean {
        return if (node == null) {
            false
        } else {
            when {
                key == node.currentNode.key -> {
                    node.parent?.lock()
                    node.lock()
                    val newNode = doubleSearch(root, key)
                    if (newNode == node) {
                        node.unlock()
                        node.parent?.unlock()
                        return true
                    } else {
                        search(root, key)
                    }
                }
                key < node.currentNode.key -> search(node.left, key)
                else -> search(node.right, key)
            }
        }
    }

    private suspend fun remove(node: BSNode<E, T>?, key: E): BSNode<E, T>? {

        return when {
            node == null -> null
            key == node.currentNode.key -> {
                node.parent?.lock()
                node.lock()
                if (doubleSearch(root, node.currentNode.key) == node) {
                    when {
                        node.left == null -> {
                            node.parent?.unlock()
                            node.unlock()
                            node.right
                        }

                        node.right == null -> {
                            node.parent?.unlock()
                            node.unlock()
                            node.left
                        }

                        else -> {
                            val temp =
                                minValueNode(
                                    root?.right
                                        ?: throw IllegalStateException("Impossible to find minValueNode from node without right child")
                                )
                            node.right?.lock()
                            node.right = balancer.remove(node.right, temp.currentNode.key)
                            root?.currentNode = temp.currentNode
                            node.parent?.unlock()
                            node.unlock()
                            node
                        }
                    }
                } else {
                    node.parent?.unlock()
                    node.unlock()
                    remove(root, key)
                }
            }

            key < node.currentNode.key -> {
                node.left = remove(node.left, key)
                node
            }

            else -> {
                node.right = remove(node.right, key)
                node
            }
        }
    }

    private fun doubleSearch(node: BSNode<E, T>?, key: E): BSNode<E, T>? {
        return if (node == null) {
            null
        } else {
            when {
                key == node.currentNode.key -> node
                key < node.currentNode.key -> doubleSearch(node.left, key)
                else -> doubleSearch(node.right, key)
            }
        }
    }

    private fun minValueNode(node: BSNode<E, T>): BSNode<E, T> {
        var current = node
        while (true) {
            current = current.left ?: break
        }
        return current
    }


}