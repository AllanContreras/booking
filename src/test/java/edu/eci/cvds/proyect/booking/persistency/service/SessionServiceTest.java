package edu.eci.cvds.proyect.booking.persistency.service;

import edu.eci.cvds.proyect.booking.exceptions.SessionException;
import edu.eci.cvds.proyect.booking.persistency.entity.User;
import edu.eci.cvds.proyect.booking.users.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class SessionServiceTest {

    private SessionService sessionService;

    @BeforeEach
    void setUp() {
        sessionService = new SessionService();
    }

    @Test
    void testCreateSessionCookie() throws SessionException {
        User user = new User("1", "John Doe", "john@example.com", UserRole.ADMIN, "password");
        String sessionId = sessionService.createSessionCookie(user);

        assertNotNull(sessionId);
        assertTrue(sessionId.length() > 0);
    }

    @Test
    void testGetUserFromSessionValid() throws SessionException {
        User user = new User("2", "Jane Doe", "jane@example.com", UserRole.ADMIN, "password");
        String sessionId = sessionService.createSessionCookie(user);

        User resultUser = sessionService.getUserFromSession(sessionId);
        assertEquals(user.getId(), resultUser.getId());
    }

    @Test
    void testGetUserFromSessionThrowsSessionNotFoundException() {
        String invalidSessionId = UUID.randomUUID().toString();
        assertThrows(SessionException.SessionNotFoundException.class,
                () -> sessionService.getUserFromSession(invalidSessionId));
    }

    @Test
    void testRenewSessionValid() throws SessionException {
        User user = new User("3", "Alice", "alice@example.com", UserRole.ADMIN, "password");
        String sessionId = sessionService.createSessionCookie(user);

        assertTrue(sessionService.renewSession(sessionId));
    }

    @Test
    void testRenewSessionThrowsExpiredSessionException() {
        String invalidSessionId = UUID.randomUUID().toString();
        assertThrows(SessionException.ExpiredSessionException.class,
                () -> sessionService.renewSession(invalidSessionId));
    }

    @Test
    void testIsSessionActiveValid() throws SessionException {
        User user = new User("4", "Bob", "bob@example.com", UserRole.ADMIN, "password");
        String sessionId = sessionService.createSessionCookie(user);

        assertTrue(sessionService.isSessionActive(sessionId));
    }

    @Test
    void testIsSessionActiveThrowsSessionNotFoundException() {
        String invalidSessionId = UUID.randomUUID().toString();
        assertThrows(SessionException.SessionNotFoundException.class,
                () -> sessionService.isSessionActive(invalidSessionId));
    }

    @Test
    void testInvalidateSessionValid() throws SessionException {
        User user = new User("5", "Charlie", "charlie@example.com", UserRole.ADMIN, "password");
        String sessionId = sessionService.createSessionCookie(user);

        sessionService.invalidateSession(sessionId);
        assertThrows(SessionException.SessionNotFoundException.class,
                () -> sessionService.getUserFromSession(sessionId));
    }

    @Test
    void testInvalidateSessionThrowsSessionNotFoundException() {
        String invalidSessionId = UUID.randomUUID().toString();
        assertThrows(SessionException.SessionNotFoundException.class,
                () -> sessionService.invalidateSession(invalidSessionId));
    }

    @Test
    void testCleanExpiredSessions() throws SessionException {
        User user1 = new User("6", "Dave", "dave@example.com", UserRole.ADMIN, "password");
        User user2 = new User("7", "Eve", "eve@example.com", UserRole.ADMIN, "password");

        String session1 = sessionService.createSessionCookie(user1);
        String session2 = sessionService.createSessionCookie(user2);

        sessionService.cleanExpiredSessions();

        assertTrue(sessionService.isSessionActive(session1));
        assertTrue(sessionService.isSessionActive(session2));
    }
}
