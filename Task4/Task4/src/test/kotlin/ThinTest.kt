import kotlin.random.Random
import kotlinx.coroutines.*
import org.example.Balancer.ThinBalancer
import org.example.Node.ValuePair
import org.example.Tree.ThinTree
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test


class TestsThinBST{
    private val count: Int = 100
    private val tree = ThinTree<Int, Int>(
        ThinBalancer()
    )
    @Test
    fun `one thread test`() = runBlocking {
        repeat(10) {

            val random = java.util.Random()
            val pairsList = mutableListOf<ValuePair<Int, Int>>()

            repeat(count) {
                val mockData = random.nextInt(100)
                pairsList.add(ValuePair(mockData, mockData))
            }

            repeat(count) {
                tree.add(pairsList[it])
            }

            repeat(count) {
                tree.remove(pairsList[it].key)
            }

            for (number in pairsList) {
                assertEquals(false, tree.search(number.key))
            }
        }

    }

    @Test
    fun `parallel test`() = runBlocking {
        repeat(10) {

            val random = java.util.Random()
            val pairsList = mutableListOf<ValuePair<Int, Int>>()

            repeat(count) {
                val mockData = random.nextInt(100)
                pairsList.add(ValuePair(mockData, mockData))
            }

            val headNodes = pairsList.subList(0, pairsList.size / 2)
            val tailNodes = pairsList.subList(pairsList.size / 2, pairsList.size)

            coroutineScope {
                launch {
                    repeat(count / 2) {
                        delay(50)
                        tree.add(headNodes[it])
                    }
                }

                launch {
                    repeat(count / 2) {
                        delay(50)
                        tree.add(tailNodes[it])
                    }
                }

            }

            val nodesToDelete = pairsList.shuffled(Random).take(count / 2)
            val headNodesToDelete = nodesToDelete.subList(0, nodesToDelete.size / 2)
            val tailNodesToDelete = nodesToDelete.subList(nodesToDelete.size / 2, nodesToDelete.size)

            coroutineScope {
                launch {
                    repeat(count / 4) {
                        delay(50)
                        tree.remove(headNodesToDelete[it].key)
                    }
                }

                launch {
                    repeat(count / 4) {
                        delay(50)
                        tree.remove(tailNodesToDelete[it].key)
                    }
                }
            }

            for (number in pairsList) {
                if (number !in nodesToDelete) {
                    assertEquals(true, tree.search(number.key))
                } else {
                    assertEquals(false, tree.search(number.key))
                }
            }
        }
    }
}