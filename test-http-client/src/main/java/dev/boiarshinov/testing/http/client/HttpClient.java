package dev.boiarshinov.testing.http.client;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
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
        return sendGetRequest(path, null, null);
    }

    public StatusAndBody sendGetRequest(String path, String queryParam, String queryValue) {
        OkHttpClient httpClient = new OkHttpClient();

        String queryPart = queryParam != null
            ? "?" + queryParam + "=" + queryValue
            : "";

        Request request = new Request.Builder()
            .url(baseUrl + ":" + port + path + queryPart)
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

    public StatusAndBody sendPostRequest(String path, String body) {
        OkHttpClient httpClient = new OkHttpClient();

        Request request = new Request.Builder()
            .url(baseUrl + ":" + port + path)
            .post(RequestBody.create(body, MediaType.parse("application/json")))
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
