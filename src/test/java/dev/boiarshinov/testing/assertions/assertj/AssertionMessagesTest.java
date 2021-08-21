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
    void asNotOverload() {
        assertThatThrownBy(
            () -> assertThat(ChronoUnit.HOURS)
                .as("first message").isGreaterThan(ChronoUnit.WEEKS)
                //second `as()` not overloads first one cause first assertion failed
                .as("second message").isLessThan(ChronoUnit.DAYS))
            .hasMessageContaining("first message")
            .hasMessageNotContaining("second message");
    }

    @Test
    void asOverload() {
        assertThatThrownBy(
            () -> assertThat(ChronoUnit.HOURS)
                .as("first message").isGreaterThan(ChronoUnit.MILLIS)
                //second `as()` overloads first one if first assertion was successful
                .as("second message").isLessThan(ChronoUnit.NANOS))
            .hasMessageContaining("second message")
            .hasMessageNotContaining("first message");
    }

    @Test
    void asFallThrow() {
        assertThatThrownBy(
            () -> assertThat(ChronoUnit.HOURS)
                .as("first message").isGreaterThan(ChronoUnit.MILLIS)
                //first `as()` not overloaded by anything
                .isLessThan(ChronoUnit.NANOS))
            .hasMessageContaining("first message");
    }

    @Test
    void withFailMessage() {
        assertThatThrownBy(
            () -> assertThat(ChronoUnit.HOURS)
                .withFailMessage("first message").isGreaterThan(ChronoUnit.WEEKS))
            .hasMessage("first message");
    }

    @Test
    void withFailMessageOverloading() {
        assertThatThrownBy(
            () -> assertThat(ChronoUnit.HOURS)
                .withFailMessage("first message").isGreaterThan(ChronoUnit.MILLIS)
                .withFailMessage("second message").isLessThan(ChronoUnit.NANOS))
            .hasMessage("second message");
    }

    @Test
    void withFailMessageFallThrow() {
        assertThatThrownBy(
            () -> assertThat(ChronoUnit.HOURS)
                .withFailMessage("first message").isGreaterThan(ChronoUnit.MILLIS)
                .isLessThan(ChronoUnit.NANOS))
            .hasMessage("first message");
    }
}
