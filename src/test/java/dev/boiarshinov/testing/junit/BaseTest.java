package dev.boiarshinov.testing.junit;

import org.junit.jupiter.api.BeforeEach;

public abstract class BaseTest {

    @BeforeEach
    void setUp() {
        System.out.println("parent");
    }
}
