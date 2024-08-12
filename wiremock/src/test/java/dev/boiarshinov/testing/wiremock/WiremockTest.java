package dev.boiarshinov.testing.wiremock;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import dev.boiarshinov.testing.mock.server.HttpClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WiremockTest {

    private HttpClient httpClient;
    private WireMockServer wireMockServer;

    @BeforeEach
    void setUp() {
        // По умолчанию стартует на порту 8080. Если порт занят - падает
        wireMockServer = new WireMockServer();
        wireMockServer.start();
        httpClient = new HttpClient("http://localhost", wireMockServer.port());
    }

    @AfterEach
    void tearDown() {
        wireMockServer.stop();
    }

    @Test
    void test() {
        wireMockServer.stubFor(
            WireMock.get("/path/to/resource").willReturn(WireMock.ok("responseBody"))
        );

        var statusAndBody = httpClient.sendGetRequest("/path/to/resource");

        assertEquals(200, statusAndBody.status());
        assertEquals("responseBody", statusAndBody.body());

        wireMockServer.verify(
            WireMock.getRequestedFor(WireMock.urlEqualTo("/path/to/resource"))
        );
    }
}
