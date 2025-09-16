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

    @Test
    public void testCreatePostEndpointCreatesPost() throws IOException {
        // Create a new post
        PostRecord newPost = new PostRecord();
        newPost.userId = 1;
        newPost.title = "Test Post Title";
        newPost.body = "This is a test post body content";

        // Create the post via API
        PostRecord createdPost = externalApiClient.createPost(newPost);

        // Verify the post was created successfully
        assertThat(createdPost).isNotNull();
        assertThat(createdPost.userId).isEqualTo(newPost.userId);
        assertThat(createdPost.title).isEqualTo(newPost.title);
        assertThat(createdPost.body).isEqualTo(newPost.body);
        assertThat(createdPost.id).isGreaterThan(0); // API should assign an ID
    }

}
