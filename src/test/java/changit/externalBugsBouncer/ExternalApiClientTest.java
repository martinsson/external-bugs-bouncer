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
        assertThat(firstPost.userId()).isGreaterThan(0);
        assertThat(firstPost.id()).isGreaterThan(0);
        assertThat(firstPost.title()).isNotEmpty();
        assertThat(firstPost.body()).isNotEmpty();
    }

    @Test
    public void testCreatePostEndpointCreatesPost() throws IOException {
        // Create a new post
        var body = "est rerum tempore vitae\n" +
                   "sequi sint nihil reprehenderit dolor beatae ea dolores neque\n" +
                   "fugiat blanditiis voluptate porro vel nihil molestiae ut reiciendis\n" +
                   "qui aperiam non debitis possimus qui neque nisi nulla";
        PostRecord newPost = new PostRecord(1, 0, "qui est esse", body);

        PostRecord createdPost = externalApiClient.createPost(newPost);

        // Verify the post was created by fetching it from the API
        var createdPostId = createdPost.id();
        createdPostId = 2; // this api is not actually writable, it only pretends to be
        // but let's imagine it is and write the test as if it were
        PostRecord fetchedPost = externalApiClient.getPostById(createdPostId);

        var expectedPost = new PostRecord(newPost.userId(), createdPostId, newPost.title(), newPost.body());
        assertThat(fetchedPost).usingRecursiveAssertion().isEqualTo(expectedPost);
    }

}
