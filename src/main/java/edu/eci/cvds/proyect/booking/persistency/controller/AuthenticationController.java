package edu.eci.cvds.proyect.booking.persistency.controller;

import edu.eci.cvds.proyect.booking.exceptions.AppException;
import edu.eci.cvds.proyect.booking.persistency.entity.Login;
import edu.eci.cvds.proyect.booking.persistency.entity.PublicUser;
import edu.eci.cvds.proyect.booking.persistency.entity.User;
import edu.eci.cvds.proyect.booking.persistency.service.SessionService;
import edu.eci.cvds.proyect.booking.persistency.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
            User user = userService.loginUser(login.getName(), login.getPassword());
            return ResponseEntity.ok().body(Collections.singletonMap("cookie", this.sessionService.createSessionCookie(user)));
        } catch (Exception e) {
            if (e instanceof AppException) {
                return ((AppException) e).getResponse();
            }
            return ResponseEntity.status(500).body(Collections.singletonMap("error", "Server error"));
        }
    }

    @GetMapping("auth")
    public ResponseEntity<?> getUser(@RequestHeader("Authorization") String token) {
        try {
            sessionService.isSessionActive(token);
            return ResponseEntity.ok().body(new PublicUser(sessionService.getUserFromSession(token)));
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

