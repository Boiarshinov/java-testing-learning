package dev.boiarshinov.testing.allure;

import io.qameta.allure.Step;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AllureStepsTest {

    @Test
    @DisplayName("Использование шагов")
    void steps() {
        createOrder();
        checkOrderCreated();
        checkOrderInProcessing(OrderStatus.IN_PROCESSING);
    }

    @Step("Пользователь создает заказ")
    void createOrder() { }

    @Step("Заказ получен системой")
    void checkOrderCreated() { }

    @Step("Заказ находится в статусе {orderStatus}")
    void checkOrderInProcessing(OrderStatus orderStatus) { }

    private enum OrderStatus {
        CREATED, IN_PROCESSING, CANCELLED, FINISHED
    }
}
