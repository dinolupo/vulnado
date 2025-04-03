package com.scalesec.vulnado;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.mockito.Mockito;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LoginControllerTests {

    @Value("${app.secret}")
    private String secret;

    @Test
    public void login_ValidCredentials_ShouldReturnToken() {
        // Arrange
        LoginController controller = new LoginController();
        LoginRequest request = new LoginRequest();
        request.username = "validUser";
        request.password = "validPassword";

        User mockUser = mock(User.class);
        when(mockUser.hashedPassword).thenReturn(Postgres.md5("validPassword"));
        when(mockUser.token(secret)).thenReturn("validToken");

        // Mock static method User.fetch
        try (var mockedStatic = Mockito.mockStatic(User.class)) {
            mockedStatic.when(() -> User.fetch("validUser")).thenReturn(mockUser);

            // Act
            LoginResponse response = controller.login(request);

            // Assert
            assertNotNull("Token should not be null", response.token);
            assertEquals("Token should match expected value", "validToken", response.token);
        }
    }

    @Test
    public void login_InvalidCredentials_ShouldThrowUnauthorized() {
        // Arrange
        LoginController controller = new LoginController();
        LoginRequest request = new LoginRequest();
        request.username = "validUser";
        request.password = "invalidPassword";

        User mockUser = mock(User.class);
        when(mockUser.hashedPassword).thenReturn(Postgres.md5("validPassword"));

        // Mock static method User.fetch
        try (var mockedStatic = Mockito.mockStatic(User.class)) {
            mockedStatic.when(() -> User.fetch("validUser")).thenReturn(mockUser);

            // Act & Assert
            try {
                controller.login(request);
                fail("Expected Unauthorized exception to be thrown");
            } catch (Unauthorized ex) {
                assertEquals("Exception message should match", "Access Denied", ex.getMessage());
            }
        }
    }

    @Test
    public void login_NonExistentUser_ShouldThrowUnauthorized() {
        // Arrange
        LoginController controller = new LoginController();
        LoginRequest request = new LoginRequest();
        request.username = "nonExistentUser";
        request.password = "somePassword";

        // Mock static method User.fetch
        try (var mockedStatic = Mockito.mockStatic(User.class)) {
            mockedStatic.when(() -> User.fetch("nonExistentUser")).thenThrow(new Unauthorized("Access Denied"));

            // Act & Assert
            try {
                controller.login(request);
                fail("Expected Unauthorized exception to be thrown");
            } catch (Unauthorized ex) {
                assertEquals("Exception message should match", "Access Denied", ex.getMessage());
            }
        }
    }

    @Test
    public void login_NullRequest_ShouldThrowException() {
        // Arrange
        LoginController controller = new LoginController();

        // Act & Assert
        try {
            controller.login(null);
            fail("Expected exception to be thrown");
        } catch (NullPointerException ex) {
            assertNotNull("Exception should not be null", ex.getMessage());
        }
    }
}

