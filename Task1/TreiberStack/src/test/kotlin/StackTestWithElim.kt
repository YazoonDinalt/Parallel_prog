import org.example.TreiberStackWithElimination
import org.example.TreiberStackWithoutElimination
import org.jetbrains.kotlinx.lincheck.annotations.Operation
import org.jetbrains.kotlinx.lincheck.check
import org.jetbrains.kotlinx.lincheck.strategy.stress.StressOptions
import kotlin.test.Test
import org.jetbrains.kotlinx.lincheck.strategy.managed.modelchecking.ModelCheckingOptions
import org.junit.jupiter.api.Assertions.assertEquals


class EliminationStackTest {
    private val stack = TreiberStackWithElimination<Int>()

    @Operation
    fun pop() = stack.pop()

    @Operation
    fun top() = stack.top()

    @Operation
    fun push(value: Int) = stack.push(value)

    @Test
    fun stressTest() = StressOptions()
        .actorsBefore(2)
        .threads(2)
        .actorsPerThread(2)
        .actorsAfter(1)
        .iterations(100)
        .invocationsPerIteration(1000)
        .check(this::class)

    @Test
    fun `Stress test for many count invocation`() = StressOptions()
        .actorsBefore(2)
        .threads(2)
        .actorsPerThread(2)
        .actorsAfter(1)
        .iterations(500)
        .invocationsPerIteration(10000)
        .check(this::class)

    @Test
    fun modelCheckingTest() = ModelCheckingOptions()
        .hangingDetectionThreshold(10_000)
        .actorsBefore(2)
        .threads(2)
        .actorsPerThread(2)
        .actorsAfter(1)
        .iterations(100)
        .invocationsPerIteration(1000)
        .check(this::class)

}

class StackDriverTestWithElimination {
    private val stack = TreiberStackWithElimination<Int>()

    @Test
    fun `test pushing element to stack`() {
        stack.push(42)
        assertEquals(42, stack.top())
    }

    @Test
    fun `test popping element from stack`() {
        stack.push(100)
        val poppedElement = stack.pop()
        assertEquals(100, poppedElement)
    }

    @Test
    fun `test peeking element from stack`() {
        stack.push(10)
        stack.push(20)
        assertEquals(20, stack.top())
    }

}
