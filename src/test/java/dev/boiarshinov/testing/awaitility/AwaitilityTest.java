package dev.boiarshinov.testing.awaitility;

import org.awaitility.core.ConditionTimeoutException;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AwaitilityTest {

    private static final Duration ONE_SECOND = Duration.ofSeconds(1);
    private static final Duration TWO_SECONDS = Duration.ofSeconds(2);

    private final AsyncMap<String, String> asyncMap = new AsyncMap<>(TWO_SECONDS);

    @Test
    void noAwait() {
        await().until(() -> true);
    }

    @Test
    void testAwait() {
        asyncMap.put("key", "value");
        await().until(() -> asyncMap.get("key") != null);
    }

    @Test
    void failWithAwaitTimeout() {
        assertThrows(ConditionTimeoutException.class, () -> {
            asyncMap.put("key", "value");
            await().atMost(ONE_SECOND)
                .until(() -> asyncMap.get("key") != null);
        });
    }

    @Test
    void untilAndReturn() {
        asyncMap.put("key", "value");
        String value = await().until(() -> asyncMap.get("key"), v -> v != null);
        assertEquals("value", value);
    }

    @Test
    void atLeastFail() {
        assertThrows(ConditionTimeoutException.class, () ->
            await().atLeast(1, TimeUnit.SECONDS).until(() -> true)
        );
    }
}
