package edu.eci.cvds.proyect.booking.persistency.service;

import edu.eci.cvds.proyect.booking.exceptions.SessionException;
import edu.eci.cvds.proyect.booking.persistency.entity.User;
import edu.eci.cvds.proyect.booking.users.UserRole;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

@Service
public class AuthorizationService {

    @Autowired
    SessionService sessionService;

    public void adminResource(String token) throws SessionException {
        User user = sessionService.getUserFromSession(token);
        if (!user.getRole().equals(UserRole.ADMIN)) {
            throw new SessionException.InvalidSessionException("No access");
        }
    }
}
