package com.scalesec.vulnado;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CommentsControllerTests {

    @Value("${app.secret}")
    private String secret;

    @Test
    public void comments_ShouldReturnListOfComments_WhenAuthenticated() {
        // Arrange
        String token = "valid-token";
        List<Comment> mockComments = new ArrayList<>();
        mockComments.add(new Comment("user1", "This is a comment"));
        mockComments.add(new Comment("user2", "Another comment"));

        User mockUser = mock(User.class);
        Comment mockComment = mock(Comment.class);
        Mockito.doNothing().when(mockUser).assertAuth(secret, token);
        Mockito.when(mockComment.fetch_all()).thenReturn(mockComments);

        CommentsController controller = new CommentsController();
        controller.secret = secret;

        // Act
        List<Comment> result = controller.comments(token);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertEquals(2, result.size(), "Result size should match the number of mock comments");
    }

    @Test
    public void comments_ShouldThrowException_WhenNotAuthenticated() {
        // Arrange
        String token = "invalid-token";

        User mockUser = mock(User.class);
        Mockito.doThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED)).when(mockUser).assertAuth(secret, token);

        CommentsController controller = new CommentsController();
        controller.secret = secret;

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> controller.comments(token));
        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatus(), "Exception status should be UNAUTHORIZED");
    }

    @Test
    public void createComment_ShouldReturnComment_WhenValidInput() {
        // Arrange
        String token = "valid-token";
        CommentRequest input = new CommentRequest();
        input.username = "user1";
        input.body = "This is a new comment";

        Comment mockComment = mock(Comment.class);
        Mockito.when(mockComment.create(input.username, input.body)).thenReturn(new Comment(input.username, input.body));

        CommentsController controller = new CommentsController();
        controller.secret = secret;

        // Act
        Comment result = controller.createComment(token, input);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertEquals(input.username, result.getUsername(), "Username should match the input");
        assertEquals(input.body, result.getBody(), "Body should match the input");
    }

    @Test
    public void deleteComment_ShouldReturnTrue_WhenCommentDeleted() {
        // Arrange
        String token = "valid-token";
        String commentId = "123";

        Comment mockComment = mock(Comment.class);
        Mockito.when(mockComment.delete(commentId)).thenReturn(true);

        CommentsController controller = new CommentsController();
        controller.secret = secret;

        // Act
        Boolean result = controller.deleteComment(token, commentId);

        // Assert
        assertTrue(result, "Result should be true when comment is deleted");
    }

    @Test
    public void deleteComment_ShouldReturnFalse_WhenCommentNotDeleted() {
        // Arrange
        String token = "valid-token";
        String commentId = "123";

        Comment mockComment = mock(Comment.class);
        Mockito.when(mockComment.delete(commentId)).thenReturn(false);

        CommentsController controller = new CommentsController();
        controller.secret = secret;

        // Act
        Boolean result = controller.deleteComment(token, commentId);

        // Assert
        assertFalse(result, "Result should be false when comment is not deleted");
    }
}

