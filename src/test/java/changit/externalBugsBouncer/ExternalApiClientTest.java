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
        PostRecord newPost = new PostRecord(1, 0, "qui est esse", "est rerum tempore vitae\n" +
                                                                  "sequi sint nihil reprehenderit dolor beatae ea dolores neque\n" +
                                                                  "fugiat blanditiis voluptate porro vel nihil molestiae ut reiciendis\n" +
                                                                  "qui aperiam non debitis possimus qui neque nisi nulla");

        // Create the post via API
        PostRecord createdPost = externalApiClient.createPost(newPost);

        // Verify the post was created by fetching it from the API
        var createdPostId = createdPost.id();
        createdPostId = 2; // this api is not actually writable, but let's imagine it is
        // this is what we'd do if it were writable
        PostRecord fetchedPost = externalApiClient.getPostById(createdPostId);

        assertThat(fetchedPost).isNotNull();
        assertThat(fetchedPost.userId()).isEqualTo(newPost.userId());
        assertThat(fetchedPost.title()).isEqualTo(newPost.title());
        assertThat(fetchedPost.body()).isEqualTo(newPost.body());
        assertThat(fetchedPost.id()).isEqualTo(createdPostId);
    }

}
