package dev.boiarshinov.testing.mock.server;

import org.junit.jupiter.api.*;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.matchers.Times;
import org.mockserver.model.Header;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MockServerTest {

    private static ClientAndServer mockServer;

    private HttpClient httpClient;

    @BeforeAll
    static void setUp() {
        mockServer = ClientAndServer.startClientAndServer();
    }

    @AfterAll
    static void tearDown() {
        mockServer.stop();
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
                    .withMethod("GET"))
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
    void return404AtNoExpectations() {
        var statusAndBody = httpClient.sendGetRequest("/path/to/resource");

        assertEquals(404, statusAndBody.status());
        assertEquals("", statusAndBody.body());
    }

    @Test
    void sendRequestTwiceOnOneMock() {
        mockServer.when(
                HttpRequest.request()
                    .withMethod("GET"))
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
                    .withMethod("GET"),
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
}
