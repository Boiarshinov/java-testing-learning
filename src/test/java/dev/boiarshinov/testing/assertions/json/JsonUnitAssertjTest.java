package dev.boiarshinov.testing.assertions.json;

import net.javacrumbs.jsonunit.assertj.JsonAssertions;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class JsonUnitAssertjTest {

    @Test
    void arraySize() {
        assertThatThrownBy(() -> {
            //language=JSON
            JsonAssertions.assertThatJson("""
            {"kek": [
              {"name": "1"},
              {"name": "2"},
              {"name": "3"}
            ]}
            """)
                .isEqualTo("""
            {"kek": [
              {"name": "2"},
              {"name": "3"}
            ]}
            """);
        })
            .isInstanceOf(AssertionError.class)
            .hasMessageContaining("has different length, expected: <2> but was: <3>");
    }

    @Test
    void failAtUnknownProperty() {
        assertThatThrownBy(() -> {
            //language=JSON
            JsonAssertions.assertThatJson("""
            {
              "field": "value",
              "unexpected": "value"
            }
            """)
                .isEqualTo("""
            {"field": "value"}
            """);
        }).isInstanceOf(AssertionFailedError.class)
            .hasMessageContaining("Different keys found in node");
    }

    @Test
    void ignorePlaceholder() {
        //language=JSON
        JsonAssertions.assertThatJson(""" 
            {"field": "real_value"}
            """)
            .withIgnorePlaceholder("*")
            .isEqualTo("""
            {"field": "*"}
            """);

        //language=JSON
        JsonAssertions.assertThatJson(""" 
            {"field": "Lady Gaga"}
            """)
            .withIgnorePlaceholder(":abracadabra:")
            .isEqualTo("""
            {"field": ":abracadabra:"}
            """);
    }

    @Test
    void matchRegex() {
        //language=JSON
        JsonAssertions.assertThatJson(""" 
            {"field": "real_value"}
            """)
            .isEqualTo("""
            {"field": "${json-unit.regex}.*"}
            """);
    }

    @Test
    void anyValueWithType() {
        //language=JSON
        JsonAssertions.assertThatJson(""" 
            {
              "strField": "string_value",
              "intField": 5,
              "booleanField": true
            }
            """)
            .isEqualTo("""
            {
              "strField": "${json-unit.any-string}",
              "intField": "${json-unit.any-number}",
              "booleanField": "${json-unit.any-boolean}"
            }
            """);
    }
}
