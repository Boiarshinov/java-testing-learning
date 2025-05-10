package dev.boiarshinov.testing.assertions.json;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.Customization;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.RegularExpressionValueMatcher;
import org.skyscreamer.jsonassert.comparator.CustomComparator;

public class JsonAssertTest {

    @Test
    void assertStrictly() throws JSONException {
        //language=JSON
        String expected = """
            {"field": "value"}
            """;
        //language=JSON
        String actual = """
            {"field": "value"}
            """;

        JSONAssert.assertEquals(expected, actual, true);
    }

    @Test
    void assertWithPlaceholder() throws JSONException {
        //language=JSON
        String expected = """
            {"field": "[A-Za-z]{8}"}
            """;
        //language=JSON
        String actual = """
            {"field": "LadyGaga"}
            """;

        CustomComparator comparator = new CustomComparator(JSONCompareMode.STRICT,
            Customization.customization("***", new RegularExpressionValueMatcher<>()));

        JSONAssert.assertEquals(expected, actual, comparator);
    }
}
