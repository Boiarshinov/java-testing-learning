package dev.boiarshinov.testing.awaitility;

import org.awaitility.Awaitility;
import org.awaitility.Durations;
import org.awaitility.core.ConditionTimeoutException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;

public class AwaitilityTest {

    private static final Duration ONE_SECOND = Duration.ofSeconds(1);
    private static final Duration TWO_SECONDS = Duration.ofSeconds(2);
    private static final Duration THREE_SECONDS = Duration.ofSeconds(3);

    /** Волшебная мапа, в которой значения появляются спустя 2 секунды после записи */
    private final AsyncMap<String, String> asyncMap = new AsyncMap<>(TWO_SECONDS);

    @DisplayName("Точка входа - await(), точка выхода - until()")
    @Test
    void noAwait() {
        await().until(() -> true);
    }

    @DisplayName("Дожидаемся пока объект не примет нужное значение")
    @Test
    void testAwait() {
        asyncMap.put("key", "value");
        await().until(() -> asyncMap.get("key") != null);
    }

    @DisplayName("Если не уложились в таймаут, то падаем")
    @Test
    void failWithAwaitTimeout() {
        assertThrows(ConditionTimeoutException.class, () -> {
            asyncMap.put("key", "value");
            await().atMost(ONE_SECOND)
                .timeout(ONE_SECOND) // синоним к atMost()
                .until(() -> asyncMap.get("key") != null);
        });
    }

    @DisplayName("После того, как дождались выполнения условия, можно вернуть значение")
    @Test
    void untilAndReturn() {
        asyncMap.put("key", "value");
        String value = await().until(() -> asyncMap.get("key"), v -> v != null);
        assertEquals("value", value);
    }

    @DisplayName("Внутри until() можно использовать ассерты")
    @Test
    void untilAsserted() {
        asyncMap.put("key", "value");
        await().untilAsserted(() -> assertThat(asyncMap.get("key")).isEqualTo("value"));
    }

    @DisplayName("Задержка перед началом поллинга")
    @Test
    void pollDelay() {
        await().pollDelay(TWO_SECONDS)
                .until(() -> true);
    }

    @DisplayName("Если полящийся метод падает с исключением в случае неудачи, это исключение можно игнорировать")
    @Test
    void ignoreException() {
        asyncMap.put("key", "value");
        await().ignoreException(IllegalArgumentException.class)
                .until(() -> asyncMap.getOrThrow("key") != null);
    }

    @DisplayName("Можно добавить описание чего ждем строкой в await()")
    @Test
    void failWithAlias() {
        Throwable exception = assertThrows(ConditionTimeoutException.class, () ->
                await("await alias").atMost(Duration.ofMillis(101)).until(() -> false)
        );
        assertTrue(exception.getMessage().contains("await alias"));
    }

    @DisplayName("Можно переопределять дефолтные настройки. Например, для определенного тестового класса")
    @Test
    void defaultProperties() {
//        Awaitility.setDefaultPollDelay(null);
        Awaitility.setDefaultPollInterval(Durations.ONE_HUNDRED_MILLISECONDS);
        Awaitility.setDefaultTimeout(Durations.TEN_SECONDS);
        Awaitility.catchUncaughtExceptionsByDefault();
        Awaitility.setDefaultConditionEvaluationListener(null);
        Awaitility.ignoreExceptionsByDefault();
        Awaitility.setDefaultFailFastCondition(null);
        Awaitility.reset();
    }

    @DisplayName("Можно переопределить дефолтные настройки для конкретного кейса")
    @Test
    void customProperties() {
        await().pollInterval(Duration.ofMillis(250));
        //todo
    }

    @DisplayName("В течение заданного времени предикат не должен выполняться")
    @Test
    void atLeastFail() {
        assertThrows(ConditionTimeoutException.class, () ->
            await().atLeast(1, TimeUnit.SECONDS).until(() -> true)
        );
    }

    @DisplayName("between() вместо сочетания atLeast() и atMost()")
    @Test
    void between() {
        asyncMap.put("key", "value");
        await().between(ONE_SECOND, THREE_SECONDS)
                .until(() -> asyncMap.get("key") != null);
    }

    @DisplayName("В течение заданного времени предикат должен выполняться")
    @Test
    void checkNothingHappen() {
        await().during(THREE_SECONDS) // Значение должно быть меньше atMost не менее чем на 1 pollInterval
                .failFast(() -> asyncMap.get("key") != null)
                .until(() -> asyncMap.get("key") == null);
    }
}
