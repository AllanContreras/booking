package edu.eci.cvds.proyect.booking.persistency.controller;

import edu.eci.cvds.proyect.booking.exceptions.SessionException;
import edu.eci.cvds.proyect.booking.exceptions.UserException;
import edu.eci.cvds.proyect.booking.persistency.entity.User;
import edu.eci.cvds.proyect.booking.persistency.service.AuthorizationService;
import edu.eci.cvds.proyect.booking.persistency.service.UserService;
import edu.eci.cvds.proyect.booking.users.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private AuthorizationService authorizationService;

    @InjectMocks
    private UserController userController;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("1", "Test User", "test@example.com", UserRole.ADMIN, "password123");
    }

    @Test
    void createUserAsUser_ShouldReturnSuccess() throws UserException {
        when(userService.createUserAsUser(any(User.class), any(UserRole.class))).thenReturn(user);

        ResponseEntity<?> response = userController.createUserAsUser(user, UserRole.ADMIN);

        assertThat(response.getStatusCodeValue()).isEqualTo(201);
        assertThat(response.getBody()).isInstanceOf(Map.class);
        assertThat(((Map<?, ?>) response.getBody()).get("message")).isEqualTo(null);

        verify(userService, times(1)).createUserAsUser(any(User.class), any(UserRole.class));
    }

    @Test
    void createUserAsUser_ShouldHandleException() throws UserException {
        when(userService.createUserAsUser(any(User.class), any(UserRole.class)))
                .thenThrow(new UserException("Error creating user", 500));

        ResponseEntity<?> response = userController.createUserAsUser(user, UserRole.ADMIN);

        assertThat(response.getStatusCodeValue()).isEqualTo(500);
        assertThat(response.getBody()).isInstanceOf(Map.class);
        assertThat(((Map<?, ?>) response.getBody()).containsKey("error")).isTrue();
    }

    @Test
    void createUserAsAdmin_ShouldReturnSuccess() throws UserException, SessionException {
        when(userService.createUserAsAdmin(any(User.class))).thenReturn(user);
        doNothing().when(authorizationService).adminResource(anyString());

        ResponseEntity<?> response = userController.createUserAsAdmin("admin-token", user, "ADMIN");

        assertThat(response.getStatusCodeValue()).isEqualTo(201);
        assertThat(response.getBody()).isInstanceOf(Map.class);
        assertThat(((Map<?, ?>) response.getBody()).get("message")).isEqualTo("The user Test User was created successfully.");

        verify(userService, times(1)).createUserAsAdmin(any(User.class));
        verify(authorizationService, times(1)).adminResource("admin-token");
    }

    @Test
    void getUserById_ShouldReturnUser() throws UserException, SessionException {
        when(userService.getUserById("1")).thenReturn(user);
        doNothing().when(authorizationService).adminResource(anyString());

        ResponseEntity<?> response = userController.getUserById("admin-token", "1");

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(user);
    }

    @Test
    void getUserById_ShouldHandleUserNotFoundException() throws UserException {
        when(userService.getUserById("999")).thenThrow(new UserException.UserNotFoundException("999"));

        ResponseEntity<?> response = userController.getUserById("admin-token", "999");

        assertThat(response.getStatusCodeValue()).isEqualTo(404);
        assertThat(((Map<?, ?>) response.getBody()).get("error")).isEqualTo("User with ID: 999, not found in the database.");
    }

    @Test
    void getAllUsers_ShouldReturnUserList() throws UserException, SessionException {
        List<User> users = Arrays.asList(user, new User("2", "Another User", "another@example.com", UserRole.ADMIN, "password456"));
        when(userService.getAllUsers()).thenReturn(users);
        doNothing().when(authorizationService).adminResource(anyString());

        ResponseEntity<?> response = userController.getAllUsers("admin-token");

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(users);
    }

    @Test
    void updateUser_ShouldReturnUpdatedUser() throws UserException, SessionException {
        when(userService.updateUser(eq("1"), any(User.class))).thenReturn(user);
        doNothing().when(authorizationService).adminResource(anyString());

        ResponseEntity<?> response = userController.updateUser("admin-token", "1", user);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(user);
    }

    @Test
    void updateUser_ShouldHandleUserNotFoundException() throws UserException {
        when(userService.updateUser(eq("999"), any(User.class))).thenThrow(new UserException.UserNotFoundException("999"));

        ResponseEntity<?> response = userController.updateUser("admin-token", "999", user);

        assertThat(response.getStatusCodeValue()).isEqualTo(404);
        assertThat(((Map<?, ?>) response.getBody()).get("error")).isEqualTo("User with ID: 999, not found in the database.");
    }


    @Test
    void deleteUser_ShouldReturnDeletedUser() throws Exception {

        when(userService.deleteUser("1")).thenReturn(user);
        doNothing().when(authorizationService).adminResource("admin-token");


        ResponseEntity<?> response = userController.deleteUser("admin-token", "1");

        // Verificar
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isInstanceOf(User.class);
        assertThat((User) response.getBody()).isEqualTo(user);

        verify(userService, times(1)).deleteUser("1");
    }

    @Test
    void deleteUser_ShouldHandleUserNotFoundException() throws UserException {
        doThrow(new UserException.UserNotFoundException("999")).when(userService).deleteUser("999");

        ResponseEntity<?> response = userController.deleteUser("admin-token", "999");

        assertThat(response.getStatusCodeValue()).isEqualTo(404);
        assertThat(((Map<?, ?>) response.getBody()).get("error"))
                .isEqualTo("User with ID: 999, not found in the database.");
    }

}
