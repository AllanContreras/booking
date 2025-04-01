package edu.eci.cvds.proyect.booking.persistency.service;

import edu.eci.cvds.proyect.booking.exceptions.SessionException;
import edu.eci.cvds.proyect.booking.persistency.entity.User;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.UUID;

@Service
public class SessionService implements SessionsService {
    private static final int COOKIE_MAX_AGE_MIN = 1800;
    private final Map<String, SessionInfo> activeSessions = new ConcurrentHashMap<>();

    public User getUserFromSession(String sessionId) throws SessionException {
        SessionInfo sessionInfo = activeSessions.get(sessionId);

        System.out.println(sessionInfo);

        activeSessions.forEach((key, value) -> {
            System.out.println(key + ": : :" + value);
        });

        if (sessionInfo == null) {
            throw new SessionException.SessionNotFoundException(sessionId);
        }
        if (System.currentTimeMillis() > sessionInfo.expirationTime) {
            activeSessions.remove(sessionId);
            throw new SessionException.ExpiredSessionException(sessionId);
        }

        return sessionInfo.user;
    }

    public String createSessionCookie(User user) throws SessionException {
        String sessionId = UUID.randomUUID().toString();
        long expirationTime = System.currentTimeMillis() + (COOKIE_MAX_AGE_MIN * 60000);

        activeSessions.put(sessionId, new SessionInfo(user, expirationTime));

        return sessionId;

        // // Create cookie in the Backend
        // return ResponseCookie.from("SESSION_ID", sessionId)
        // .maxAge(COOKIE_MAX_AGE)
        // .path("/")
        // .httpOnly(true)
        // .build();
    }

    public boolean renewSession(String sessionId) throws SessionException {
        SessionInfo sessionInfo = activeSessions.get(sessionId);

        if (sessionInfo == null || System.currentTimeMillis() > sessionInfo.expirationTime) {
            throw new SessionException.ExpiredSessionException(sessionId);
        }

        sessionInfo.expirationTime = System.currentTimeMillis() + (COOKIE_MAX_AGE_MIN * 60000);
        return true;
    }

    public boolean isSessionActive(String sessionId) throws SessionException {
        User user = getUserFromSession(sessionId);
        if (user == null) {
            throw new SessionException.SessionNotFoundException(sessionId);
        }
        return activeSessions.containsKey(sessionId);
    }

    public void invalidateSession(String sessionId) throws SessionException {
        if (activeSessions.remove(sessionId) == null) {
            throw new SessionException.SessionNotFoundException(sessionId);
        }
    }

    public void cleanExpiredSessions() {
        long currentTime = System.currentTimeMillis();
        activeSessions.entrySet().removeIf(entry -> entry.getValue().expirationTime < currentTime);
    }

    private static class SessionInfo {
        User user;
        long expirationTime;

        SessionInfo(User user, long expirationTime) {
            this.user = user;
            this.expirationTime = expirationTime;
        }

        @Override
        public String toString() {
            return user.getName() + ": " + expirationTime;
        }
    }
}