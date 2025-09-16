package changit.externalBugsBouncer;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ExternalApiClientTest {
    private final ExternalApiClient externalApiClient = new ExternalApiClient("https://jsonplaceholder.typicode.com/posts");

    @Test
    public void testGetPostsEndpointReturns200() throws IOException {
        List<PostRecord> posts = externalApiClient.invokePostRecords();

        assertThat(posts).isNotEmpty();
        assertThat(posts.get(0).userId).isGreaterThan(0);
        assertThat(posts.get(0).id).isGreaterThan(0);
        assertThat(posts.get(0).title).isNotEmpty();
        assertThat(posts.get(0).body).isNotEmpty();
    }

}
