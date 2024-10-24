package dev.boiarshinov.testing.mock.server;

import org.junit.jupiter.api.*;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.matchers.Times;
import org.mockserver.model.Delay;
import org.mockserver.model.Header;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.mockserver.model.JsonBody;
import org.mockserver.model.Parameter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MockServerTest {

    private HttpClient httpClient;
    private static ClientAndServer mockServer;
    //based on netty

    @BeforeAll
    static void setUp() {
        long startTime = measureTime(() ->
            mockServer = ClientAndServer.startClientAndServer()
        );
        System.out.println("Started by: " + startTime + " millis");
    }

    @AfterAll
    static void tearDown() {
        long stopTime = measureTime(() ->
            mockServer.stop()
        );
        System.out.println("Stopped by: " + stopTime + " millis");
    }

    @BeforeEach
    void prepare() {
        mockServer.reset();
        httpClient = new HttpClient("http://localhost", mockServer.getPort());
    }

    @Test
    void mockResponse() {
        mockServer.when(
                HttpRequest.request()
                    .withMethod("GET")
                    .withPath("/path/to/resource"))
            .respond(
                HttpResponse.response()
                    .withHeader(Header.header("Content-Type", "application/json"))
                    .withBody("responseBody")
            );

        var statusAndBody = httpClient.sendGetRequest("/path/to/resource");

        assertEquals(200, statusAndBody.status());
        assertEquals("responseBody", statusAndBody.body());
        mockServer.verify(HttpRequest.request()
            .withMethod("GET")
            .withPath("/path/to/resource"));
    }

    @Test
    void verifyRequestBody() {
        mockServer.when(
                HttpRequest.request()
                    .withMethod("POST")
                    .withPath("/path/to/resource")
            )
            .respond(
                HttpResponse.response()
                    .withHeader(Header.header("Content-Type", "application/json"))
                    .withBody("responseBody")
            );

        var statusAndBody = httpClient.sendPostRequest("/path/to/resource", """
            {
                "need_to_check": "value",
                "no_need_to_check": "value"
            }
            """);

        assertEquals(200, statusAndBody.status());
        assertEquals("responseBody", statusAndBody.body());

        //проассертить по jsonPath не получилось
        mockServer.verify(HttpRequest.request()
            .withMethod("POST")
            .withPath("/path/to/resource")
            .withBody(JsonBody.json("""
                {
                  "need_to_check": "value",
                  "no_need_to_check": "${json-unit.any-string}"
                }
                """))
        );
        mockServer.verify(HttpRequest.request()
            .withMethod("POST")
            .withPath("/path/to/resource")
            .withBody(JsonBody.json("""
                {
                  "need_to_check": "value"
                }
                """))
        );
    }

    @Test
    void matchRequestWithPathParam() {
        mockServer.when(
            HttpRequest.request()
                .withMethod("GET")
                .withPath("/resource/{id}")
                .withPathParameter(Parameter.param("id", ".+"))
            )
            .respond(
                HttpResponse.response()
                    .withHeader(Header.header("Content-Type", "application/json"))
                    .withBody("responseBody")
            );

        var statusAndBody = httpClient.sendGetRequest("/resource/123456");

        assertEquals(200, statusAndBody.status());
        assertEquals("responseBody", statusAndBody.body());

        mockServer.verify(HttpRequest.request()
            .withMethod("GET")
            .withPath("/resource/{id}")
            .withPathParameter("id", "123456")
        );
        //В случае провала валидации сообщение об ошибке не показывает ожидаемое значение
    }

    @Test
    void verifyQueryParam() {
        mockServer.when(
                HttpRequest.request()
                    .withMethod("GET")
                    .withPath("/path/to/resource")
            )
            .respond(
                HttpResponse.response()
                    .withHeader(Header.header("Content-Type", "application/json"))
                    .withBody("responseBody")
            );

        var statusAndBody = httpClient.sendGetRequest("/path/to/resource", "filter", "value");

        assertEquals(200, statusAndBody.status());
        assertEquals("responseBody", statusAndBody.body());

        mockServer.verify(HttpRequest.request()
            .withMethod("GET")
            .withPath("/path/to/resource")
            .withQueryStringParameter("filter", "value")
        );
        //В случае провала валидации сообщение об ошибке норм
    }

    @Test
    void return404AtNoExpectations() {
        var statusAndBody = httpClient.sendGetRequest("/path/to/resource");

        assertEquals(404, statusAndBody.status());
        assertEquals("", statusAndBody.body());
    }

    @Test
    void sendRequestTwiceOnOneMock() {
        mockServer.when(
                HttpRequest.request()
                    .withMethod("GET")
                    .withPath("/path/to/resource"))
            .respond(
                HttpResponse.response()
                    .withHeader(Header.header("Content-Type", "application/json"))
                    .withBody("responseBody")
            );

        var statusAndBody1 = httpClient.sendGetRequest("/path/to/resource");
        var statusAndBody2 = httpClient.sendGetRequest("/path/to/resource");

        assertEquals(200, statusAndBody1.status());
        assertEquals("responseBody", statusAndBody1.body());
        assertEquals(200, statusAndBody2.status());
        assertEquals("responseBody", statusAndBody2.body());
    }

    @Test
    void expectOnlyOneRequest() {
        mockServer.when(
                HttpRequest.request()
                    .withMethod("GET")
                    .withPath("/path/to/resource"),
                Times.exactly(1))
            .respond(
                HttpResponse.response()
                    .withHeader(Header.header("Content-Type", "application/json"))
                    .withBody("responseBody")
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
        mockServer.when(
                HttpRequest.request()
                    .withMethod("GET")
                    .withPath("/path/to/resource"),
                Times.exactly(1))
            .respond(
                HttpResponse.response()
                    .withStatusCode(503)
            );
        mockServer.when(
                HttpRequest.request()
                    .withMethod("GET")
                    .withPath("/path/to/resource")
            )
            .respond(
                HttpResponse.response()
                    .withHeader(Header.header("Content-Type", "application/json"))
                    .withBody("responseBody")
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
        mockServer.when(
                HttpRequest.request()
                    .withMethod("GET")
                    .withPath("/path/to/resource"),
                Times.exactly(1))
            .respond(
                HttpResponse.response()
                    .withHeader(Header.header("Content-Type", "application/json"))
                    .withBody("responseBody")
                    .withDelay(Delay.milliseconds(delay))
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
