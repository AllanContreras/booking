package edu.eci.cvds.proyect.booking.persistency.service;

import edu.eci.cvds.proyect.booking.persistency.dto.UserDto;
import edu.eci.cvds.proyect.booking.persistency.entity.User;
import edu.eci.cvds.proyect.booking.persistency.repository.UserRepository;
import edu.eci.cvds.proyect.booking.users.UserRole;
import edu.eci.cvds.proyect.booking.persistency.security.CustomPasswordEncoder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CustomPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllUsersSuccess() {
        User user1 = new User(1, "Andres Silva", "AndresSilva@gmail.com", UserRole.TEACHER, "hashedPassword1");
        User user2 = new User(2, "Maria Lopez", "MariaLopez@gmail.com", UserRole.ADMIN, "hashedPassword2");

        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        List<User> users = userService.getAll();

        assertNotNull(users);
        assertEquals(2, users.size());
        assertEquals("Andres Silva", users.get(0).getName());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testGetAllUsersEmptyList() {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        List<User> users = userService.getAll();

        assertNotNull(users);
        assertTrue(users.isEmpty(), "La lista de usuarios debería estar vacía");
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testGetOneUserSuccess() {
        User user = new User(1, "Andres Silva", "AndresSilva@gmail.com", UserRole.TEACHER, "hashedPassword");

        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        User foundUser = userService.getOne(1);

        assertNotNull(foundUser);
        assertEquals("Andres Silva", foundUser.getName());
        verify(userRepository, times(1)).findById(1);
    }

    @Test
    void testGetOneUserNotFound() {
        when(userRepository.findById(99)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.getOne(99));

        assertEquals("Usuario no encontrado con ID: 99", exception.getMessage());
        verify(userRepository, times(1)).findById(99);
    }

    @Test
    void testSaveUserSuccess() {
        UserDto userDto = new UserDto("Andres Silva", "AndresSilva@gmail.com", UserRole.TEACHER, "password");
        User user = new User(1, "Andres Silva", "AndresSilva@gmail.com", UserRole.TEACHER, "hashedPassword");

        when(passwordEncoder.encode(userDto.getPassword())).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User savedUser = userService.save(userDto);

        assertNotNull(savedUser);
        assertEquals("Andres Silva", savedUser.getName());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testSaveUserFailure() {
        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.save(null));
        assertEquals("El usuario no puede ser nulo", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testAuthenticateSuccess() {
        String email = "AndresSilva@gmail.com";
        String rawPassword = "password";
        String hashedPassword = "hashedPassword";
        User user = new User(1, "Andres Silva", email, UserRole.TEACHER, hashedPassword);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);


        assertTrue(userService.authenticate(email, rawPassword));
    }

    @Test
    void testAuthenticateFailure() {
        String email = "notfound@gmail.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertFalse(userService.authenticate(email, "password"));
    }
}
