import org.assertj.core.api.KotlinAssertions.assertThat
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue
import kotlin.test.fail

fun Int.sum10() : Int = this + 10

class MainTest {
    @Test
    fun `please give me good name`() {
            assertTrue { true == true }
        assertThat("foo").isEqualToIgnoringCase("bar")
    }

    @Test
    fun `fun adds 10 to a given Int`(){
        assertThat( 1.sum10()).isEqualTo(11)
    }
}
