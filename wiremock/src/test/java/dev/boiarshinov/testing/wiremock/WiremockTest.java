package dev.boiarshinov.testing.wiremock;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.stubbing.Scenario;
import dev.boiarshinov.testing.http.client.HttpClient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WiremockTest {

    private HttpClient httpClient;
    private static WireMockServer wireMockServer;
    //based on jetty

    @BeforeAll
    static void prepare() {
        long startTime = measureTime(() -> {
                // По умолчанию стартует на порту 8080. Если порт занят - падает
                wireMockServer = new WireMockServer();
                wireMockServer.start();
            }
        );
        System.out.println("Started by: " + startTime + " millis");
    }

    @BeforeEach
    void setUp() {
        wireMockServer.resetMappings();
        httpClient = new HttpClient("http://localhost", wireMockServer.port());
    }

    @AfterAll
    static void tearDown() {
        long stopTime = measureTime(() ->
            wireMockServer.stop()
        );
        System.out.println("Stopped by: " + stopTime + " millis");
    }

    @Test
    void mockResponse() {
        wireMockServer.stubFor(
            WireMock.get("/path/to/resource")
                .willReturn(WireMock.ok("responseBody")
                    .withHeader("Content-Type", "application/json"))
        );

        var statusAndBody = httpClient.sendGetRequest("/path/to/resource");

        assertEquals(200, statusAndBody.status());
        assertEquals("responseBody", statusAndBody.body());

        wireMockServer.verify(
            WireMock.getRequestedFor(WireMock.urlEqualTo("/path/to/resource"))
        );
    }

    @Test
    void verifyRequestBody() {
        wireMockServer.stubFor(
            WireMock.post("/path/to/resource")
                .willReturn(WireMock.ok("responseBody")
                    .withHeader("Content-Type", "application/json"))
        );

        var statusAndBody = httpClient.sendPostRequest("/path/to/resource", """
            {
                "need_to_check": "value",
                "no_need_to_check": "value"
            }
            """);

        assertEquals(200, statusAndBody.status());
        assertEquals("responseBody", statusAndBody.body());

        wireMockServer.verify(
            WireMock.postRequestedFor(WireMock.urlEqualTo("/path/to/resource"))
                .withRequestBody(WireMock.matchingJsonPath("$.need_to_check", WireMock.equalTo("value")))
        );
        wireMockServer.verify(
            WireMock.postRequestedFor(WireMock.urlEqualTo("/path/to/resource"))
                .withRequestBody(WireMock.equalToJson("""
                {"need_to_check": "value", "no_need_to_check": "value"}
                """))
        );
    }

    @Test
    void matchRequestWithPathParam() {
        wireMockServer.stubFor(
            WireMock.get(WireMock.urlPathTemplate("/resource/{id}"))
                .willReturn(WireMock.ok("responseBody")
                    .withHeader("Content-Type", "application/json"))
        );

        var statusAndBody = httpClient.sendGetRequest("/resource/123456");

        assertEquals(200, statusAndBody.status());
        assertEquals("responseBody", statusAndBody.body());

        wireMockServer.verify(
            WireMock.getRequestedFor(WireMock.urlPathTemplate("/resource/{id}"))
                .withPathParam("id", WireMock.equalTo("123456"))
        );
    }

    @Test
    void verifyQueryParam() {
        wireMockServer.stubFor(
            WireMock.get(WireMock.urlPathEqualTo("/path/to/resource"))
                .willReturn(WireMock.ok("responseBody")
                    .withHeader("Content-Type", "application/json"))
        );

        var statusAndBody = httpClient.sendGetRequest("/path/to/resource", "filter", "value");

        assertEquals(200, statusAndBody.status());
        assertEquals("responseBody", statusAndBody.body());

        wireMockServer.verify(
            WireMock.getRequestedFor(WireMock.urlPathEqualTo("/path/to/resource"))
                .withoutQueryParam("nonexist")
                .withQueryParam("filter", WireMock.equalTo("value"))
        );
        //В случае провала валидации сообщение об ошибке норм
    }

    @Test
    void return404AtNoStubs() {
        var statusAndBody = httpClient.sendGetRequest("/path/to/resource");

        assertEquals(404, statusAndBody.status());
        assertEquals(
            "No response could be served as there are no stub mappings in this WireMock instance.",
            statusAndBody.body()
        );
    }

    @Test
    void sendRequestTwiceOnOneMock() {
        wireMockServer.stubFor(
            WireMock.get("/path/to/resource").willReturn(WireMock.ok("responseBody"))
        );

        var statusAndBody1 = httpClient.sendGetRequest("/path/to/resource");
        var statusAndBody2 = httpClient.sendGetRequest("/path/to/resource");

        assertEquals(200, statusAndBody1.status());
        assertEquals("responseBody", statusAndBody1.body());

        assertEquals(200, statusAndBody2.status());
        assertEquals("responseBody", statusAndBody2.body());
    }

    //намного сложнее, чем в mock-server
    @Test
    void expectOnlyOneRequest() {
        wireMockServer.stubFor(
            WireMock.get("/path/to/resource")
                .inScenario("once")
                .whenScenarioStateIs(Scenario.STARTED)
                .willSetStateTo("next")
                .willReturn(WireMock.ok("responseBody"))
        );

        wireMockServer.stubFor(
            WireMock.get("/path/to/resource")
                .inScenario("once")
                .whenScenarioStateIs("next")
                .willReturn(WireMock.notFound())
        );

        var statusAndBody1 = httpClient.sendGetRequest("/path/to/resource");
        var statusAndBody2 = httpClient.sendGetRequest("/path/to/resource");

        assertEquals(200, statusAndBody1.status());
        assertEquals("responseBody", statusAndBody1.body());
        assertEquals(404, statusAndBody2.status());
        assertEquals("", statusAndBody2.body());
    }

    @Test
    void checkClientRetries() {
        wireMockServer.stubFor(
            WireMock.get("/path/to/resource")
                .inScenario("retry")
                .whenScenarioStateIs(Scenario.STARTED)
                .willSetStateTo("repaired")
                .willReturn(WireMock.serviceUnavailable())
        );

        wireMockServer.stubFor(
            WireMock.get("/path/to/resource")
                .inScenario("retry")
                .whenScenarioStateIs("repaired")
                .willReturn(WireMock.ok("responseBody"))
        );

        //Как будто в клиенте написаны ретраи при негативных ответах
        var statusAndBody1 = httpClient.sendGetRequest("/path/to/resource");
        var statusAndBody2 = httpClient.sendGetRequest("/path/to/resource");

        assertEquals(503, statusAndBody1.status());
        assertEquals("", statusAndBody1.body());
        assertEquals(200, statusAndBody2.status());
        assertEquals("responseBody", statusAndBody2.body());
    }

    @Test
    void delayedResponse() {
        int delay = 1000;
        wireMockServer.stubFor(
            WireMock.get("/path/to/resource")
                .willReturn(
                    WireMock.ok("responseBody")
                        .withFixedDelay(delay))
        );

        long requestTimeMillis = measureTime(() -> {
            var statusAndBody = httpClient.sendGetRequest("/path/to/resource");

            assertEquals(200, statusAndBody.status());
            assertEquals("responseBody", statusAndBody.body());
        });

        assertTrue(requestTimeMillis >= delay);
    }

    private static long measureTime(Runnable runnable) {
        long startMillis = System.currentTimeMillis();
        runnable.run();
        long finishMillis = System.currentTimeMillis();
        return finishMillis - startMillis;
    }
}
