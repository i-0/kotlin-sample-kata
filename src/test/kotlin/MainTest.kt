import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

fun Int.sum10(): Int = this + 10

class MainTest {
    @Test
    fun `please give me good name`() {
        assertTrue { true == true }
        assertEquals("foo", "bar")
    }

    @Test
    fun `fun adds 10 to a given Int`() {
        assertEquals(10, 1.sum10())
    }
}
