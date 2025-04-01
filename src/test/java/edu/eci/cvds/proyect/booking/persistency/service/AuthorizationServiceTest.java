package edu.eci.cvds.proyect.booking.persistency.service;

import edu.eci.cvds.proyect.booking.exceptions.SessionException;
import edu.eci.cvds.proyect.booking.persistency.entity.User;
import edu.eci.cvds.proyect.booking.users.UserRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorizationServiceTest {

    @Mock
    private SessionService sessionService;

    @InjectMocks
    private AuthorizationService authorizationService;

    @Test
    void testAdminResourceWithAdminUser() throws SessionException {
        User adminUser = new User("1", "Admin User", "admin@example.com", UserRole.ADMIN, "securePass");
        when(sessionService.getUserFromSession("valid_token")).thenReturn(adminUser);

        assertDoesNotThrow(() -> authorizationService.adminResource("valid_token"));
    }

    @Test
    void testAdminResourceWithNonAdminUserThrowsException() throws SessionException {
        User regularUser = new User("2", "Regular User", "user@example.com", UserRole.TEACHER, "password");
        when(sessionService.getUserFromSession("invalid_token")).thenReturn(regularUser);

        Exception exception = assertThrows(SessionException.InvalidSessionException.class,
                () -> authorizationService.adminResource("invalid_token"));

        // Comprobación más flexible del mensaje de la excepción
        assertTrue(exception.getMessage().contains("No access"));
    }

    @Test
    void testAdminResourceWithNullUserThrowsException() throws SessionException {
        when(sessionService.getUserFromSession("null_token")).thenReturn(null);

        assertThrows(NullPointerException.class, () -> authorizationService.adminResource("null_token"));
    }
}
