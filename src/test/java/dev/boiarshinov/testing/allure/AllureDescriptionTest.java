package dev.boiarshinov.testing.allure;

import io.qameta.allure.Description;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AllureDescriptionTest {

    @Test
    @DisplayName("Имя тест-кейса для allure-report с помощью @DisplayName")
    void nameByDisplayName() { }

    @Test
    @Description("Имя тест-кейса для allure-report с помощью @Description")
    void nameByDescription() { }
}
