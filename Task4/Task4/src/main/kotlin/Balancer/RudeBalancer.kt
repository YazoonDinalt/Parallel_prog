package org.example.Balancer

import org.example.Node.BSNode
import org.example.Node.ValuePair

class RudeBalancer<E : Comparable<E>, T>: TreeBalancer<E, T>  {

    override suspend fun add(root: BSNode<E, T>?, currentNode: ValuePair<E, T>): BSNode<E, T> {
        return when {
            root == null -> BSNode(currentNode)
            currentNode.key < root.currentNode.key ->  {
                root.left = add(root.left, currentNode)
                root
            }
            currentNode.key > root.currentNode.key -> {
                root.right = add(root.right, currentNode)
                root
            }
            else ->  root
        }
    }

    override suspend fun remove(root: BSNode<E, T>?, key: E): BSNode<E, T>? {
        return when {
            root == null -> null
            key < root.currentNode.key -> {
                root.left = remove(root.left, key)
                root
            }

            key >  root.currentNode.key -> {
                root.right = remove(root.right, key)
                root
            }

            else -> {
                if (root.left == null || root.right == null) {
                    return root.left ?: root.right
                } else {
                    val temp =
                        minValueNode(
                            root.right
                                ?: throw IllegalStateException("Impossible to find minValueNode from node without right child")
                        )
                    root.currentNode = temp.currentNode
                    root.right = remove(root.right, temp.currentNode.key)
                }
                root
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

    override tailrec suspend fun search(node: BSNode<E, T>?, key: E): Boolean {
        return when {
            node == null -> false
            key == node.currentNode.key -> true
            else -> search((if (key < node.currentNode.key) node.left else node.right), key)
        }
    }


}
