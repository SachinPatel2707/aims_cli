package utility;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpCalls {
    public static HttpResponse<String> postCall (Object obj, String uri) throws URISyntaxException, IOException, InterruptedException {
        Gson gson = new Gson();
        String postBody = gson.toJson(obj);

        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(new URI(uri))
                .setHeader("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(postBody))
                .build();

        HttpClient http = HttpClient.newHttpClient();
        HttpResponse<String> postResponse = http.send(postRequest, HttpResponse.BodyHandlers.ofString());
        return postResponse;
    }

    public static HttpResponse<String> getCall (String uri) throws URISyntaxException, IOException, InterruptedException {
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(new URI(uri))
                .GET()
                .build();

        HttpClient http = HttpClient.newHttpClient();
        HttpResponse<String> getResponse = http.send(getRequest, HttpResponse.BodyHandlers.ofString());
        return getResponse;
    }
}
