package edu.eci.cvds.proyect.booking.persistency.controller;

import edu.eci.cvds.proyect.booking.exceptions.AppException;
import edu.eci.cvds.proyect.booking.persistency.entity.Login;
import edu.eci.cvds.proyect.booking.persistency.entity.PublicUser;
import edu.eci.cvds.proyect.booking.persistency.entity.User;
import edu.eci.cvds.proyect.booking.persistency.service.SessionService;
import edu.eci.cvds.proyect.booking.persistency.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RequestMapping("/")
@RestController
public class AuthenticationController {

    @Autowired
    private UserService userService;

    @Autowired
    private SessionService sessionService;

    @PostMapping("auth")
    public ResponseEntity<?> loginUser(@RequestBody Login login) {
        try {
            // Validar campos requeridos
            if (login.getName() == null || login.getName().isEmpty() ||
                    login.getPassword() == null || login.getPassword().isEmpty()) {
                return ResponseEntity.status(400).body(Collections.singletonMap("error", "Name and password are required"));
            }

            User user = userService.loginUser(login.getName(), login.getPassword());
            return ResponseEntity.ok().body(Collections.singletonMap("cookie", this.sessionService.createSessionCookie(user)));
        } catch (Exception e) {
            if (e instanceof AppException) {
                return ((AppException) e).getResponse();
            }
            return ResponseEntity.status(500).body(Collections.singletonMap("error", "Server error"));
        }
    }


    @PostMapping("logout")
    public ResponseEntity<?> logoutUser(@RequestHeader("Authentication") String token) {
        try {
            if (token == null || token.isEmpty()) { // Validación explícita
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Collections.singletonMap("error", "Token requerido"));
            }
            this.sessionService.invalidateSession(token);
            return ResponseEntity.ok().body(Collections.singletonMap("message", "User logged out"));
        } catch (Exception e) {
            if (e instanceof AppException) {
                return ((AppException) e).getResponse();
            }
            return ResponseEntity.status(500).body(Collections.singletonMap("error", "Server error"));
        }
    }

}

