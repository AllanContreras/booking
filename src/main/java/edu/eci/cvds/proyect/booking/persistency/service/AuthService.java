package edu.eci.cvds.proyect.booking.persistency.service;

import edu.eci.cvds.proyect.booking.persistency.repository.UserRepository;
import edu.eci.cvds.proyect.booking.persistency.security.CustomPasswordEncoder;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.eci.cvds.proyect.booking.persistency.entity.User;

@Service
public class AuthService {

    private final UserRepository userRepository;

    @Autowired
    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean authenticate(String email, String password) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            return false; // Usuario no encontrado
        }

        User user = optionalUser.get();
        return CustomPasswordEncoder.matches(password, user.getPassword());
    }
}