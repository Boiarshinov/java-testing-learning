package dev.boiarshinov.testing.assertions.assertj;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.opentest4j.MultipleFailuresError;

import static org.assertj.core.api.Assertions.assertThat;

public class SoftAssertionTest {

    @Test
    void assertAll() {
        explorationTest(() ->
            Assertions.assertAll(
                () -> assertThat("Joshua Bloch").isEqualTo("Bruce Eckel"),
                () -> assertThat("Java 7").isGreaterThan("Java 8")
            )
        );
    }

    @Test
    void softAssertionsObject() {
        explorationTest(() -> {
            var softly = new SoftAssertions();

            softly.assertThat("Joshua Bloch").isEqualTo("Bruce Eckel");
            softly.assertThat("Java 7").isGreaterThan("Java 8");

            softly.assertAll();
        });

    }

    @Test
    void assertSoftly() {
        explorationTest(() ->
            SoftAssertions.assertSoftly(softly -> {
                softly.assertThat("Joshua Bloch").isEqualTo("Bruce Eckel");
                softly.assertThat("Java 7").isGreaterThan("Java 8");
            })
        );
    }

    void explorationTest(Runnable runnable) {
        try {
            runnable.run();
        } catch (MultipleFailuresError e) {
            var failures = e.getFailures();
            assertThat(failures).hasSize(2);
        }
    }
}
