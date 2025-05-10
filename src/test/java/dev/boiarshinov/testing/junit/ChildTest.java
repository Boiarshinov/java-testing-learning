package dev.boiarshinov.testing.junit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ChildTest extends BaseTest {

    @BeforeEach
    void setUp() {
        System.out.println("child");
    }

    @Test
    void test() {
        System.out.println("test");
    }
}
