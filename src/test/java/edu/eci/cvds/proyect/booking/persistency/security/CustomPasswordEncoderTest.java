package edu.eci.cvds.proyect.booking.persistency.security;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

class CustomPasswordEncoderTest {

    private CustomPasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        passwordEncoder = new CustomPasswordEncoder();
    }

    @Test
    void testEncodeNotNull() {
        String password = "securePassword123";
        String encodedPassword = passwordEncoder.encode(password);

        assertNotNull(encodedPassword, "Encoded password should not be null");
        assertNotEquals(password, encodedPassword, "Encoded password should not be the same as raw password");
    }

    @Test
    void testMatchesValidPassword() {
        String password = "correctPassword";
        String encodedPassword = passwordEncoder.encode(password);

        assertTrue(passwordEncoder.matches(password, encodedPassword), "Password should match the encoded version");
    }

    @Test
    void testMatchesInvalidPassword() {
        String correctPassword = "correctPassword";
        String incorrectPassword = "wrongPassword";
        String encodedPassword = passwordEncoder.encode(correctPassword);

        assertFalse(passwordEncoder.matches(incorrectPassword, encodedPassword), "Incorrect password should not match");
    }

    @Test
    void testEncodeMultipleTimesProducesDifferentHashes() {
        String password = "repeatablePassword";
        String encodedPassword1 = passwordEncoder.encode(password);
        String encodedPassword2 = passwordEncoder.encode(password);

        assertNotEquals(encodedPassword1, encodedPassword2, "Encoding the same password should produce different hashes due to salting");
    }

    @Test
    void testMatchesAgainstManuallyHashedPassword() {
        String password = "manualTestPassword";
        String manuallyEncodedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        assertTrue(passwordEncoder.matches(password, manuallyEncodedPassword), "Password should match manually encoded version");
    }

    @Test
    void testMatchesNullPassword() {
        String encodedPassword = passwordEncoder.encode("somePassword");

        assertThrows(IllegalArgumentException.class, () -> passwordEncoder.matches(null, encodedPassword), "Null password should throw exception");
    }

    @Test
    void testMatchesNullEncodedPassword() {
        assertThrows(IllegalArgumentException.class, () -> passwordEncoder.matches("password", null), "Null encoded password should throw exception");
    }

    @Test
    void testEncodeEmptyString() {
        String encodedPassword = passwordEncoder.encode("");
        assertNotNull(encodedPassword, "Encoding an empty string should not return null");
    }

    @Test
    void testMatchesWithEmptyRawPassword() {
        String encodedPassword = passwordEncoder.encode("password123");

        assertFalse(passwordEncoder.matches("", encodedPassword), "Empty raw password should not match an encoded password");
    }
}
