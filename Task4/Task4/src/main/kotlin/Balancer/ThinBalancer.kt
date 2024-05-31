package org.example.Balancer

import org.example.Node.BSNode
import org.example.Node.ValuePair

class ThinBalancer<E : Comparable<E>, T>: TreeBalancer<E, T> {

    override suspend fun add(root: BSNode<E, T>?, currentNode: ValuePair<E, T>): BSNode<E, T> {
        return when {
            root == null -> {
                BSNode(currentNode)
            }

            currentNode.key < root.currentNode.key -> {
                root.left?.lock()
                root.unlock()
                root.left = add(root.left, currentNode)
                root
            }

            currentNode.key > root.currentNode.key -> {
                root.right?.lock()
                root.unlock()
                root.right = add(root.right, currentNode)
                root
            }

            else -> {
                root.unlock()
                root
            }
        }
    }

    override suspend fun remove(root: BSNode<E, T>?, key: E): BSNode<E, T>? {
        return when {
            root == null -> null
            key == root.currentNode.key -> {
                when {
                    root.left == null -> {
                        root.unlock()
                        root.right
                    }
                    root.right == null -> {
                        root.unlock()
                        root.left
                    }
                    else -> {
                        root.right?.lock()
                        val temp =
                            minValueNode(
                                root.right
                                    ?: throw IllegalStateException("Impossible to find minValueNode from node without right child")
                            )
                        root.currentNode = temp.currentNode
                        root.right = remove(root.right, temp.currentNode.key)
                        root.unlock()
                        root
                    }
                }
            }

            key < root.currentNode.key -> {
                if (root.left == null ){
                    root.unlock()
                    null
                } else {
                    root.left?.lock()
                    root.unlock()
                    root.left = remove(root.left, key)
                    root
                }
            }
            else -> {
                if (root.right == null ){
                    root.unlock()
                    null
                } else {
                    root.right?.lock()
                    root.unlock()
                    root.right = remove(root.right, key)
                    root
                }
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

    override suspend fun search(node: BSNode<E, T>?, key: E): Boolean {
        return when {
            node == null -> false
            key == node.currentNode.key -> {
                node.unlock()
                return true
            }
            key < node.currentNode.key  -> {
                return if (node.left != null) {
                    node.unlock()
                    node.left?.lock()
                    search(node.left, key)
                } else {
                    node.unlock()
                    false
                }
            }

            else -> {
                return if (node.right != null) {
                    node.unlock()
                    node.right?.lock()
                    search(node.right, key)
                } else {
                    node.unlock()
                    false
                }
            }

        }
    }



}