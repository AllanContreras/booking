package edu.eci.cvds.proyect.booking.persistency.controller;

import java.util.Collections;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.eci.cvds.proyect.booking.persistency.dto.LoginRequest;
import edu.eci.cvds.proyect.booking.persistency.entity.User;
import edu.eci.cvds.proyect.booking.persistency.security.JwtUtil;
import edu.eci.cvds.proyect.booking.persistency.service.AuthService;
import edu.eci.cvds.proyect.booking.persistency.service.UserService;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:5180")
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    @Autowired
    public AuthController(AuthService authService, JwtUtil jwtUtil, UserService userService) {
        this.authService = authService;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Optional<User> user = userService.findByEmail(request.getEmail());

        if (user.isEmpty() || !authService.authenticate(request.getEmail(), request.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales incorrectas");
        }

        String token = jwtUtil.generateToken(user.get());
        return ResponseEntity.ok(Collections.singletonMap("token", token));
    }
}