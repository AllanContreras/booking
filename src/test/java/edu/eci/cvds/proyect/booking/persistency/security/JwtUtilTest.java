package edu.eci.cvds.proyect.booking.persistency.security;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Date;

import edu.eci.cvds.proyect.booking.users.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import edu.eci.cvds.proyect.booking.persistency.entity.User;


class JwtUtilTest {

    private JwtUtil jwtUtil;

    @Mock
    private User mockUser;

    private String validToken;
    private String expiredToken;
    private final String SECRET_KEY = "secreto";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtUtil = new JwtUtil();

        // Configurar mock de usuario
        when(mockUser.getEmail()).thenReturn("user@example.com");
        when(mockUser.getRole()).thenReturn(UserRole.ADMIN);

        // Crear un token válido
        validToken = JWT.create()
                .withSubject(mockUser.getEmail())
                .withClaim("role", mockUser.getRole().name())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 3600000)) // 1 hora de validez
                .sign(Algorithm.HMAC256(SECRET_KEY));

        // Crear un token expirado
        expiredToken = JWT.create()
                .withSubject(mockUser.getEmail())
                .withClaim("role", mockUser.getRole().name())
                .withIssuedAt(new Date(System.currentTimeMillis() - 7200000)) // Emitido hace 2 horas
                .withExpiresAt(new Date(System.currentTimeMillis() - 3600000)) // Expirado hace 1 hora
                .sign(Algorithm.HMAC256(SECRET_KEY));
    }

    @Test
    void testGenerateToken() {
        String token = jwtUtil.generateToken(mockUser);
        assertNotNull(token, "El token generado no debe ser nulo");
    }

    @Test
    void testExtractEmail() {
        String email = jwtUtil.extractEmail(validToken);
        assertEquals(mockUser.getEmail(), email, "El email extraído del token debe coincidir con el usuario");
    }

    @Test
    void testExtractRole() {
        String role = jwtUtil.extractRole(validToken);
        assertEquals(mockUser.getRole().name(), role, "El rol extraído del token debe coincidir con el del usuario");
    }

    @Test
    void testValidateToken_Valid() {
        assertTrue(jwtUtil.validateToken(validToken, mockUser), "El token válido debe pasar la validación");
    }

    @Test
    void testValidateToken_InvalidUser() {
        User otherUser = mock(User.class);
        when(otherUser.getEmail()).thenReturn("other@example.com");
        assertFalse(jwtUtil.validateToken(validToken, otherUser), "El token no debe ser válido para otro usuario");
    }

    @Test
    void testValidateToken_Expired() {
        assertFalse(jwtUtil.validateToken(expiredToken), "Un token expirado no debe ser válido");
    }

    @Test
    void testValidateToken_InvalidToken() {
        String invalidToken = "invalid.token.string";
        assertFalse(jwtUtil.validateToken(invalidToken), "Un token inválido no debe ser válido");
    }

    @Test
    void testExtractEmail_InvalidToken() {
        String invalidToken = "invalid.token.string";
        assertThrows(Exception.class, () -> jwtUtil.extractEmail(invalidToken), "Extraer email de un token inválido debe lanzar excepción");
    }

    @Test
    void testExtractRole_InvalidToken() {
        String invalidToken = "invalid.token.string";
        assertThrows(Exception.class, () -> jwtUtil.extractRole(invalidToken), "Extraer rol de un token inválido debe lanzar excepción");
    }
}
