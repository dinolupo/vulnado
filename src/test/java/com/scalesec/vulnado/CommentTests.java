package com.scalesec.vulnado;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.mockito.Mockito;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VulnadoApplicationTests {

    @Test
    public void contextLoads() {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Test
    public void Comment_Create_ShouldReturnValidComment() {
        // Arrange
        String username = "testUser";
        String body = "This is a test comment";
        Timestamp timestamp = new Timestamp(new Date().getTime());
        Comment mockComment = new Comment(UUID.randomUUID().toString(), username, body, timestamp);
        Comment spyComment = Mockito.spy(mockComment);
        doReturn(true).when(spyComment).commit();

        // Act
        Comment createdComment = Comment.create(username, body);

        // Assert
        assertNotNull("Created comment should not be null", createdComment);
        assertEquals("Username should match", username, createdComment.getUsername());
        assertEquals("Body should match", body, createdComment.getBody());
    }

    @Test(expected = BadRequest.class)
    public void Comment_Create_ShouldThrowBadRequestWhenCommitFails() {
        // Arrange
        String username = "testUser";
        String body = "This is a test comment";
        Timestamp timestamp = new Timestamp(new Date().getTime());
        Comment mockComment = new Comment(UUID.randomUUID().toString(), username, body, timestamp);
        Comment spyComment = Mockito.spy(mockComment);
        doReturn(false).when(spyComment).commit();

        // Act
        Comment.create(username, body);
    }

    @Test(expected = ServerError.class)
    public void Comment_Create_ShouldThrowServerErrorOnException() {
        // Arrange
        String username = "testUser";
        String body = "This is a test comment";
        Timestamp timestamp = new Timestamp(new Date().getTime());
        Comment mockComment = new Comment(UUID.randomUUID().toString(), username, body, timestamp);
        Comment spyComment = Mockito.spy(mockComment);
        doThrow(new SQLException("Database error")).when(spyComment).commit();

        // Act
        Comment.create(username, body);
    }

    @Test
    public void Comment_FetchAll_ShouldReturnListOfComments() throws Exception {
        // Arrange
        Connection mockConnection = mock(Connection.class);
        Statement mockStatement = mock(Statement.class);
        ResultSet mockResultSet = mock(ResultSet.class);
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getString("id")).thenReturn("1");
        when(mockResultSet.getString("username")).thenReturn("testUser");
        when(mockResultSet.getString("body")).thenReturn("This is a test comment");
        when(mockResultSet.getTimestamp("created_on")).thenReturn(new Timestamp(new Date().getTime()));
        Postgres.setMockConnection(mockConnection);

        // Act
        List<Comment> comments = Comment.fetchAll();

        // Assert
        assertNotNull("Comments list should not be null", comments);
        assertEquals("Comments list should contain one comment", 1, comments.size());
        assertEquals("Comment username should match", "testUser", comments.get(0).getUsername());
    }

    @Test
    public void Comment_Delete_ShouldReturnTrueWhenSuccessful() throws Exception {
        // Arrange
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);
        Postgres.setMockConnection(mockConnection);

        // Act
        Boolean result = Comment.delete("1");

        // Assert
        assertTrue("Delete should return true when successful", result);
    }

    @Test
    public void Comment_Delete_ShouldReturnFalseWhenUnsuccessful() throws Exception {
        // Arrange
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(0);
        Postgres.setMockConnection(mockConnection);

        // Act
        Boolean result = Comment.delete("1");

        // Assert
        assertFalse("Delete should return false when unsuccessful", result);
    }
}

