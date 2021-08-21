package dev.boiarshinov.testing.assertions.assertj;

import org.junit.jupiter.api.Test;

import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AssertionMessagesTest {

    @Test
    void notFail() {
        assertThat(ChronoUnit.HOURS)
            .isGreaterThan(ChronoUnit.MILLIS)
            .isLessThan(ChronoUnit.DAYS);
    }

    @Test
    void failAtFirstAssertion() {
        assertThatThrownBy(
            () -> assertThat(ChronoUnit.HOURS)
                .as("first message").isGreaterThan(ChronoUnit.WEEKS)
                //second `as()` not overloads first one cause first assertion failed
                .as("second message").isLessThan(ChronoUnit.DAYS))

            .hasMessageContaining("first message")
            .hasMessageNotContaining("second message");
    }

    @Test
    void failAtSecondAssertion() {
        assertThatThrownBy(
            () -> assertThat(ChronoUnit.HOURS)
                .as("first message").isGreaterThan(ChronoUnit.MILLIS)
                //second `as()` overloads first one if first assertion was successful
                .as("second message").isLessThan(ChronoUnit.NANOS))

            .hasMessageContaining("second message")
            .hasMessageNotContaining("first message");
    }
}
