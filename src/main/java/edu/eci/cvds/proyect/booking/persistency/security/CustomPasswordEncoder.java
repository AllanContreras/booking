package edu.eci.cvds.proyect.booking.persistency.security;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Component;

@Component
public class CustomPasswordEncoder {

    public String encode(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public boolean matches(String rawPassword, String encodedPassword) {
        if (rawPassword == null || encodedPassword == null) {
            throw new IllegalArgumentException("Both raw and encoded passwords must be non-null");
        }
        return BCrypt.checkpw(rawPassword, encodedPassword);
    }
}