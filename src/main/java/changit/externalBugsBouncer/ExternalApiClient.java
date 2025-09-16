package changit.externalBugsBouncer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

public class ExternalApiClient {

    private final String url;

    public ExternalApiClient(String url) {
        this.url = url;
    }

    List<PostRecord> invokePostRecords() throws IOException {
        URL url = new URL(this.url);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        String responseString = new BufferedReader(new InputStreamReader(connection.getInputStream()))
                .lines()
                .collect(Collectors.joining());


        ObjectMapper mapper = new ObjectMapper();
        List<PostRecord> posts = mapper.readValue(responseString, new TypeReference<List<PostRecord>>() {
        });
        connection.disconnect();
        return posts;
    }

    PostRecord createPost(PostRecord post) throws IOException {
        URL url = new URL(this.url);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        ObjectMapper mapper = new ObjectMapper();
        String jsonInputString = mapper.writeValueAsString(post);

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        String responseString = new BufferedReader(new InputStreamReader(connection.getInputStream()))
                .lines()
                .collect(Collectors.joining());

        PostRecord createdPost = mapper.readValue(responseString, PostRecord.class);
        connection.disconnect();
        return createdPost;
    }
}