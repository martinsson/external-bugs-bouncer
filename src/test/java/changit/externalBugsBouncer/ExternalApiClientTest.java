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
        PostRecord firstPost = posts.get(0);
        assertThat(firstPost.userId).isGreaterThan(0);
        assertThat(firstPost.id).isGreaterThan(0);
        assertThat(firstPost.title).isNotEmpty();
        assertThat(firstPost.body).isNotEmpty();
    }

}
