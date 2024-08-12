package dev.boiarshinov.testing.assertions.assertj

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions
import org.junit.jupiter.api.Test
import org.opentest4j.MultipleFailuresError

class KSoftAssertionTest {

    @Test
    fun assertSoftly() {
        explorationTest {
            SoftAssertions.assertSoftly {
                it.assertThat("Joshua Bloch").isEqualTo("Bruce Eckel")
                it.assertThat("Java 7").isGreaterThan("Java 8")
            }
        }
    }

    @Test
    fun `assertSoftly with custom util function`() {
        explorationTest {
            assertSoftly {
                assertThat("Joshua Bloch").isEqualTo("Bruce Eckel")
                assertThat("Java 7").isGreaterThan("Java 8")
            }
        }
    }

    private fun explorationTest(block: () -> Unit) {
        try {
            block()
        } catch (e: MultipleFailuresError) {
            assertThat(e.failures).hasSize(2)
        }
    }
}

fun assertSoftly(body: SoftAssertions.() -> Unit) =
    SoftAssertions.assertSoftly(body)
