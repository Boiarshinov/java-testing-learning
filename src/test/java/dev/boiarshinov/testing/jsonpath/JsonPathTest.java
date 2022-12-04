package dev.boiarshinov.testing.jsonpath;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
}
