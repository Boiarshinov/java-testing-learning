package dev.boiarshinov.testing.mock.server;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class HttpClient {

    private final String baseUrl;
    private final int port;

    public HttpClient(String baseUrl, int port) {
        this.baseUrl = baseUrl;
        this.port = port;
    }

    public StatusAndBody sendGetRequest(String path) {
        OkHttpClient httpClient = new OkHttpClient();

        Request request = new Request.Builder()
            .url(baseUrl + ":" + port + path)
            .get()
            .build();

        try (Response response = httpClient.newCall(request).execute()) {
            return new StatusAndBody(
                response.code(),
                response.body().string()
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public record StatusAndBody(
        int status,
        String body
    ) {}
}
