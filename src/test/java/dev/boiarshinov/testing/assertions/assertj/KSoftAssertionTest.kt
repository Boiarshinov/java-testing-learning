package dev.boiarshinov.testing.assertions.assertj

import org.assertj.core.api.SoftAssertions
import org.junit.jupiter.api.Test

class KSoftAssertionTest {

    @Test
    fun assertSoftly() {
        SoftAssertions.assertSoftly {
            it.assertThat("Joshua Bloch").isEqualTo("Bruce Eckel")
            it.assertThat("Java 7").isGreaterThan("Java 8")
        }
    }

    @Test
    fun `assertSoftly with custom util function`() {
        assertSoftly {
            assertThat("Joshua Bloch").isEqualTo("Bruce Eckel")
            assertThat("Java 7").isGreaterThan("Java 8")
        }
    }
}

fun assertSoftly(body: SoftAssertions.() -> Unit) =
    SoftAssertions.assertSoftly(body)
