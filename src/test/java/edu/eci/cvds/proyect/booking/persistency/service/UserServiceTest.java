package edu.eci.cvds.proyect.booking.persistency.service;

import edu.eci.cvds.proyect.booking.persistency.dto.UserDto;
import edu.eci.cvds.proyect.booking.persistency.entity.User;
import edu.eci.cvds.proyect.booking.persistency.repository.UserRepository;
import edu.eci.cvds.proyect.booking.users.UserRole;

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

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllUsersSuccess() {
        User user1 = new User(1, "Andres Silva", "AndresSilva@gmail.com", UserRole.TEACHER, "123456");
        User user2 = new User(2, "Maria Lopez", "MariaLopez@gmail.com", UserRole.ADMIN, "654321");

        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        List<User> users = userService.getAll();

        assertNotNull(users);
        assertEquals(2, users.size());
        assertEquals("Andres Silva", users.get(0).getName());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testGetAllUsersFailure() {
        when(userRepository.findAll()).thenReturn(Arrays.asList());
    
        List<User> users = userService.getAll();
    
        assertNotNull(users);
        assertTrue(users.isEmpty(),"lista de usuarios vacia");
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testGetOneUserSuccess() {
        User user = new User(1, "Andres Silva", "AndresSilva@gmail.com", UserRole.TEACHER, "123456");

        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        User foundUser = userService.getOne(1);

        assertNotNull(foundUser);
        assertEquals("Andres Silva", foundUser.getName());
        verify(userRepository, times(1)).findById(1);
    }

    @Test
    void testGetOneUserFailure() {
        when(userRepository.findById(99)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.getOne(99);
        });

        assertEquals("Usuario no encontrado con ID: 99", exception.getMessage());
        verify(userRepository, times(1)).findById(99);
    }

    @Test
    void testSaveUserSuccess() {
        UserDto userDto = new UserDto("Andres Silva", "AndresSilva@gmail.com", UserRole.TEACHER, "123456");
        User user = new User(1, "Andres Silva", "AndresSilva@gmail.com", UserRole.TEACHER, "123456");

        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userRepository.findAll()).thenReturn(Arrays.asList(user));

        User savedUser = userService.save(userDto);

        assertNotNull(savedUser);
        assertEquals("Andres Silva", savedUser.getName());
        verify(userRepository, times(1)).save(any(User.class));
    }
    @Test
    void testSaveUserFailure() {
        UserDto userDto =  null;

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.save(userDto);
        });

        assertEquals("El usuario no puede ser nulo", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testUpdateUserSuccess() {
        UserDto userDto = new UserDto("Updated User", "updated@gmail.com", UserRole.ADMIN, "newpassword");
        User existingUser = new User(1, "Andres Silva", "AndresSilva@gmail.com", UserRole.TEACHER, "123456");

        when(userRepository.findById(1)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        User updatedUser = userService.update(1, userDto);

        assertNotNull(updatedUser);
        assertEquals("Updated User", updatedUser.getName());
        assertEquals("updated@gmail.com", updatedUser.getEmail());
        verify(userRepository, times(1)).save(existingUser);
    }
    @Test
        void testUpdateUserFailure() {
        UserDto userDto = new UserDto("Nonexistent User", "nonexistent@gmail.com", UserRole.ADMIN, "nopassword");

        when(userRepository.findById(99)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.update(99, userDto);
        });

        assertEquals("Usuario no encontrado con ID: 99", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testDeleteUserSuccess() {
        User user = new User(1, "Andres Silva", "AndresSilva@gmail.com", UserRole.TEACHER, "123456");

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        doNothing().when(userRepository).delete(user);

        User deletedUser = userService.delete(1);

        assertNotNull(deletedUser);
        assertEquals(1, deletedUser.getId());
        verify(userRepository, times(1)).delete(user);
    }
    @Test
    void testDeleteUserFailure() {
        when(userRepository.findById(99)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.delete(99);
        });

        assertEquals("Usuario no encontrado con ID: 99", exception.getMessage());
        verify(userRepository, never()).delete(any(User.class));
    }

    @Test
    void testAutoIncrementSuccess() {
        User existingUser = new User(1, "Andres Silva", "AndresSilva@gmail.com", UserRole.TEACHER, "123456");
        when(userRepository.findAll()).thenReturn(Arrays.asList(existingUser));
    
        UserDto newUserDto = new UserDto("Maria Lopez", "MariaLopez@gmail.com", UserRole.ADMIN, "654321");
        User newUser = new User(2, "Maria Lopez", "MariaLopez@gmail.com", UserRole.ADMIN, "654321");
    
        when(userRepository.save(any(User.class))).thenReturn(newUser);
    
        User savedUser = userService.save(newUserDto);
    
        assertNotNull(savedUser);
        assertEquals(2, savedUser.getId()); 
    
        verify(userRepository, times(1)).save(any(User.class));
    }
    

    @Test
    void testAutoIncrementFailure() {
        when(userRepository.findAll()).thenReturn(Collections.emptyList()); 
    
        User user = new User(1, "First User", "first@gmail.com", UserRole.TEACHER, "password");
        when(userRepository.save(any(User.class))).thenReturn(user); 
    
        Integer newId = userService.save(new UserDto("First User", "first@gmail.com", UserRole.TEACHER, "password")).getId();
    
        assertEquals(1, newId, "El primer ID debería ser 1 cuando la lista está vacía.");
        verify(userRepository, times(1)).findAll();
        verify(userRepository, times(1)).save(any(User.class));
    }
    
}
