package dev.boiarshinov.testing.jsonpath;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class JsonPathTest {

    @Test
    void jsonPath() {
        String json = """
            {
                "qrcIds": ["abc", "dfg"]
            }
            """;
        Object result = JsonPath.read(json, "$.qrcIds[0]");

        assertEquals("abc", result.toString());
    }

    @Test
    void findNonExistentValue() {
        String json = """
            {
                "field": null
            }
            """;
        assertThrows(PathNotFoundException.class,
            () -> JsonPath.read(json, "$.nonExistent"));
    }

    @Test
    void findNull() {
        String json = """
            {
                "field": null
            }
            """;
        Object result = JsonPath.read(json, "$.field");

        assertNull(result);
    }
}
