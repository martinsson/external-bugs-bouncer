package changit.externalBugsBouncer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

public class ExternalApiClient {

    private final String url;
    private final ObjectMapper mapper = new ObjectMapper();

    public ExternalApiClient(String url) {
        this.url = url;
    }

    public List<PostRecord> getPosts() throws IOException {
        String responseString = executeGetRequest(this.url);
        return mapper.readValue(responseString, new TypeReference<>() {});
    }

    public PostRecord getPostById(int id) throws IOException {
        String responseString = executeGetRequest(this.url + "/" + id);
        return mapper.readValue(responseString, PostRecord.class);
    }

    public PostRecord createPost(PostRecord post) throws IOException {
        String jsonInputString = mapper.writeValueAsString(post);
        String responseString = executePostRequest(this.url, jsonInputString);
        return mapper.readValue(responseString, PostRecord.class);
    }

    private String executeGetRequest(String urlString) throws IOException {
        HttpURLConnection connection = createConnection(urlString);
        connection.setRequestMethod("GET");
        return getResponseString(connection);
    }

    private String executePostRequest(String urlString, String jsonBody) throws IOException {
        HttpURLConnection connection = createConnection(urlString);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        return getResponseString(connection);
    }

    private HttpURLConnection createConnection(String urlString) throws IOException {
        URL url = new URL(urlString);
        return (HttpURLConnection) url.openConnection();
    }

    private String getResponseString(HttpURLConnection connection) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String response = reader.lines().collect(Collectors.joining());
            connection.disconnect();
            return response;
        }
    }
}