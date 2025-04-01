package edu.eci.cvds.proyect.booking.persistency.controller;

import edu.eci.cvds.proyect.booking.exceptions.AppException;
import edu.eci.cvds.proyect.booking.exceptions.SessionException;
import edu.eci.cvds.proyect.booking.exceptions.SessionException.InvalidSessionException;
import edu.eci.cvds.proyect.booking.persistency.entity.Login;
import edu.eci.cvds.proyect.booking.persistency.entity.User;
import edu.eci.cvds.proyect.booking.persistency.service.SessionService;
import edu.eci.cvds.proyect.booking.persistency.service.UserService;
import edu.eci.cvds.proyect.booking.users.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private SessionService sessionService;

    @InjectMocks
    private AuthenticationController controller;

    private User validUser;
    private final String validToken = "valid_token";
    private final String invalidToken = "invalid_token";

    @BeforeEach
    void setUp() {
        validUser = new User("1", "testUser", "test@example.com", UserRole.TEACHER, "password123");
    }

    // ------------------------- loginUser Tests -------------------------
    @Test
    void loginUser_WithValidCredentials_ReturnsSessionCookie() throws Exception {
        // Arrange
        Login login = new Login();
        login.setName("testUser");
        login.setPassword("password123");
        when(userService.loginUser(login.getName(), login.getPassword())).thenReturn(validUser);
        when(sessionService.createSessionCookie(validUser)).thenReturn("session_123");

        // Act
        ResponseEntity<?> response = controller.loginUser(login);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, String> responseBody = (Map<String, String>) response.getBody();
        assertNotNull(responseBody);
        assertEquals("session_123", responseBody.get("cookie"));
        verify(userService).loginUser("testUser", "password123");
        verify(sessionService).createSessionCookie(validUser);
    }

    @Test
    void loginUser_WithInvalidCredentials_ReturnsUnauthorized() throws Exception {
        // Arrange
        Login login = new Login();
        login.setName("invalidUser");
        login.setPassword("wrongPass");
        String errorMessage = "Credenciales inválidas";
        when(userService.loginUser(anyString(), anyString()))
                .thenThrow(new InvalidSessionException(errorMessage));

        // Act
        ResponseEntity<?> response = controller.loginUser(login);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        Map<String, String> responseBody = (Map<String, String>) response.getBody();
        assertNotNull(responseBody);
        // Ajustar el mensaje esperado al formato real
        assertEquals("Session with ID :" + errorMessage + " is invalid or unauthorized.",
                responseBody.get("error"));
        verify(userService).loginUser("invalidUser", "wrongPass");
    }
    @Test
    void loginUser_WithMissingName_ReturnsBadRequest() {
        // Arrange
        Login login = new Login();
        login.setPassword("password123");

        // Act
        ResponseEntity<?> response = controller.loginUser(login);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Map<String, String> responseBody = (Map<String, String>) response.getBody();
        assertNotNull(responseBody);
        assertEquals("Name and password are required", responseBody.get("error"));
    }

    @Test
    void loginUser_WithMissingPassword_ReturnsBadRequest() {
        // Arrange
        Login login = new Login();
        login.setName("testUser");

        // Act
        ResponseEntity<?> response = controller.loginUser(login);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Map<String, String> responseBody = (Map<String, String>) response.getBody();
        assertNotNull(responseBody);
        assertEquals("Name and password are required", responseBody.get("error"));
    }

    @Test
    void loginUser_WithServerError_ReturnsInternalError() throws Exception {
        // Arrange
        Login login = new Login();
        login.setName("testUser");
        login.setPassword("password123");
        when(userService.loginUser(anyString(), anyString()))
                .thenThrow(new RuntimeException("Database error"));

        // Act
        ResponseEntity<?> response = controller.loginUser(login);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        Map<String, String> responseBody = (Map<String, String>) response.getBody();
        assertNotNull(responseBody);
        assertEquals("Server error", responseBody.get("error"));
        verify(userService).loginUser("testUser", "password123");
    }

    // ------------------------- logoutUser Tests -------------------------
    @Test
    void logoutUser_WithValidToken_ReturnsSuccess() throws Exception {
        // Arrange
        doNothing().when(sessionService).invalidateSession(validToken);

        // Act
        ResponseEntity<?> response = controller.logoutUser(validToken);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, String> responseBody = (Map<String, String>) response.getBody();
        assertNotNull(responseBody);
        assertEquals("User logged out", responseBody.get("message"));
        verify(sessionService).invalidateSession(validToken);
    }

    @Test
    void logoutUser_WithInvalidToken_ReturnsUnauthorized() throws Exception {
        // Arrange
        String errorMessage = "Token inválido";
        doThrow(new InvalidSessionException(errorMessage))
                .when(sessionService).invalidateSession(invalidToken);

        // Act
        ResponseEntity<?> response = controller.logoutUser(invalidToken);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        Map<String, String> responseBody = (Map<String, String>) response.getBody();
        assertNotNull(responseBody);
        // Mensaje esperado con el formato real
        assertEquals("Session with ID :" + errorMessage + " is invalid or unauthorized.",
                responseBody.get("error"));
        verify(sessionService).invalidateSession(invalidToken);
    }

    @Test
    void logoutUser_WithNullToken_ReturnsBadRequest() throws SessionException {

        ResponseEntity<?> response = controller.logoutUser(null);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Map<String, String> responseBody = (Map<String, String>) response.getBody();
        assertNotNull(responseBody);
        assertEquals("Token requerido", responseBody.get("error"));
        verify(sessionService, never()).invalidateSession(any()); // No se debe invocar al servicio
    }
    @Test
    void logoutUser_WithServerError_ReturnsInternalError() throws Exception {
        // Arrange
        doThrow(new RuntimeException("Error de conexión"))
                .when(sessionService).invalidateSession(validToken);

        // Act
        ResponseEntity<?> response = controller.logoutUser(validToken);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        Map<String, String> responseBody = (Map<String, String>) response.getBody();
        assertNotNull(responseBody);
        assertEquals("Server error", responseBody.get("error"));
        verify(sessionService).invalidateSession(validToken);
    }
}