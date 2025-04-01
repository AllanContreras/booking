package edu.eci.cvds.proyect.booking.persistency.controller;

import edu.eci.cvds.proyect.booking.exceptions.UserException;
import edu.eci.cvds.proyect.booking.persistency.entity.User;
import edu.eci.cvds.proyect.booking.persistency.service.AuthorizationService;
import edu.eci.cvds.proyect.booking.persistency.service.UserService;
import edu.eci.cvds.proyect.booking.users.UserRole;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/User")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthorizationService authorizationService;

    @PostMapping
    public ResponseEntity<?> createUserAsUser(@RequestBody User user, UserRole role) {
        try {
            final User user2 = userService.createUserAsUser(user,role);
            return ResponseEntity.status(201).body(Collections.singletonMap("message ", "The user " + user2.getName() + " was created successfully."));
        } catch (Exception e) {
            if (e instanceof UserException) {
                return ((UserException) e).getResponse();
            }
            return ResponseEntity.status(500).body(Collections.singletonMap("error ", "Server error "));
        }
    }

    @PostMapping("/admin")
    public ResponseEntity<?> createUserAsAdmin(@RequestHeader("authorization") String token, @RequestBody User user, @RequestBody String roles) {
        try {
            final User user2 = userService.createUserAsAdmin(user);
            this.authorizationService.adminResource(token);
            return ResponseEntity.status(201).body(Collections.singletonMap("message", "The user " + user2.getName() + " was created successfully."));
        } catch (Exception e) {
            if (e instanceof UserException) {
                return ((UserException) e).getResponse();
            }
            return ResponseEntity.status(500).body(Collections.singletonMap("error  ", "Server error  "));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@RequestHeader("authorization") String token, @PathVariable String id) {
        try {
            this.authorizationService.adminResource(token);
            return ResponseEntity.ok(userService.getUserById(id));

        } catch (Exception e) {
            if (e instanceof UserException) {
                return ((UserException) e).getResponse();
            }
            return ResponseEntity.status(500).body(Collections.singletonMap(" error", " Server error"));
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers(@RequestHeader("authorization") String token) {
        try {
            this.authorizationService.adminResource(token);
            return ResponseEntity.ok(userService.getAllUsers());
        } catch (Exception e) {
            if (e instanceof UserException) {
                return ((UserException) e).getResponse();
            }
            return ResponseEntity.status(500).body(Collections.singletonMap("error  ", "Server error  "));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@RequestHeader("authorization") String token, @PathVariable String id, @RequestBody User user) {
        try {
            this.authorizationService.adminResource(token);
            return ResponseEntity.ok(userService.updateUser(id, user));
        } catch (Exception e) {
            if (e instanceof UserException) {
                return ((UserException) e).getResponse();
            }
            return ResponseEntity.status(500).body(Collections.singletonMap("error", "Server error"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@RequestHeader("authorization") String token, @PathVariable String id) {
        try {
            userService.deleteUser(id);
            this.authorizationService.adminResource(token);
            return ResponseEntity.ok(Collections.singletonMap("message", "User deleted successfully"));

        } catch (Exception e) {
            if (e instanceof UserException) {
                return ((UserException) e).getResponse();
            }
            return ResponseEntity.status(500).body(Collections.singletonMap("error", "Server error"));
        }
    }
}
