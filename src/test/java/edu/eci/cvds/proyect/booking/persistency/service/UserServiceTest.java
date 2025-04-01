package edu.eci.cvds.proyect.booking.persistency.service;

import edu.eci.cvds.proyect.booking.exceptions.UserException;
import edu.eci.cvds.proyect.booking.persistency.entity.User;
import edu.eci.cvds.proyect.booking.persistency.repository.UserRepository;
import edu.eci.cvds.proyect.booking.users.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    private User validUser;
    private User existingUser;

    @BeforeEach
    void setUp() {
        validUser = new User(
                UUID.randomUUID().toString(),
                "validUser12",
                "valid@example.com",
                UserRole.TEACHER,
                "ValidPass123!"
        );

        existingUser = new User(
                UUID.randomUUID().toString(),
                "existingUser",
                "existing@example.com",
                UserRole.TEACHER,
                "ExistingPass123!"
        );
    }

    // Tests for createUserAsAdmin
    @Test
    void createUserAsAdmin_ShouldSetAdminRoleAndEncryptPassword() throws UserException {
        when(userRepository.save(userCaptor.capture())).thenReturn(validUser);

        User result = userService.createUserAsAdmin(validUser);

        assertEquals(UserRole.ADMIN, result.getRole());
        assertTrue(new BCryptPasswordEncoder().matches("ValidPass123!", result.getPassword()));
        assertNotNull(result.getId());
        verify(userRepository).save(userCaptor.getValue());
    }

    // Tests for createUserAsUser
    @Test
    void createUserAsUser_WithValidRole_ShouldCreateUser() throws UserException {
        when(userRepository.save(userCaptor.capture())).thenReturn(validUser);

        User result = userService.createUserAsUser(validUser, UserRole.TEACHER);

        assertEquals(UserRole.TEACHER, result.getRole());
        verify(userRepository).save(userCaptor.getValue());
    }

    @Test
    void createUserAsUser_WithInvalidRole_ShouldThrowException() {
        User invalidRoleUser = new User(
                UUID.randomUUID().toString(),
                "invalidRoleUser",
                "invalid@role.com",
                null,
                "Password123!"
        );

        assertThrows(UserException.UserInvalidValueException.class,
                () -> userService.createUserAsUser(invalidRoleUser, null));
    }

    // Tests for updateUser
    @Test
    void updateUser_WithPartialData_ShouldMergeChanges() throws UserException {
        User originalUser = new User(
                "user123",
                "OriginalName",
                "original@email.com",
                UserRole.TEACHER,
                "originalPassword"
        );

        User updateData = new User(
                "user123",
                null, // Name not updated
                "updated@email.com",
                UserRole.TEACHER,
                "newPassword123"
        );

        when(userRepository.findById("user123")).thenReturn(Optional.of(originalUser));
        when(userRepository.save(userCaptor.capture())).thenReturn(originalUser);

        User updated = userService.updateUser("user123", updateData);

        assertEquals("OriginalName", updated.getName());
        assertEquals("updated@email.com", updated.getEmail());
        assertEquals(UserRole.TEACHER, updated.getRole());
        assertTrue(new BCryptPasswordEncoder().matches("newPassword123", updated.getPassword()));
    }

    @Test
    void updateUser_WithoutPasswordChange_ShouldKeepOriginalPassword() throws UserException {
        // 1. Configuración del usuario original
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String originalPassword = "originalPassword";
        String originalEncryptedPassword = encoder.encode(originalPassword);

        User originalUser = new User(
                "user123",
                "OriginalName",
                "original@email.com",
                UserRole.TEACHER,
                originalEncryptedPassword
        );


        User updateData = new User(
                null,
                "NewName",
                null,
                null,
                null
        );


        when(userRepository.findById("user123")).thenReturn(Optional.of(originalUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // 4. Ejecutar el método
        User updated = userService.updateUser("user123", updateData);

        // 5. Verificaciones
        assertEquals("NewName", updated.getName(), "El nombre debe actualizarse");
        assertTrue(
                encoder.matches("originalPassword", updated.getPassword()),
                "La contraseña debe mantenerse igual"
        );
        verify(userRepository).save(argThat(user ->
                user.getPassword().equals(originalEncryptedPassword)
        ));
    }


    @Test
    void deleteUser_WithValidId_ShouldRemoveUser() throws UserException {
        // 1. Crear usuario con ID conocido
        User userToDelete = new User(
                "user123", // ID específico para la prueba
                "testUser",
                "test@example.com",
                UserRole.TEACHER,
                "password123"
        );

        // 2. Configurar mocks
        when(userRepository.findById("user123")).thenReturn(Optional.of(userToDelete));
        doNothing().when(userRepository).deleteById("user123");

        // 3. Ejecutar el método
        User deleted = userService.deleteUser("user123");

        // 4. Verificaciones
        assertEquals("user123", deleted.getId());
        verify(userRepository).deleteById("user123"); // Verifica que se borre con el ID correcto
    }
    @Test
    void deleteUser_WithInvalidId_ShouldThrowException() {
        // Configurar el mock para retornar Optional.empty() al buscar el ID
        when(userRepository.findById("invalidId")).thenReturn(Optional.empty());

        // Verificar que se lanza la excepción
        assertThrows(
                UserException.UserNotFoundException.class,
                () -> userService.deleteUser("invalidId")
        );


        verify(userRepository, never()).deleteById(anyString());
    }

    // Tests for getAllUsers
    @Test
    void getAllUsers_WhenEmpty_ShouldReturnEmptyList() throws UserException {
        when(userRepository.findAll()).thenReturn(List.of());

        List<User> result = userService.getAllUsers();

        assertTrue(result.isEmpty());
    }

    // Tests for loginUser
    @Test
    void loginUser_WithNonExistentUser_ShouldThrowException() {
        when(userRepository.findByName("unknown")).thenReturn(null);

        assertThrows(UserException.UserNotFoundException.class,
                () -> userService.loginUser("unknown", "anypassword"));
    }

    // Comprehensive validation tests
    @Test
    void validateUser_WithNullUser_ShouldThrowException() {
        assertThrows(UserException.UserInvalidValueException.class,
                () -> userService.validateUser(null));
    }

    @Test
    void validateUser_WithInvalidNameFormat_ShouldThrowException() {
        User invalidUser = new User(
                UUID.randomUUID().toString(),
                "inv@lid!", // Invalid characters
                "valid@email.com",
                UserRole.TEACHER,
                "ValidPass123!"
        );

        assertThrows(UserException.UserInvalidValueException.class,
                () -> userService.validateUser(invalidUser));
    }

    @Test
    void validateUser_WithShortPassword_ShouldThrowException() {
        User invalidUser = new User(
                UUID.randomUUID().toString(),
                "validUser",
                "valid@email.com",
                UserRole.TEACHER,
                "12345" // 5 characters
        );

        assertThrows(UserException.UserInvalidValueException.class,
                () -> userService.validateUser(invalidUser));
    }

    @Test
    void validateUser_WithDuplicateUsername_ShouldThrowException() {
        when(userRepository.findByName("duplicateUser")).thenReturn(existingUser);

        User duplicateUser = new User(
                UUID.randomUUID().toString(),
                "duplicateUser", // Existing username
                "unique@email.com",
                UserRole.TEACHER,
                "ValidPass123!"
        );

        assertThrows(UserException.UserConflictException.class,
                () -> userService.validateUser(duplicateUser));
    }

    @Test
    void validateRole_WithInvalidRole_ShouldThrowException() {

        assertThrows(
                UserException.UserInvalidValueException.class,
                () -> userService.validateRole(null)
        );
    }


    @Test
    void updateUser_WithMalformedEmail_ShouldThrowExceptionDuringValidation() {
        User originalUser = new User(
                "user123",
                "ValidName",
                "valid@email.com",
                UserRole.TEACHER,
                "ValidPass123!"
        );

        // Datos de actualización con email inválido
        User invalidUpdate = new User(
                "user123",
                "ValidName",
                "invalid-email", // Email mal formado
                UserRole.TEACHER,
                null
        );

        when(userRepository.findById("user123")).thenReturn(Optional.of(originalUser));

        // Verificar que se lanza la excepción
        assertThrows(
                UserException.UserInvalidValueException.class,
                () -> userService.updateUser("user123", invalidUpdate)
        );

        // Opcional: Verificar que no se guardó
        verify(userRepository, never()).save(any(User.class));
    }
    @Test
    void createUser_WithMinimumValidPassword_ShouldSucceed() throws UserException {
        User minPasswordUser = new User(
                UUID.randomUUID().toString(),
                "validUser12",
                "valid@example.com",
                UserRole.TEACHER,
                "123456" // Exactly 6 characters
        );

        when(userRepository.save(userCaptor.capture())).thenReturn(minPasswordUser);
        when(userRepository.findByName("validUser12")).thenReturn(null);
        when(userRepository.findByEmail("valid@example.com")).thenReturn(null);

        assertDoesNotThrow(() -> userService.createUserAsUser(minPasswordUser, UserRole.TEACHER));
    }
}