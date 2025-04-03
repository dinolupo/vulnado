package com.scalesec.vulnado;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.mockito.Mockito;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.UUID;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VulnadoApplicationTests {

    @Test
    public void contextLoads() {
        // Test to ensure the application context loads successfully
        assertTrue("Application context should load successfully", true);
    }

    @Test
    public void Postgres_Connection_ShouldReturnValidConnection() {
        // Mock environment variables
        System.setProperty("PGHOST", "localhost");
        System.setProperty("PGDATABASE", "testdb");
        System.setProperty("PGUSER", "testuser");
        System.setProperty("PGPASSWORD", "testpassword");

        Connection connection = Postgres.connection();
        assertNotNull("Connection should not be null", connection);
    }

    @Test
    public void Postgres_Setup_ShouldCreateTablesAndInsertData() {
        // Mock connection and statement
        Connection mockConnection = mock(Connection.class);
        Statement mockStatement = mock(Statement.class);

        try {
            when(mockConnection.createStatement()).thenReturn(mockStatement);
            Postgres.setup();
            verify(mockStatement, times(1)).executeUpdate("CREATE TABLE IF NOT EXISTS users(user_id VARCHAR (36) PRIMARY KEY, username VARCHAR (50) UNIQUE NOT NULL, password VARCHAR (50) NOT NULL, created_on TIMESTAMP NOT NULL, last_login TIMESTAMP)");
            verify(mockStatement, times(1)).executeUpdate("CREATE TABLE IF NOT EXISTS comments(id VARCHAR (36) PRIMARY KEY, username VARCHAR (36), body VARCHAR (500), created_on TIMESTAMP NOT NULL)");
            verify(mockStatement, times(1)).executeUpdate("DELETE FROM users");
            verify(mockStatement, times(1)).executeUpdate("DELETE FROM comments");
        } catch (Exception e) {
            fail("Setup should not throw an exception: " + e.getMessage());
        }
    }

    @Test
    public void Postgres_InsertUser_ShouldInsertUserSuccessfully() {
        // Mock connection and prepared statement
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);

        try {
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
            Postgres.insertUser("testuser", "testpassword");
            verify(mockPreparedStatement, times(1)).setString(1, UUID.randomUUID().toString());
            verify(mockPreparedStatement, times(1)).setString(2, "testuser");
            verify(mockPreparedStatement, times(1)).setString(3, Postgres.md5("testpassword"));
            verify(mockPreparedStatement, times(1)).executeUpdate();
        } catch (Exception e) {
            fail("InsertUser should not throw an exception: " + e.getMessage());
        }
    }

    @Test
    public void Postgres_InsertComment_ShouldInsertCommentSuccessfully() {
        // Mock connection and prepared statement
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);

        try {
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
            Postgres.insertComment("testuser", "test comment");
            verify(mockPreparedStatement, times(1)).setString(1, UUID.randomUUID().toString());
            verify(mockPreparedStatement, times(1)).setString(2, "testuser");
            verify(mockPreparedStatement, times(1)).setString(3, "test comment");
            verify(mockPreparedStatement, times(1)).executeUpdate();
        } catch (Exception e) {
            fail("InsertComment should not throw an exception: " + e.getMessage());
        }
    }

    @Test
    public void Postgres_MD5_ShouldReturnCorrectHash() {
        String input = "testpassword";
        String expectedHash = "5e884898da28047151d0e56f8dc6292773603d0d6aabbddc2f11d7e4";
        String actualHash = Postgres.md5(input);
        assertEquals("MD5 hash should match expected value", expectedHash, actualHash);
    }
}

